# ttd-feature-engine — 特征计算引擎公共 API

## 概述

`ttd-feature-engine` 是特征计算引擎的对外统一入口模块，提供批量查询、单值查询和异步写入等标准化
API。外部系统无需直接依赖 `biz-jar` 内部逻辑。

## 包结构

```
com.ly.ttd.feature.engine/
├── FeatureEngineService.java       # 引擎统一入口接口
├── impl/
│   └── FeatureEngineServiceImpl    # 入口实现（编排调用链）
├── cache/
│   └── CacheRefreshService         # 配置热加载定时刷新
├── config/
│   └── FeatureEngineConfig         # 引擎自动配置类
├── loader/
│   └── ResourceDelayLoadTask       # 配置延迟加载任务
├── req/                            # 请求对象
│   ├── FeatureQueryReq             #   单值查询请求
│   └── FeatureModelQueryReq        #   模型批量查询请求
└── res/                            # 响应对象
    ├── FeatureQueryRes             #   单值查询响应
    └── FeatureModelQueryRes        #   模型批量查询响应
```

## 核心 API

### FeatureEngineService 接口

| 方法                                     | 说明               |
|----------------------------------------|------------------|
| `getSingleValue(factorCode, txnId)`    | 单个指标值同步查询        |
| `getModelValue(dataStructCode, txnId)` | 模型批量查询（并发执行多个字段） |
| `asyncWrite(featureCodes, txnReq)`     | 异步写入 Kafka 事件队列  |

### 调用链

```
FeatureEngineService
  → FactorGetValueService.getValue(factorCode, ctx)
    → MetaFactor      → AccessMetaFieldCalculate (Groovy/Aviator脚本)
    → DerivativeFactor → Aviator 表达式
    → FeatureFactor   → Velocity 窗口聚合
      → VelDataSunkFactory (TTL/DAY/MONTH/YEAR/PERSIST)
        → AbstractVelCalculateMethod (COUNT/SUM/AVG/MAX/MIN)
```

## 配置热加载

`CacheRefreshService` 在后台定时刷新 `FeatureConfiguration` 中的 12 个缓存 Map，确保配置变更秒级生效，无需重启服务。

## 依赖

- `ttd-feature-common` — 数据模型
- `ttd-feature-cfg-jar` — 配置容器
- `ttd-feature-biz-jar` — 业务逻辑实现
