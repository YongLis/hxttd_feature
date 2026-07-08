# hxttd_feature 统一特征平台

面向支付风控场景的统一特征平台，提供**特征配置管理**、**实时/离线特征计算**、**多语言规则执行**、**数据源连接器**等能力。采用
Maven 多模块架构，支持 Flink 实时流处理与 Spring Boot 微服务部署。

## 项目结构

```
hxttd_feature/
├── ttd-feature/                          # 特征引擎核心（父模块）
│   ├── ttd-feature-common/               #   公共类：枚举、DTO、注解、上下文
│   ├── ttd-feature-cfg-jar/              #   配置中心：12 个缓存容器 + 资源加载器
│   ├── ttd-feature-biz-jar/              #   业务核心：连接器/数据/因子/流速/降级/Kafka
│   ├── ttd-feature-engine/               #   引擎入口：FeatureEngineService 统一 API
│   └── connector-core/                   #   连接器框架（SPI 插件体系）
│       ├── connector-api/                #     SPI 接口 + AbstractConnector + 拦截器链
│       └── connector-plugin/             #     数据源插件
│           ├── connector-jdbc-plugin/    #       JDBC 连接器
│           ├── connector-es-plugin/      #       Elasticsearch 连接器
│           ├── connector-hbase-plugin/   #       HBase 连接器
│           └── connector-http-plugin/    #       HTTP 连接器
├── ttd-language/                         # 多语言脚本执行引擎（Groovy/Aviator/Jexl）
├── ttd-nacos-common/                     # 基础设施：Nacos/Redis/Kafka/dynamic-tp
├── ttd-feature-flink-task/               # Flink 实时聚合任务（Count/Sum/Avg/Max/Min）
├── ttd-feature-sample/                   # 开发启动器 + 线程池配置
├── biz-feature-admin-srv/                # 管理后台服务（Spring Boot）
├── biz-feature-oss-web/                  # 前端控制台（UmiJS + Ant Design）
├── PROJECT_README.md                     # 本文件
├── DEPLOYMENT.md                         # 部署说明
└── schema.sql                            # 建表语句
```

## 技术栈

| 类别     | 技术                      | 版本                    |
|--------|-------------------------|-----------------------|
| 基础框架   | Spring Boot             | 3.5.0                 |
| ORM    | MyBatis-Plus            | 3.5.13                |
| 实时计算   | Apache Flink            | 2.2.0                 |
| 脚本引擎   | Groovy / Aviator / Jexl | 5.0.4 / 5.3.3 / 3.5.0 |
| 配置中心   | Nacos Client            | 3.1.1                 |
| 缓存     | Redis (Redisson)        | 3.52.0                |
| 消息队列   | Kafka                   | —                     |
| 数据库连接池 | Druid                   | 1.2.28                |
| 线程池    | dynamic-tp              | 1.2.2                 |
| 前端     | UmiJS + Ant Design      | —                     |
| JSON   | Fastjson2 / Jackson     | 2.0.58 / 2.18.2       |

## 模块说明

### ttd-feature（特征引擎核心）

#### ttd-feature-common

公共基础模块，定义了整个平台共用的枚举、DTO、上下文对象和注解。

- **`TxnParamContext`** — 交易参数上下文，承载单次特征计算的输入/输出
- **`ScriptVariable`** — 脚本执行变量封装（语言类型 + 脚本 + 参数）
- **`ObjectTypeEnum`** — 特征返回值类型枚举（LIST/LONG/DOUBLE/DECIMAL/DATE/DICT/STRING/BOOLEAN）
- **`ScriptType`** — 脚本语言枚举（GROOVY/AVIATOR/JEXL）
- **`@FunctionDef` / `@MethodName`** — 自定义函数标记注解
- **`FunctionTip` / `ParamObj`** — 函数提示元数据结构

#### ttd-feature-cfg-jar

配置管理模块，负责加载和缓存特征配置元数据。

- **`FeatureConfiguration`** — 核心配置类，维护 12 个 `ConcurrentHashMap` 缓存：
  - 项目-用户映射、特征配置、因子定义、因子依赖关系
  - 连接器配置、数据结构、数据字段、事件消息
  - 接入点配置、接入点参数、字典码表
- **`FeatureConfigurationAware`** — 配置感知接口，供下游模块获取配置
- **`ResourceLoader`** 系列 — 各类配置的资源加载器

#### ttd-feature-biz-jar

业务核心模块（93 个类），包含完整的特征计算链路。

```
srv/
├── connector/        # 连接器服务：调用 SPI 插件获取外部数据
├── data/             # 数据服务：数据结构解析、字段映射
├── factor/           # 因子计算：
│   └── velocity/     #   流速计算（滑动窗口聚合）
│       ├── db/       #     数据库存储实现（Day/Month/Year/Persist）
│       ├── handler/  #     处理器链
│       ├── vel/      #     流速计算逻辑（calculate/filter）
│       └── dto/      #     数据对象
├── kafka/            # Kafka 消息处理
├── meta/             # 元数据服务
└── fallback/         # 降级策略
```

#### ttd-feature-engine

引擎对外统一入口。

```java
// FeatureEngineService — 唯一对外 API
ExecuteResult execute(TxnParamContext context);
```

调用链：`EngineService` → `FactorService` → `ConnectorService` → `DataSource SPI`，结果写入 `TxnParamContext`。

#### connector-core（连接器框架）

SPI 插件体系，支持热插拔式扩展数据源。

- **`Connector`** — SPI 核心接口，定义 `execute(ConnectorRequest)` 方法
- **`ConnectorProvider`** — SPI 提供者接口，通过 `ServiceLoader` 发现插件
- **`ConnectorInterceptor`** — 拦截器接口，支持前置/后置处理
- **`ConnectorObserver`** — 观察者接口，监听连接器执行事件
- **`AbstractConnector`** — 模板基类，封装拦截器链、缓存、异常处理
- **`ConnectorEngine`** — 连接器引擎，统一路由到具体插件

已实现 4 个数据源插件：

| 插件                     | 用途                            |
|------------------------|-------------------------------|
| connector-jdbc-plugin  | 关系型数据库查询（MySQL/PostgreSQL/H2） |
| connector-es-plugin    | Elasticsearch 查询              |
| connector-hbase-plugin | HBase 查询                      |
| connector-http-plugin  | HTTP API 调用                   |

### ttd-language（多语言脚本引擎）

支持 **Groovy**、**Aviator**、**Jexl** 三种脚本语言，通过工厂模式实现语言无关的调用。

- **Groovy** — 完整脚本能力，适合复杂条件和逻辑运算。通过 `GroovyFun` 接口 + `@FunctionDef` / `@MethodName`
  注解实现自定义函数扩展（已内置 math/list 函数）
- **Aviator** — 高性能表达式引擎，适合简单数值/逻辑计算。通过继承 `AbstractFunction` 扩展函数（已内置 56 个函数：数学/集合/字符串/系统）
- **Jexl** — 轻量表达式引擎，脚本缓存加速。通过 `JexlFunction` 类扩展函数

详见 [ttd-language/README.md](ttd-language/README.md)

### ttd-nacos-common（基础设施）

- **`NacosConfigLoader`** — Nacos 配置中心客户端，动态拉取和热更新配置
- **`XRedisTemplate`** — Redis 操作模板封装（支持单机/哨兵/集群模式 + Redisson）
- **`KryoSerializer` / `KryoDeserializer`** — Kafka 消息的高性能 Kryo 序列化
- **dynamic-tp** — 动态线程池，支持运行时调参
- **`EnvPropertyUtil`** — 环境变量工具类

### ttd-feature-flink-task（Flink 实时任务）

5 个聚合任务，基于 Flink DataStream API + Kafka Source：

| 任务                 | 功能  | 状态管理       |
|--------------------|-----|------------|
| `FeatureCountTask` | 计数  | ValueState |
| `FeatureSumTask`   | 求和  | ValueState |
| `FeatureAvgTask`   | 平均值 | ValueState |
| `FeatureMaxTask`   | 最大值 | ValueState |
| `FeatureMinTask`   | 最小值 | ValueState |

- **`AbstractTask`** — 任务基类，封装 Kafka Consumer 配置和公共逻辑
- **`DynamicWatermarkGenerator`** — 动态水位线生成器
- **`AppConfig`** — Flink 应用配置类
- **`JsonDeserializationSchema`** — JSON 反序列化

### ttd-feature-sample（开发启动器）

本地开发和测试的示例启动模块，内置 8 个线程池的 `ThreadPoolConfig` 配置。

### biz-feature-admin-srv（管理后台）

Spring Boot 后端服务，提供特征平台的配置管理能力（14 个 Service 模块）。

- 项目管理、用户账户、项目用户关联
- 特征配置 CRUD、因子定义与依赖
- 连接器/数据结构/数据字段管理
- 接入点与参数配置
- 字典码表管理
- 事件追踪、操作审计
- 资源变更日志（`ResourceChgEntity`）
- MyBatis-Plus 代码生成器

### biz-feature-oss-web（前端控制台）

基于 UmiJS + Ant Design Pro 的管理控制台。

- 特征配置可视化
- 因子管理
- 连接器配置
- 字典管理
- 审计日志

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis（Redisson 3.52）
- Nacos Server 3.x
- Kafka（Flink 任务需要）

### 构建

```bash
# 1. 先安装 ttd-parent（父 POM）
cd ~/IdeaProjects/ttd-parent
mvn clean install -DskipTests

# 2. 构建核心引擎
cd ~/IdeaProjects/hxttd_feature/ttd-feature
mvn clean install -DskipTests

# 3. 构建基础设施模块
cd ~/IdeaProjects/hxttd_feature/ttd-nacos-common
mvn clean install -DskipTests

# 4. 构建脚本语言模块
cd ~/IdeaProjects/hxttd_feature/ttd-language
mvn clean install -DskipTests

# 5. 构建管理后台
cd ~/IdeaProjects/hxttd_feature/biz-feature-admin-srv
mvn clean package -DskipTests
```

### 启动顺序

1. MySQL / Redis / Nacos（基础设施）
2. `biz-feature-admin-srv`（管理后台，初始化配置）
3. `ttd-feature-sample`（特征引擎开发启动器）
4. `ttd-feature-flink-task`（Flink 任务，按需部署）
5. `biz-feature-oss-web`（前端控制台）

### 调用示例

```java
// 构建交易参数上下文
TxnParamContext context = new TxnParamContext();
context.setProjectCode("PAYMENT_RISK");
context.setFeatureCodes(Arrays.asList("user_24h_order_count", "user_total_amt"));
context.setTxnParams(Map.of(
    "userId", "U12345",
    "merchantId", "M67890",
    "amount", 100.00
));

// 执行特征计算
ExecuteResult result = featureEngineService.execute(context);

// 读取结果
Object orderCount = result.getFeatureValue("user_24h_order_count");
Object totalAmt = result.getFeatureValue("user_total_amt");
```

## 核心流程

```
请求进入
  │
  ▼
FeatureEngineService.execute(TxnParamContext)
  │
  ├── 解析项目配置、特征列表、因子依赖
  ├── 拓扑排序确定计算顺序
  │
  ▼
FactorService.calculate(factorCode, context)
  │
  ├── VelocityService   ← 流速计算（滑动窗口聚合）
  │   └── VelDataSunkService → Redis/DB 持久化
  │
  ├── ConnectorService  ← 外部数据查询
  │   └── ConnectorEngine → SPI Plugin (JDBC/ES/HBase/HTTP)
  │
  ├── LanguageService   ← 脚本/表达式执行
  │   └── Groovy / Aviator / Jexl
  │
  └── FallbackService   ← 降级策略
  │
  ▼
ExecuteResult（结果 + 耗时 + 状态）
```

## 自定义扩展

### 新增数据源连接器

1. 实现 `Connector` 和 `ConnectorProvider` 接口
2. 在 `META-INF/services/` 中注册 SPI
3. 在 `connector-plugin/` 下新建模块

### 新增脚本语言自定义函数

见 [ttd-language/README.md](ttd-language/README.md) 的「自定义函数」章节。

### 新增 Flink 聚合任务

继承 `AbstractTask`，实现聚合逻辑，注册到 `AppConfig`。

## 相关文档

- [部署说明](DEPLOYMENT.md) — 环境配置、应用部署、Docker Compose 一键部署
- [建表语句](schema.sql) — 19 张表的完整 DDL
- [ttd-language 模块文档](ttd-language/README.md) — 多语言脚本引擎详细说明
