# ttd-feature-biz-jar — 业务逻辑层

## 概述

`ttd-feature-biz-jar` 是特征计算引擎的业务逻辑核心模块，包含特征值获取、指标计算、Velocity 维度聚合、元字段脚本执行、连接器调度、Kafka
消息发送、数据存储以及超时降级等全部业务能力。模块代码量最大（81 个 Java 类），是整个引擎的中枢。

## 包结构

```
com.ly.ttd.feature/
├── consts/                             # 常量（HBase 表名、追踪表枚举）
├── request/                            # 请求对象
├── response/                           # 响应对象（Redis 数据结构、快照模型等）
└── srv/
    ├── connector/                      # 连接器服务层
    │   ├── ConnectorService.java       #   连接器查询接口
    │   ├── impl/ConnectorServiceImpl   #   连接器调度实现
    │   ├── observer/                   #   执行追踪观察者
    │   └── op/                         #   连接器操作（ConnectorOpFactory 策略模式）
    ├── data/                           # 数据持久化服务
    │   ├── FeatureMiddleDataSaveService #  特征中间数据保存
    │   ├── HbaseDataSaveService        #   HBase 数据保存
    │   └── TraceDataSaveService        #   追踪数据保存
    ├── factor/                         # 指标计算服务
    │   ├── FactorGetValueService       #   指标值获取接口
    │   ├── FactorGetValueServiceImpl   #   指标值获取实现（Meta/Derivative/Feature 三种类型）
    │   └── velocity/                   #   Velocity 维度聚合
    │       ├── FactorCalculateService  #     特征计算服务
    │       ├── MetaFieldQueryService   #     元字段查询服务
    │       └── vel/
    │           ├── calculate/          #     聚合计算方法（SUM/COUNT/AVG/MAX/MIN + 工厂）
    │           ├── filter/             #     特征过滤服务（条件脚本 + 维度值计算）
    │           └── sink/               #     数据下沉服务（5种时间模式 + Redis缓存）
    ├── fallback/                       # 超时降级
    │   ├── FallBackExecutor.java       #   CompletableFuture 超时控制
    │   └── ValueConvertor.java         #   返回值类型转换（STRING/LONG/DOUBLE/BOOLEAN/INTEGER）
    ├── kafka/                          # Kafka 消息服务
    │   ├── KafkaMessageSendService     #   消息发送接口
    │   └── impl/                       #   实现（含监控埋点消息）
    ├── meta/                           # 元字段计算
    │   └── AccessMetaFieldCalculate   #   元字段脚本执行 + 批量预加载（CompletableFuture并发）
    ├── TxnFeatureService              # 交易特征服务
    └── vel/                            # Velocity 编译
        ├── compile/                    #   脚本编译上下文（Jexl3Context / 特征配置表达式）
        ├── monitor/                    #   特征监控下沉
        └── sunk/                       #   特征数据写入任务（FeatureTask / FeatureDataSunkTask）
```

## 核心能力

### 1. 指标值获取（FactorGetValueService）

```
getValue(factorCode, ctx)
  ├── MetaFactor        →  AccessMetaFieldCalculate.loadValue() (Groovy/Aviator 脚本)
  ├── DerivativeFactor  →  Aviator 表达式计算
  └── FeatureFactor     →  Velocity 窗口聚合值查询
```

### 2. Velocity 维度聚合

| 组件                                   | 说明                                             |
|--------------------------------------|------------------------------------------------|
| **filter/VelocityFilterServiceImpl** | 条件脚本过滤 + 主/从维度展开（1:1, 1:N, N:1, N:N）           |
| **calculate/Calculate*Method**       | 5 种聚合方法工厂，支持 COUNT/SUM/AVG/MAX/MIN             |
| **sink/VelDataSunk*Service**         | 5 种时间模式（TTL/DAY/MONTH/YEAR/PERSIST），Redis 缓存读取 |

### 3. 超时降级（FallBackExecutor）

```java
FallBackExecutor.getWithTimeout(
    supplier,          // CompletableFuture.supplyAsync()
    timeout,           // 毫秒
    unit,              // TimeUnit.MILLISECONDS
    defaultValue,      // 超时 / null → 默认值
    exceptionValue     // 异常 → 兜底值
)
```

### 4. 元字段批量加载

`AccessMetaFieldCalculate.batchLoadValue()` 按接入点（pointCode）并发预加载所有关联元字段，结果缓存在 `TxnParamContext`
中，避免重复脚本执行。

## 依赖

- `ttd-feature-common` — 数据模型
- `ttd-feature-cfg-jar` — 配置读取
- `connector-api` — 连接器框架
- `ttd-nacos-common` — Redis/Kafka 基础设施
- `language` — 脚本语言服务
- `api` — API 接口定义

## 测试

测试代码在 `src/test/java` 下，使用 JUnit 5 + Mockito。当前已覆盖：

- `CalculateMinMethod` / `CalculateSumMethod` — 聚合计算
- `VelocityFilterServiceImpl` — 特征过滤
- `AccessMetaFieldCalculate` — 元字段加载
- `VelDataSunk*Service` (5个) — 数据下沉
