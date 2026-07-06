# hxttd_feature — TTD 统一特征平台

## 1. 项目概述

hxttd_feature 是 **TTD 统一特征平台（Unified Feature Platform）** 的完整实现，定位为面向实时风控场景的企业级特征平台，提供从特征定义、特征计算、特征存储到特征服务的全链路能力。

与业界主流特征平台（Tecton / Feast / Hopsworks）相比，本平台具备以下差异化特点：

| 维度       | Tecton / Feast / Hopsworks | TTD 特征平台                         |
|----------|----------------------------|----------------------------------|
| **核心场景** | ML 训练数据生成 + 在线推理           | **实时风控**（支付欺诈 / 交易风控）            |
| **计算模式** | 离线批处理（Spark）+ 在线 Serving   | **在线实时计算**（Flink 流处理 + 同步脚本执行）   |
| **特征类型** | ML 特征（训练 / 推理）             | **风控指标**（聚合 / 派生 / 元字段 / 决策特征）   |
| **时间窗口** | 训练数据的 Point-in-Time Join   | **滑动时间窗口聚合**（分钟 / 小时 / 天）        |
| **脚本语言** | Python SDK / SQL           | **Groovy / Aviator** 表达式（业务人员可写） |
| **规则编排** | 无内置                        | **Drools 规则引擎 + LiteFlow 编排**    |
| **运维体系** | 配置即代码                      | **运营管理后台**（可视化 CRUD + 审批流程）      |

项目整体分为四大部分：

| 子系统        | 目录                                              | 说明                                               |
|------------|-------------------------------------------------|--------------------------------------------------|
| **运营支撑后台** | `biz-feature-admin-srv` + `biz-feature-oss-web` | 特征/指标/连接器/元字段的 CRUD 管理与审批流程，16 个 REST 模块         |
| **实时计算引擎** | `ttd-feature`                                   | Flink 流处理 + 多数据源连接器（SPI 插件体系）+ 特征计算内核            |
| **特征数据服务** | `ttd-feature-data-srv`                          | 独立轻量级特征值查询服务（对外 API 入口）                          |
| **基础设施层**  | `ttd-nacos-common`                              | 配置中心（Nacos）、缓存（Redis/Redisson）、消息队列（Kafka）、动态线程池 |

---

## 2. 技术栈

### 2.1 计算引擎（ttd-feature）

| 组件                    | 版本           | 用途                   |
|-----------------------|--------------|----------------------|
| Spring Framework      | 6.0.16       | 核心 IoC 容器            |
| Apache Flink          | 2.2.0        | 实时流处理 —— 特征聚合、滑动窗口计算 |
| Kafka Client          | 4.2.0        | 事件消息队列               |
| Flink Kafka Connector | 4.0.1        | Flink ↔ Kafka 集成     |
| Aviator               | 5.3.3        | 轻量表达式引擎              |
| Groovy                | 5.0.4        | 动态脚本执行               |
| Drools                | 9.44.0       | 规则引擎                 |
| LiteFlow              | 2.15.3       | 规则编排 / 流程编排          |
| Redisson              | 3.52.0       | Redis 分布式客户端         |
| Jackson               | 2.21 / 3.1.3 | JSON 序列化             |
| Kryo                  | 5.6.2        | Kafka 消息二进制序列化       |
| Caffeine              | 3.2.3        | 配置本地缓存               |
| Druid                 | 1.2.28       | JDBC 连接池             |
| HBase Client          | 2.6.4        | HBase 数据源后端          |
| Elasticsearch Client  | 8.1.2        | ES 数据源后端             |
| RocketMQ              | 5.3.0        | 备用消息队列               |
| H2                    | 2.3.232      | 测试 / 嵌入式数据库          |
| JDK                   | 17           | Java 语言版本            |

### 2.2 运营后台（biz-feature-admin-srv）

| 组件              | 版本     | 用途             |
|-----------------|--------|----------------|
| Spring Boot     | 3.5.0  | Web 服务框架       |
| Spring Security | —      | 认证与权限控制        |
| MyBatis-Plus    | 3.5.11 | ORM（增删改查 + 分页） |
| MySQL           | 8.0.33 | 持久化存储          |
| Caffeine        | —      | 本地缓存           |
| FastJSON        | 2.0.58 | JSON 序列化       |
| HuTool          | 5.8.44 | 通用工具集          |
| JDK             | 17     | Java 语言版本      |

### 2.3 前端（biz-feature-oss-web）

| 组件                  | 版本     | 用途                        |
|---------------------|--------|---------------------------|
| UmiJS Max           | 4.3.x  | React 企业级框架               |
| Ant Design          | 5.25.x | UI 组件库                    |
| ProComponents       | 2.7.x  | ProTable / ProForm 高级业务组件 |
| React               | 19.x   | 视图层                       |
| CodeMirror 6        | 6.x    | Groovy / SQL 代码编辑器        |
| TypeScript          | 5.x    | 类型安全                      |
| @xyflow/react       | 12.x   | 流程可视化（特征血缘图）              |
| AntV / L7           | 2.x    | 图表 / 地理可视化                |
| jspdf + html2canvas | —      | 接入点文档 PDF 导出              |
| WangEditor          | 5.x    | 富文本编辑器                    |
| Biome               | 2.x    | 代码规范检查                    |
| Jest                | 30.x   | 单元测试                      |

---

## 3. 项目结构

```
hxttd_feature/
│
├── biz-feature-admin-srv/              # 运营支撑后台 —— 后端服务 (Spring Boot 3.5)
│   └── src/main/java/com/ly/ttd/biz/admin/
│       ├── controller/                 # 16 个 REST 控制器（/api/*）
│       ├── mybatis/
│       │   ├── entity/                 # 数据库实体（MyBatis-Plus）
│       │   └── mapper/                 # Mapper 接口 + XML
│       ├── srv/                        # 业务服务层（按模块分包）
│       │   ├── access/                 #   接入点管理
│       │   ├── account/                #   账户管理
│       │   ├── audit/                  #   特征审核（含审批流程）
│       │   ├── connector/              #   连接器管理
│       │   ├── connectorAudit/         #   连接器审核
│       │   ├── dict/                   #   字典管理（Dict → DictCode 双层）
│       │   ├── eventmessage/           #   事件消息 / 特征溯源
│       │   ├── factor/                 #   指标管理 + 血缘查询
│       │   ├── factorAudit/            #   指标审核
│       │   ├── feature/                #   特征配置管理
│       │   ├── metaField/              #   元字段管理
│       │   ├── project/                #   项目管理
│       │   └── user/                   #   用户管理
│       ├── config/                     # Spring Security / CORS / MyBatis 配置
│       └── interceptor/                # 登录拦截器
│
├── biz-feature-oss-web/                # 运营支撑后台 —— 前端应用 (UmiJS + Ant Design)
│   ├── config/
│   │   ├── routes.ts                   # 路由表（11 个页面模块）
│   │   ├── proxy.ts                    # 开发代理 → localhost:7082
│   │   └── config.ts                   # UmiJS 框架配置
│   └── src/
│       ├── pages/                      # 页面组件（按功能模块目录）
│       │   ├── accessPointModel/       #   接入点 CRUD + API 文档 + PDF 导出
│       │   ├── audit/                  #   审核中心（特征 / 指标 / 连接器）
│       │   ├── connector/              #   连接器管理（JDBC/ES/HBase/HTTP）
│       │   ├── eventMessage/           # 流程实例 / 特征溯源
│       │   ├── factor/                 #   指标管理
│       │   ├── featureConfig/          #   实时特征配置（事件过滤 / 窗口配置）
│       │   ├── home/                   #   首页 Dashboard
│       │   ├── metaField/              # 元字段 + 测试用例管理
│       │   ├── system/                 # 系统配置（项目 / 账户 / 字典）
│       │   └── user/                   #   登录页
│       ├── services/srv/               # API 请求层（统一 request 封装）
│       ├── components/                 # 公共组件（CustomProTable / GroovyEditor）
│       └── utils/                      # 工具函数
│
├── ttd-feature/                        # 实时计算引擎（核心内核，Maven 多模块）
│   │
│   ├── ttd-feature-common/             # 公共模型 / 枚举 / DTO / 事件对象
│   │   └── src/.../common/
│   │       ├── model/                  # Factor / Feature / Connector / MetaField / AccessPoint
│   │       ├── enums/                  # 聚合类型 / 返回类型 / 时间单位 / 操作符
│   │       ├── ctx/                    # TxnParamContext（交易上下文）
│   │       └── event/                  # 事件消息 DTO
│   │
│   ├── ttd-feature-cfg-jar/            # 特征配置容器（FeatureConfiguration + 资源加载器）
│   │   └── src/.../cfg/
│   │       ├── FeatureConfiguration.java    # 12 个 ConcurrentHashMap 缓存
│   │       └── Resource*Loader.java         # 各资源类型的加载器
│   │
│   ├── ttd-feature-biz-jar/            # 业务逻辑层（计算 / 降级 / 连接 / Kafka）
│   │   └── src/.../srv/
│   │       ├── factor/                 # FactorGetValue / FactorCalculate / Velocity 计算
│   │       ├── meta/                   # AccessMetaFieldCalculate（脚本执行 + 超时降级）
│   │       ├── fallback/               # FallBackExecutor / ValueConvertor
│   │       ├── connector/              # ConnectorService / ConnectorOpFactory 策略
│   │       ├── kafka/                  # KafkaConsumer / KafkaProducer 封装
│   │       └── vel/                    # Velocity 表达式编译与计算
│   │
│   ├── ttd-feature-engine/             # 特征计算引擎公共 API
│   │   └── src/.../engine/
│   │       ├── FeatureEngineService.java    # 统一查询入口（单值 / 模型 / 异步写入）
│   │       ├── cache/                       # CacheRefreshService（配置热加载）
│   │       └── loader/                      # ResourceDelayLoadTask（延迟加载）
│   │
│   ├── connector-core/                 # 连接器框架（SPI 插件体系）
│   │   ├── connector-api/              #   AbstractConnector + ConnectorProvider SPI
│   │   │   └── engine/                 #   ConnectorEngine（缓存 / 过滤 / 映射）
│   │   └── connector-plugin/
│   │       ├── connector-jdbc-plugin/  #   JDBC 连接器（NamedParameterJdbcTemplate）
│   │       ├── connector-es-plugin/   #   Elasticsearch 连接器
│   │       ├── connector-hbase-plugin/ #  HBase 连接器
│   │       └── connector-http-plugin/  #  HTTP 连接器（Apache HttpClient）
│   │
│   ├── ttd-feature-flink-task/         # Flink 实时聚合任务
│   │   └── src/.../flink/
│   │       ├── task/                   # FeatureCount / Sum / Avg / Max / Min Task
│   │       │   └── fun/               # ProcessFunction（ValueState 有状态聚合）
│   │       ├── kafka/                  # Kafka Source（JsonDeserializationSchema）
│   │       └── config/                 # DynamicWatermarkGenerator（动态水位线）
│   │
│   └── ttd-feature-sample/             # Sample 启动器（集成测试 / 开发调试）
│
├── ttd-feature-data-srv/               # 特征数据查询服务（独立部署，轻量级）
├── ttd-nacos-common/                   # 基础设施公共库
│   └── src/.../
│       ├── config/                     # NacosConfigLoader（EnvironmentPostProcessor）
│       ├── redis/                      # XRedisTemplate + Redisson 封装
│       ├── kafka/                      # KafkaConfig / Json+ Kryo 序列化
│       └── threadpool/                 # 动态线程池管理（dynamic-tp + Nacos）
│
└── PROJECT_README.md                   # 本文档
```

---

## 4. 功能模块

### 4.1 实时特征配置

管理特征定义，配置事件过滤条件、聚合类型和时间窗口。

| 能力   | 说明                                             |
|------|------------------------------------------------|
| 聚合类型 | COUNT / COUNT_DISTINCT / SUM / AVG / MAX / MIN |
| 时间窗口 | MINUTE / HOUR / DAY（滑动窗口）                      |
| 事件过滤 | 条件表达式过滤 Kafka 事件流                              |
| 计算脚本 | 基于 Velocity 表达式模板自动生成                          |

### 4.2 指标管理（Factor）

指标的完整生命周期管理。

| 能力    | 说明                                                              |
|-------|-----------------------------------------------------------------|
| 指标类型  | 元字段指标（MetaFactor）、派生指标（DerivativeFactor）、特征指标（FeatureFactor）    |
| 返回类型  | STRING / LONG / DOUBLE / DECIMAL / BOOLEAN / LIST / DATE / DICT |
| 超时与降级 | 每个指标可配置超时时间、默认值、异常值                                             |
| 血缘关系  | 支持上下游依赖追溯（`factor_dependency` 表）                                |
| 审核流程  | 新增/修改需经过审批（PENDING → APPROVED / REJECTED）                       |

### 4.3 连接器管理（Connector）

基于 SPI 插件的多数据源连接框架。

| 插件            | 后端                           | 配置项                    |
|---------------|------------------------------|------------------------|
| JDBC          | `NamedParameterJdbcTemplate` | SQL 模板、动态变量、前置条件脚本、数据源 |
| Elasticsearch | ES Java Client 8.1.2         | 索引、查询 DSL、映射           |
| HBase         | HBase Client 2.6.4           | 表名、列族、过滤器              |
| HTTP          | Apache HttpClient 4.5.14     | URL、方法、Header、Body 模板  |

核心能力：

- **拦截器链**：`ConnectorInterceptor` SPI，支持请求前后处理
- **结果缓存**：`ConnectorCache` 可配置缓存策略
- **输出映射**：`OutputMapper` 字段转换
- **执行追踪**：`ConnectorTraceSaveObserver` 记录每次查询日志

### 4.4 元字段管理（MetaField）

事件元字段的定义与测试。

| 能力    | 说明                     |
|-------|------------------------|
| 脚本语言  | Groovy / Aviator 表达式   |
| 代码编辑器 | 前端集成 CodeMirror 6 语法高亮 |
| 测试用例  | 支持创建、运行测试用例验证脚本正确性     |
| 接入点绑定 | 元字段归属接入点，批量预加载         |

### 4.5 接入点管理（AccessPoint）

接入点是业务场景的抽象，定义一组字段和操作类型。

| 能力     | 说明                     |
|--------|------------------------|
| 操作类型   | QUERY（查询）/ STORAGE（存储） |
| API 文档 | 自动生成接口文档，支持 PDF 导出     |
| 参数配置   | 动态可编辑表格（Form.List）     |

### 4.6 审核管理（Audit）

所有资源变更需经过审批，确保配置安全。

```
提交 → PENDING → 审批通过 (APPROVED) → 自动写入资源表 + 生成 ResourceChg 变更记录
              → 审批驳回 (REJECTED)  → 记录驳回原因
```

### 4.7 特征溯源

| 能力       | 说明                          |
|----------|-----------------------------|
| 事件消息追踪   | 按 txnId 查询完整调用链             |
| 特征血缘图    | 前端 @xyflow/react 可视化上下游依赖关系 |
| 血缘查询 API | 支持 UP（上游依赖）/ DOWN（下游依赖）方向查询 |

### 4.8 系统配置

| 模块   | 说明                               |
|------|----------------------------------|
| 项目管理 | 多项目隔离（projectId 数据级）             |
| 账户管理 | 用户账户 CRUD                        |
| 字典管理 | DictEntity → DictCodeEntity 双层结构 |

---

## 5. 计算引擎架构

### 5.1 特征计算流程

```
FeatureEngineService（统一入口）
  │
  ├── getSingleValue(factorCode, txnId)     # 单值同步查询
  ├── getModelValue(dataStructCode, txnId)   # 模型批量查询（并发）
  └── asyncWrite(featureCodes, txnReq)       # 异步写入 Kafka
        │
        ▼
  FactorGetValueService.getValue(factorCode, ctx)
        │
        ├── MetaFactor      →  MetaField 脚本执行（Groovy / Aviator）
        ├── DerivativeFactor →  派生表达式计算（Aviator）
        └── FeatureFactor   →  Velocity 窗口聚合值查询
                                  │
                                  ▼
                            Flink 流处理（COUNT / SUM / AVG / MAX / MIN）
```

### 5.2 超时降级机制

```
FallBackExecutor.getWithTimeout(
    supplier,          // () -> scriptLanguageService.execute()
    timeout,           // field.getTimeout() ms
    unit,              // TimeUnit.MILLISECONDS
    defaultValue,      // 超时 / 返回 null 时的默认值
    exceptionValue     // 执行异常时的兜底值
)
```

- 使用 `CompletableFuture.supplyAsync().orTimeout().join()` 实现超时控制
- 返回值类型自动转换（`ValueConvertor.convert(returnType, value)`）
- 结果缓存到 `TxnParamContext`，同一请求内不重复计算

### 5.3 配置热加载

```
ResourceLoader（项目启动时加载）
  → FeatureConfiguration（12 个 ConcurrentHashMap，内存配置中心）
    → CacheRefreshService（定时间隔刷新，秒级生效）
```

### 5.4 Flink 实时聚合

| 任务               | 聚合类型                   | 状态后端              | 窗口类型          |
|------------------|------------------------|-------------------|---------------|
| FeatureCountTask | COUNT / COUNT_DISTINCT | ValueState（去重集合）  | 滑动窗口（分钟/小时/天） |
| FeatureSumTask   | SUM                    | ValueState（累加值）   | 同上            |
| FeatureAvgTask   | AVG                    | ValueState（和+计数）  | 同上            |
| FeatureMaxTask   | MAX                    | ValueState（当前最大值） | 同上            |
| FeatureMinTask   | MIN                    | ValueState（当前最小值） | 同上            |

关键技术点：

- **动态水位线**：`DynamicWatermarkGenerator` 根据窗口大小自动调整
- **去重计数**：`DedupCountProcessFunction` 基于 KeyedProcessFunction + ValueState
- **Kafka Source/Sink**：自定义 `JsonDeserializationSchema` 解析事件

---

## 6. 后端架构

### 6.1 分层结构

```
Controller    →  @RestController + @RequestMapping
    ↓
Service       →  业务接口
    ↓
ServiceImpl   →  @Service + 业务逻辑（extends MyBatis-Plus ServiceImpl）
    ↓
Mapper        →  @Mapper + XML（extends BaseMapper<T>）
```

### 6.2 模块分包规范

```
srv/{moduleName}/
├── {Module}Service.java         # 服务接口
├── impl/
│   └── {Module}ServiceImpl.java # 服务实现
├── req/                         # 请求 DTO（extends PageQuery）
│   ├── {Module}QueryReq.java
│   └── {Module}SaveReq.java
└── res/                         # 响应 DTO
    └── {Module}QueryRes.java
```

### 6.3 API 路径一览

| 模块    | 基础路径                             | 功能                       |
|-------|----------------------------------|--------------------------|
| 特征配置  | `/api/feature-config/*`          | CRUD                     |
| 指标    | `/api/factor/*`                  | CRUD + 血缘查询              |
| 连接器   | `/api/connector/*`               | CRUD（JDBC/ES/HBase/HTTP） |
| 元字段   | `/api/meta-field/*`              | CRUD + 测试用例              |
| 接入点   | `/api/access-point/*`            | CRUD + API 文档 + PDF 导出   |
| 特征审核  | `/api/feature-audit/*`           | 审批流程                     |
| 指标审核  | `/api/factor-audit/*`            | 审批流程                     |
| 连接器审核 | `/api/connector-audit/*`         | 审批流程                     |
| 字典    | `/api/dict/*` `/api/dict-code/*` | 字典主表 + 编码                |
| 项目    | `/api/project/*`                 | 项目管理                     |
| 账户    | `/api/account/*`                 | 账户管理                     |
| 特征溯源  | `/api/feature-trace/*`           | 调用链查询                    |
| 系统配置  | `/api/system/*`                  | 系统参数                     |
| 健康检查  | `/api/health/*`                  | 健康状态                     |

---

## 7. 前端架构

### 7.1 路由结构

基于 UmiJS Max 配置式路由，11 个页面模块：

| 路由                  | 页面   | 说明                  |
|---------------------|------|---------------------|
| `/home/index`       | 首页   | Dashboard           |
| `/user/login`       | 登录   | 用户登录                |
| `/access-point/*`   | 接入点  | CRUD + API 文档 + PDF |
| `/meta-field/*`     | 元字段  | 管理 + 测试用例           |
| `/feature-config/*` | 特征配置 | CRUD + 详情           |
| `/connector/*`      | 连接器  | 类型表单（JDBC/ES/HTTP）  |
| `/factor/*`         | 指标   | 管理 + 血缘             |
| `/event-message/*`  | 溯源   | 流程实例 / 特征追踪         |
| `/audit/*`          | 审核   | 特征 / 指标 / 连接器审批     |
| `/system/*`         | 系统   | 项目 / 账户 / 字典        |
| `/*`                | 404  | 兜底页                 |

### 7.2 公共组件

- **CustomProTable**：统一分页表格（`scroll.x = 1200px`）
- **GroovyEditor / SqlEditor**：CodeMirror 6 编辑器，支持自动补全
- **FlowChart**：基于 @xyflow/react 的特征血缘可视化

---

## 8. 连接器框架（SPI 插件体系）

```
connector-core/
├── connector-api/                     # 接口定义层
│   ├── AbstractConnector<K, V>       #   拦截器链 + Observer 通知
│   ├── ConnectorProvider             #   SPI 接口（ServiceLoader 发现）
│   ├── ConnectorInterceptor          #   请求拦截器 SPI
│   ├── ConnectorObserver             #   执行观察者 SPI
│   ├── ConnectorFilter              #   前置过滤器
│   └── engine/
│       ├── ConnectorEngine           #   执行引擎（注册 / 缓存 / 路由）
│       ├── ConnectorCache            #   结果缓存
│       └── OutputMapper              #   输出字段映射
│
└── connector-plugin/                  # 插件实现层
    ├── connector-jdbc-plugin/        # JDBC（NamedParameterJdbcTemplate）
    ├── connector-es-plugin/         # Elasticsearch
    ├── connector-hbase-plugin/      # HBase
    └── connector-http-plugin/       # HTTP
```

每个插件通过 `META-INF/services/com.ly.ttd.connector.api.spi.ConnectorProvider` 注册，运行时由 `ServiceLoader` 自动发现。

---

## 9. 数据库主要表

| 表名                   | 说明     | 关键字段                                                                |
|----------------------|--------|---------------------------------------------------------------------|
| `feature_config`     | 特征配置   | feature_code, project_id, event_filter, aggregate_type, window_type |
| `factor`             | 指标定义   | resource_key, factor_type, return_type, timeout, default_value      |
| `factor_dependency`  | 指标血缘关系 | parent（下游依赖方）, child（上游被依赖方）                                        |
| `connector`          | 连接器配置  | resource_key, connector_type, resource_json                         |
| `meta_field`         | 元字段定义  | resource_key, language（groovy/aviator）, script, point_code          |
| `access_point`       | 接入点    | code, name, operation_type（QUERY/STORAGE）, status                   |
| `feature_audit`      | 特征审核   | before_content, after_content, status（PENDING/APPROVED/REJECTED）    |
| `factor_audit`       | 指标审核   | 同上                                                                  |
| `connector_audit`    | 连接器审核  | 同上                                                                  |
| `resource_chg`       | 资源变更记录 | resource_type, operation_type, change_content                       |
| `dict` / `dict_code` | 字典     | code, name / dict_id, code, value                                   |
| `project`            | 项目     | code, name                                                          |
| `account`            | 账户     | username, project_id                                                |
| `sequence`           | 序列号    | type, current_value                                                 |

---

## 10. 基础设施

### 10.1 Nacos 配置中心

`NacosConfigLoader`（`EnvironmentPostProcessor`）在容器启动早期通过 SPI 自动执行，从 Nacos 拉取远程配置并注入
Spring `Environment`。

配置格式（在 `application.properties` 中声明）：

```properties
ttd.nacos.server-addr=127.0.0.1:8848
ttd.nacos[0].namespace=dev
ttd.nacos[0].group=ttd-feature-sample
ttd.nacos[0].dataId=application.properties
```

优势：不依赖 `spring.config.import`，全程序化控制加载顺序，确保 Redis/Kafka Bean 创建时配置已就绪。

### 10.2 动态线程池（dynamic-tp）

10 个命名线程池（FeaturePool / MetricPool / ConnectorPool / HbaseClientPool 等），通过 Nacos 动态调整核心参数（core/max pool
size, queue capacity, timeout），DingTalk 告警通知。

### 10.3 消息序列化

Kafka 消息支持两种序列化方式：

- **JSON**（`JsonSerializer / JsonDeserializer`）：可读性好，适合调试
- **Kryo**（`KryoSerializer / KryoDeserializer`）：高性能二进制序列化，适合生产

---

## 11. 快速启动

### 环境要求

| 组件      | 版本          |
|---------|-------------|
| JDK     | ≥ 17        |
| Node.js | ≥ 20.0.0    |
| MySQL   | 8.0.x       |
| Maven   | ≥ 3.8       |
| Nacos   | ≥ 2.x（配置中心） |

### 后端

```bash
cd biz-feature-admin-srv
mvn clean package -DskipTests
java -jar target/biz-feature-admin-srv-1.0.0.jar    # 默认端口 7082
```

### 前端

```bash
cd biz-feature-oss-web
yarn install
yarn dev                          # 开发模式 → http://localhost:8000
yarn build                        # 生产构建
```

### 计算引擎

```bash
cd ttd-feature
mvn clean package -DskipTests
# Flink 任务通过 ttd-feature-sample 启动或提交到 Flink 集群
```

---

## 12. 与业界主流特征平台对比 & 改进建议

### 12.1 特征平台能力成熟度评估

下表对标 Tecton / Feast / Hopsworks / Databricks Feature Store 等主流特征平台，评估当前项目的能力覆盖度：

| 能力域      | 能力项                 | 业界主流做法                                                | 当前状态                                     | 差距分析                                           |
|----------|---------------------|-------------------------------------------------------|------------------------------------------|------------------------------------------------|
| **特征注册** | 特征元数据管理             | 统一 Feature Registry，含 schema、owner、tags、documentation | ✅ 已具备（Factor + FeatureConfig CRUD）       | 缺少标签体系、特征搜索、语义化文档                              |
|          | 特征分组/命名空间           | Feature Group / Feature View 层级组织                     | ⚠️ 仅支持 projectId 级隔离                     | 缺少特征分组（Feature Group），无跨项目复用机制                 |
|          | 特征发现                | 全文搜索、按标签/类型过滤                                         | ❌ 缺失                                     | 无特征目录或搜索 UI                                    |
| **特征计算** | 在线计算                | 低延迟（< 10ms）特征 Serving                                 | ✅ 已具备（Groovy/Aviator 脚本 + Connector 查询）  | 缺少预计算 + 在线存储物化，当前为纯实时计算                        |
|          | 离线计算                | 批量生成训练数据集（Spark / SQL）                                | ❌ 缺失                                     | 无离线特征回填能力，无法为模型训练生成历史数据集                       |
|          | 时间窗口聚合              | 滑动窗口 / 会话窗口                                           | ✅ 已具备（Flink 流处理）                         | 窗口类型可扩展（会话窗口、累计窗口）                             |
| **特征存储** | 在线存储（Online Store）  | Redis / Cassandra / DynamoDB 低延迟 KV                   | ⚠️ 部分                                    | 特征值缓存在本地 ctx，未独立持久化到在线存储层                      |
|          | 离线存储（Offline Store） | Parquet / Delta Lake / Snowflake                      | ❌ 缺失                                     | 无离线特征存储，历史特征值不可追溯                              |
|          | Point-in-Time Join  | 训练数据生成时的时间点精确关联                                       | ❌ 缺失                                     | 无法保证离线训练与在线推理的特征一致性                            |
| **特征质量** | 数据验证                | Great Expectations / schema 校验                        | ⚠️ 基础                                    | 仅有返回类型校验，无数值范围/分布/空值率校验                        |
|          | 数据漂移检测              | Training-Serving Skew / 分布漂移                          | ❌ 缺失                                     | 无 Training-Serving 偏斜检测机制                      |
|          | 特征新鲜度监控             | 特征更新时间 SLA 监控                                         | ❌ 缺失                                     | 无法感知特征数据延迟或过期                                  |
| **特征服务** | REST / gRPC API     | 标准化特征查询接口                                             | ⚠️ 部分                                    | `ttd-feature-data-srv` 仅脚手架，引擎 API 为 Java 原生接口 |
|          | Python SDK          | 数据科学家友好的 Python 接口                                    | ❌ 缺失                                     | 只有 Java API，数据科学家无法直接使用                        |
|          | 批量特征查询              | 一次请求获取多个特征值                                           | ✅ 已具备（`getModelValue`）                   | —                                              |
| **特征治理** | 特征版本管理              | 语义化版本 + 回滚                                            | ⚠️ 基础                                    | 存在 version 字段但无版本对比/回滚能力                       |
|          | 特征血缘                | DAG 可视化依赖追溯                                           | ✅ 已具备（factor_dependency + @xyflow/react） | 可扩展至字段级血缘                                      |
|          | 审批流程                | Git-based PR / 平台审批                                   | ✅ 已具备（Audit 模块）                          | —                                              |
|          | RBAC 权限             | 项目级 / 特征级权限控制                                         | ⚠️ 基础                                    | Spring Security 已引入但权限规则不完善                    |
| **运维监控** | 特征值监控               | Prometheus / Grafana 指标暴露                             | ❌ 缺失                                     | 无特征查询延迟、QPS、错误率等监控                             |
|          | 计算链路追踪              | OpenTelemetry / Jaeger 分布式追踪                          | ⚠️ 基础                                    | 有 txnId traceId 记录，未接入分布式追踪系统                  |
|          | 熔断降级                | Circuit Breaker / 限流                                  | ⚠️ 部分                                    | 有 FallBackExecutor 超时降级，无熔断/限流                 |

> 图例：✅ 已具备 &nbsp;&nbsp; ⚠️ 部分具备 &nbsp;&nbsp; ❌ 缺失

### 12.2 改进路线图建议

#### P0（高优先级 —— 弥补核心能力缺口）

| 改进项                     | 描述                                                      | 预期收益                                          |
|-------------------------|---------------------------------------------------------|-----------------------------------------------|
| **离线特征存储与回填**           | 建设 Offline Store（如 Hive/Delta Lake），支持批量特征回填生成训练数据集     | 打通特征平台到模型训练的链路，是特征平台的核心价值                     |
| **在线存储层（Online Store）** | 引入 Redis Cluster 作为低延迟 KV 在线存储，预计算高频特征写入 Redis，在线查询直接读取 | 将在线查询延迟从 "实时计算" 降至 "直接读取"，SLA 从 100ms → < 5ms |
| **特征监控体系**              | 特征值分布监控、新鲜度监控、Training-Serving Skew 检测                  | 保障线上特征质量，提前发现数据异常                             |
| **特征服务 API 标准化**        | 完善 `ttd-feature-data-srv`，提供 REST/gRPC 标准化特征查询接口        | 解耦业务系统与引擎实现，支持多语言调用方                          |

#### P1（中优先级 —— 提升易用性与规模化）

| 改进项                     | 描述                                      | 预期收益                             |
|-------------------------|-----------------------------------------|----------------------------------|
| **特征目录与搜索**             | 前端增加特征搜索/发现页面，支持按标签、类型、负责人过滤            | 降低特征复用门槛，减少重复开发                  |
| **特征分组（Feature Group）** | 在 projectId 之上增加 FeatureGroup 概念，组织相关特征 | 规模化特征管理（100+ 特征时 projectId 层级不够） |
| **Python SDK**          | 提供 Python 客户端库，封装特征注册/查询 API            | 数据科学家可通过 Notebook 直接使用平台         |
| **特征版本管理增强**            | 版本对比 Diff、历史版本回滚、灰度发布                   | 降低特征变更风险                         |
| **Point-in-Time 正确性**   | 离线训练数据生成时保证时间点精确性                       | 消除 Training-Serving Skew 的系统性方案  |

#### P2（低优先级 —— 精细化运营）

| 改进项                | 描述                             | 预期收益              |
|--------------------|--------------------------------|-------------------|
| **Connector 类型扩展** | 新增 Redis / MongoDB / Kafka 连接器 | 丰富数据源覆盖，减少自定义接入成本 |
| **SQL 特征定义**       | 支持通过 SQL 定义简单特征（无需写 Groovy 脚本） | 降低特征开发门槛          |
| **特征 A/B 测试**      | 支持同一特征的多版本并行上线与效果对比            | 量化特征变更的业务效果       |

### 12.3 工程化改进

| 改进项               | 现状                                                        | 建议                                                    | 优先级 |
|-------------------|-----------------------------------------------------------|-------------------------------------------------------|-----|
| **公共模型收敛**        | `ttd-feature-common` 和 `biz-feature-admin-srv` 各自定义相似模型   | 枚举、常量、基础模型统一收敛到 `ttd-feature-common`                  | 高   |
| **审核模块抽象**        | 三种审核各自独立实现，`handleApproved` 逻辑相似                          | 抽取 `AbstractAuditService<T>` 模板方法                     | 高   |
| **单元测试覆盖**        | 核心 Service 层测试覆盖极低                                        | 补充单元测试（JUnit 5 + Mockito），审核逻辑必须有测试                   | 高   |
| **Jackson 版本统一**  | 同时依赖 `tools.jackson:3.1.3` 和 `com.fasterxml.jackson:2.21` | 统一为 Jackson 2.x 稳定版                                   | 高   |
| **Docker 化**      | 无 Dockerfile / docker-compose                             | 添加 Docker Compose 开发环境（MySQL + Nacos + Redis + Kafka） | 高   |
| **API 文档**        | 无 Swagger/OpenAPI 注解                                      | 引入 SpringDoc OpenAPI，自动生成 API 文档                      | 中   |
| **数据库 Migration** | 手动维护 SQL 脚本                                               | 引入 Flyway 管理数据库版本                                     | 中   |
| **Spring 版本统一**   | admin-srv 用 Boot 3.5.0，引擎用 Framework 6.0.16               | 统一至 Spring Boot 3.5.x                                 | 中   |
| **熔断限流**          | 无 resilience4j 或 Sentinel                                 | 关键外部调用增加熔断重试，API 层增加限流                                | 中   |
| **Flink 状态后端**    | 使用默认内存状态后端                                                | 生产配置 RocksDB 状态后端 + 增量 Checkpoint                     | 中   |
| **安全加固**          | 密码可能明文存储，权限规则不完善                                          | 配置加密 + 完善 RBAC（接口级 + 数据级）                             | 高   |
| **CI/CD**         | 无自动化流水线                                                   | GitHub Actions / Jenkins Pipeline：编译 → 测试 → 代码检查      | 中   |
| **子模块 README**    | 各子模块无独立说明                                                 | 关键模块补充简要 README                                       | 低   |

---

## 13. 附录：特征平台术语说明

| 术语    | 英文              | 本平台定义                             |
|-------|-----------------|-----------------------------------|
| 特征    | Feature         | 用于风控决策的变量，由指标 + 时间窗口聚合得到          |
| 指标    | Factor          | 可计算的基础度量，分为元字段指标/派生指标/特征指标        |
| 元字段   | MetaField       | 事件报文字段，通过 Groovy/Aviator 脚本从请求中提取 |
| 接入点   | AccessPoint     | 业务场景抽象，定义一组字段和操作类型                |
| 连接器   | Connector       | 外部数据源访问适配器（JDBC/ES/HBase/HTTP）    |
| 血缘    | Lineage         | 指标间依赖关系的有向图                       |
| 降级    | Fallback        | 计算超时或异常时返回预设默认值                   |
| 交易上下文 | TxnParamContext | 单次请求的变量空间，缓存计算中间结果                |
