# TTD 统一特征平台 — 部署说明文档

## 1. 系统架构与部署拓扑

```
                    ┌──────────────────────┐
                    │   Nacos Config Center │  配置中心（必须最先启动）
                    │      (≥ 2.x)          │
                    └──────────┬───────────┘
                               │
            ┌──────────────────┼──────────────────┐
            │                  │                  │
     ┌──────▼──────┐   ┌──────▼──────┐   ┌──────▼──────┐
     │   MySQL 8.0  │   │ Redis       │   │   Kafka     │
     │  (tcbiz_     │   │ (Redisson)  │   │  (≥ 3.x)    │
     │  feature)    │   │             │   │             │
     └──────┬───────┘   └──────┬──────┘   └──────┬──────┘
            │                  │                  │
            └──────────────────┼──────────────────┘
                               │
     ┌─────────────────────────────────────────────────┐
     │              应用服务层                           │
     │                                                  │
     │  ┌──────────────────────┐  ┌──────────────────┐  │
     │  │ biz-feature-admin-srv│  │ ttd-feature-     │  │
     │  │ (运营后台 :7082)      │  │ data-srv         │  │
     │  │ Spring Boot 3.5      │  │ (特征查询服务)    │  │
     │  └──────────────────────┘  └──────────────────┘  │
     │                                                  │
     │  ┌──────────────────────────────────────────┐    │
     │  │         ttd-feature (计算引擎)             │    │
     │  │  ┌──────────────────────────────────┐    │    │
     │  │  │ ttd-feature-sample (:8081)       │    │    │
     │  │  │ 或 Flink Cluster                 │    │    │
     │  │  └──────────────────────────────────┘    │    │
     │  └──────────────────────────────────────────┘    │
     └─────────────────────────────────────────────────┘
```

## 2. 环境要求

| 组件      | 最低版本   | 推荐版本       | 说明                  |
|---------|--------|------------|---------------------|
| JDK     | 17     | 17.0.x LTS | 所有 Java 模块均需 JDK 17 |
| Maven   | 3.8    | 3.9.x      | 构建工具                |
| Node.js | 20.0.0 | 22.x LTS   | 前端构建                |
| Yarn    | 1.22   | latest     | 前端包管理               |
| MySQL   | 8.0    | 8.0.33     | 持久化存储（运营后台）         |
| Redis   | 7.0    | 7.x        | 特征缓存 + 分布式锁         |
| Nacos   | 2.2    | 2.4.x      | 配置中心                |
| Kafka   | 3.0    | 3.6.x      | 事件消息队列              |

## 3. 中间件安装与初始化

### 3.1 MySQL 数据库

```bash
# 1. 创建数据库
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS tcbiz_feature DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 2. 执行建表语句
mysql -u root -p tcbiz_feature < biz-feature-admin-srv/src/main/resources/db/schema.sql

# 3. 初始化管理员账户（可选）
mysql -u root -p tcbiz_feature < biz-feature-admin-srv/src/main/resources/db/init_admin_account.sql
```

### 3.2 Nacos 配置中心

```bash
# 下载并启动 Nacos（standalone 模式）
wget https://github.com/alibaba/nacos/releases/download/2.4.3/nacos-server-2.4.3.tar.gz
tar -xzf nacos-server-2.4.3.tar.gz
cd nacos/bin

# Mac/Linux
sh startup.sh -m standalone

# 创建命名空间
# 登录 http://localhost:8848/nacos (默认账户: nacos/nacos)
# 进入 命名空间 → 新建命名空间
#   命名空间ID: dev
#   命名空间名: 开发环境

# 在 dev 命名空间下创建配置项:
#   group: ttd-feature-sample
#   dataId: mysql.properties → 内容见下方
#   dataId: kafka.properties → 内容见下方
#   dataId: redis.properties → 内容见下方
```

**Nacos 配置内容示例：**

`mysql.properties` (group: ttd-feature-sample):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tcbiz_feature?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=your_password
```

`redis.properties` (group: ttd-feature-sample):

```properties
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=
spring.redis.database=0
spring.redis.timeout=3000ms
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-idle=8
spring.redis.lettuce.pool.min-idle=0
```

`kafka.properties` (group: ttd-feature-sample):

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=ttd-feature-consumer
spring.kafka.consumer.auto-offset-reset=latest
spring.kafka.consumer.enable-auto-commit=true
```

### 3.3 Redis

```bash
# Docker 方式
docker run -d --name redis -p 6379:6379 redis:7-alpine

# 或 macOS
brew install redis && brew services start redis
```

### 3.4 Kafka

```bash
# Docker Compose 方式（推荐）
cat <<EOF > docker-compose.yml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.6.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
  kafka:
    image: confluentinc/cp-kafka:7.6.0
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    depends_on:
      - zookeeper
EOF

docker-compose up -d
```

## 4. 应用服务部署

### 4.1 构建

```bash
# 克隆项目
cd hxttd_feature

# ==================== 计算引擎（ttd-feature）====================
# 需要先安装内部依赖模块（language, api, ttd-const, utils, cache 等）
# 这些为内部 SNAPSHOT 包，需确保本地 Maven 仓库已安装
cd ttd-feature
mvn clean install -DskipTests

# ==================== 运营后台（biz-feature-admin-srv）====================
cd ../biz-feature-admin-srv
mvn clean package -DskipTests

# ==================== 特征数据服务（ttd-feature-data-srv）====================
cd ../ttd-feature-data-srv
mvn clean package -DskipTests
```

### 4.2 启动顺序

必须按以下顺序启动：

```
第一步：Nacos        →  配置中心必须最先就绪
第二步：MySQL + Redis + Kafka →  基础中间件
第三步：biz-feature-admin-srv  →  运营后台（端口 7082）
第四步：ttd-feature-sample     →  计算引擎开发调试（端口 8081）
第五步：biz-feature-oss-web    →  前端开发服务器（端口 8000）
```

### 4.3 启动运营后台

```bash
cd biz-feature-admin-srv

# 方式一：Maven 插件
mvn spring-boot:run

# 方式二：Jar 包
java -jar target/biz-feature-admin-srv-1.0.0.jar

# 方式三：IDEA
# 运行 com.ly.ttd.biz.admin.BizTtdAdminSrvApplication 主类

# 验证
curl http://localhost:7082/api/health/check
```

### 4.4 启动计算引擎

```bash
cd ttd-feature/ttd-feature-sample

# 方式一：Maven 插件
mvn spring-boot:run

# 方式二：IDEA
# 运行 TtdFeatureSampleApplication 主类

# 验证
curl http://localhost:8081/actuator/health
```

### 4.5 启动前端

```bash
cd biz-feature-oss-web

# 安装依赖
yarn install

# 开发模式启动
yarn dev
# 访问: http://localhost:8000

# 生产构建
yarn build
# 产出在 dist/ 目录，部署到 Nginx 等静态服务器
```

## 5. Flink 生产部署

### 5.1 Flink 集群提交

```bash
# 打包 Flink 任务
cd ttd-feature/ttd-feature-flink-task
mvn clean package -DskipTests

# 提交到 Flink 集群
flink run -c com.ly.ttd.feature.flink.task.FeatureCountTask \
  -p 4 \
  target/ttd-feature-flink-task-1.0.0-SNAPSHOT.jar

# 各任务说明
# FeatureCountTask   → COUNT / COUNT_DISTINCT 聚合
# FeatureSumTask     → SUM 聚合
# FeatureAvgTask     → AVG 聚合
# FeatureMaxTask     → MAX 聚合
# FeatureMinTask     → MIN 聚合
```

### 5.2 Flink 配置要点

```yaml
# flink-conf.yaml 关键配置
state.backend: rocksdb
state.checkpoints.dir: hdfs://namenode:8020/flink/checkpoints
state.savepoints.dir: hdfs://namenode:8020/flink/savepoints
execution.checkpointing.interval: 60s
execution.checkpointing.min-pause: 30s
```

## 6. 配置参数说明

### 6.1 biz-feature-admin-srv (application.yml)

| 配置项                                                       | 默认值                                         | 说明            |
|-----------------------------------------------------------|---------------------------------------------|---------------|
| `server.port`                                             | 7082                                        | 服务端口          |
| `enc.key`                                                 | —                                           | 配置加密密钥（AES）   |
| `spring.datasource.url`                                   | `jdbc:mysql://localhost:3306/tcbiz_feature` | 数据库连接         |
| `spring.datasource.username`                              | root                                        | 数据库用户名        |
| `spring.datasource.password`                              | admin123                                    | 数据库密码         |
| `spring.cache.caffeine.spec`                              | `maximumSize=1000,expireAfterWrite=10m`     | 本地缓存配置        |
| `mybatis-plus.global-config.db-config.logic-delete-field` | deleted                                     | 逻辑删除字段        |
| `mybatis-plus.global-config.db-config.logic-delete-value` | 1                                           | 已删除标识         |
| `springdoc.swagger-ui.enabled`                            | true                                        | Swagger UI 开关 |

### 6.2 ttd-feature-sample (application.properties)

| 配置项                         | 默认值                | 说明         |
|-----------------------------|--------------------|------------|
| `server.port`               | 8081               | 服务端口       |
| `ttd.feature.project.id`    | 1                  | 当前项目 ID    |
| `ttd.nacos.server-addr`     | `127.0.0.1:8848`   | Nacos 地址   |
| `ttd.nacos[n].namespace`    | dev                | 命名空间       |
| `ttd.nacos[n].group`        | ttd-feature-sample | 配置分组       |
| `ttd.nacos[n].dataId`       | —                  | 配置 Data ID |
| `dynamictp.enabled`         | true               | 动态线程池开关    |
| `dynamictp.monitorInterval` | 5                  | 线程池监控间隔（秒） |

### 6.3 动态线程池说明

| 线程池名称                   | 别名                 | 核心/最大线程 | 用途           |
|-------------------------|--------------------|---------|--------------|
| THREAD_META_FIELD       | MetaFieldPool      | 4/8     | 元字段脚本执行      |
| THREAD_METRIC           | MetricPool         | 8/16    | 指标计算         |
| THREAD_FEATURE_WRITE    | FeatureWritePool   | 4/8     | 特征异步写入 Kafka |
| THREAD_FEATURE          | FeaturePool        | 8/16    | 通用特征处理       |
| THREAD_LOG              | LogPool            | 2/4     | 日志记录         |
| THREAD_FACTOR_GET_VALUE | FactorGetValuePool | 8/16    | 指标值获取        |
| THREAD_CONNECTOR        | ConnectorPool      | 4/8     | 连接器数据查询      |
| THREAD_HBASE_CLIENT     | HbaseClientPool    | 10/20   | HBase 客户端    |

## 7. 前端部署

### 7.1 Nginx 配置示例

```nginx
server {
    listen 80;
    server_name feature-admin.example.com;

    root /opt/ttd-feature-oss-web/dist;
    index index.html;

    # SPA 路由支持
    location / {
        try_files $uri $uri/ /index.html;
    }

    # API 代理
    location /api/ {
        proxy_pass http://127.0.0.1:7082;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    # Swagger 代理
    location /v3/api-docs {
        proxy_pass http://127.0.0.1:7082;
    }

    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

### 7.2 前端环境变量

```bash
# .env 文件（开发环境）
UMI_ENV=dev
API_BASE=http://localhost:7082

# 生产构建
UMI_ENV=prod
API_BASE=https://feature-admin.example.com
```

## 8. Docker Compose 一键部署（开发环境）

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0.33
    environment:
      MYSQL_ROOT_PASSWORD: admin123
      MYSQL_DATABASE: tcbiz_feature
    ports:
      - "3306:3306"
    volumes:
      - ./biz-feature-admin-srv/src/main/resources/db/schema.sql:/docker-entrypoint-initdb.d/01-schema.sql
      - mysql_data:/var/lib/mysql

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data

  nacos:
    image: nacos/nacos-server:v2.4.3
    environment:
      MODE: standalone
      PREFER_HOST_MODE: hostname
    ports:
      - "8848:8848"
      - "9848:9848"

  kafka:
    image: confluentinc/cp-kafka:7.6.0
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    depends_on:
      - zookeeper

  zookeeper:
    image: confluentinc/cp-zookeeper:7.6.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181

volumes:
  mysql_data:
  redis_data:
```

```bash
# 启动所有中间件
docker-compose up -d

# 然后按顺序启动应用
# 1. 启动 Nacos → 导入配置
# 2. 启动 biz-feature-admin-srv
# 3. 启动 ttd-feature-sample
# 4. 启动前端 dev server
```

## 9. 健康检查与监控

### 9.1 健康检查端点

| 服务             | 端点                      | 说明              |
|----------------|-------------------------|-----------------|
| admin-srv      | `GET /api/health/check` | 运营后台健康状态        |
| feature-sample | `GET /actuator/health`  | 引擎健康状态          |
| Swagger        | `GET /swagger-ui.html`  | API 文档          |
| OpenAPI        | `GET /v3/api-docs`      | OpenAPI 规范 JSON |

### 9.2 关键指标

| 指标             | 说明                            | 告警阈值    |
|----------------|-------------------------------|---------|
| 线程池队列使用率       | dynamic-tp 自动上报               | > 80%   |
| 线程池拒绝次数        | dynamic-tp 自动上报               | > 0     |
| JVM 堆内存        | 通过 JMX 暴露                     | > 80%   |
| Kafka 消费 Lag   | Kafka 自带指标                    | > 1000  |
| Connector 查询延迟 | ConnectorTraceSaveObserver 记录 | > 500ms |

### 9.3 日志配置

```bash
# 日志文件路径
biz-feature-admin-srv:   ./logs/biz-ttd-srv.log
ttd-feature-sample:       /home/logs/dynamictp/ttd-feature-engine/
```

## 10. 常见问题排查

### 10.1 启动失败

| 现象         | 可能原因              | 解决方案                                       |
|------------|-------------------|--------------------------------------------|
| 无法连接 Nacos | Nacos 未启动或地址错误    | 检查 `ttd.nacos.server-addr` 配置，确保 Nacos 已启动 |
| 无法连接 MySQL | 数据库连接信息错误         | 检查 `spring.datasource.*` 配置                |
| 表不存在       | 未执行建表脚本           | 执行 `schema.sql`                            |
| Bean 创建失败  | 依赖的 SNAPSHOT 包未安装 | 先执行 `mvn clean install -DskipTests`        |
| 端口被占用      | 已有进程占用            | `lsof -i :7082` 查看端口占用                     |

### 10.2 运行时问题

| 现象           | 可能原因             | 解决方案                                    |
|--------------|------------------|-----------------------------------------|
| 特征查询返回空      | 配置未加载或 Kafka 无事件 | 检查 `feature_config` 表配置，确认 Kafka 事件正在发送 |
| 元字段脚本执行超时    | 脚本逻辑复杂或外部依赖超时    | 调整 `timeout` 字段值，优化脚本                   |
| Redis 连接超时   | Redis 不可达或网络问题   | 检查 Redis 状态和网络连通性                       |
| Flink 任务 OOM | 状态过大             | 增加 TaskManager 内存，使用 RocksDB 状态后端       |

### 10.3 配置热加载不生效

```bash
# 确认 Nacos 配置已更新
curl "http://localhost:8848/nacos/v1/cs/configs?dataId=application.properties&group=ttd-feature-sample&tenant=dev"

# 手动触发刷新
curl -X POST http://localhost:8081/actuator/refresh
```
