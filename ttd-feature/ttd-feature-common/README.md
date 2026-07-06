# ttd-feature-common — 公共模块

## 概述

`ttd-feature-common` 是特征计算引擎的基础公共模块，提供所有其他模块共享的数据结构、枚举定义、事件对象和交易上下文。不依赖任何业务模块，是整个引擎体系的基石。

## 包结构

```
com.ly.ttd.feature.common/
├── consts/           # 常量定义（TxnConsts 等）
├── ctx/              # 交易上下文（TxnParamContext）
├── enums/            # 枚举（聚合类型、返回类型、时间模式、操作符等）
├── event/            # 事件对象
│   ├── doris/        #   VelEventData（特征事件数据）
│   └── dto/          #   VelValueItem / VelWindowData（窗口数据）
└── model/            # 配置模型
    ├── connector/    #   连接器配置模型
    ├── meta/         #   MetaFieldModel（元字段模型）
    ├── vel/          #   FeatureConfigModel（特征配置模型）
    └── ...           #   AccessPoint / DataStruct / Dict 等
```

## 核心类

| 类名                      | 说明                                                                         |
|-------------------------|----------------------------------------------------------------------------|
| `TxnParamContext`       | 单次请求的变量空间，支持 key-value 缓存计算中间结果                                            |
| `VelEventData`          | 特征事件数据载体（txnId, featureCode, master/slave, velValue 等）                     |
| `VelWindowData`         | 滑动窗口聚合数据（beforeData, afterData, expireTime）                                |
| `VelValueItem`          | 特征值项（txnId, ts, v）                                                         |
| `FeatureConfigModel`    | 特征配置模型（条件脚本、主/从维度脚本、聚合模式、时间窗口等）                                            |
| `MetaFieldModel`        | 元字段模型（脚本、返回类型、超时、默认值、异常值）                                                  |
| `VelocityTimeModeEnum`  | 时间模式枚举（TTL="ttl", CURRENT_DAY="cd", CURRENT_MONTH="cm", CURRENT_YEAR="cy"） |
| `VelocityValueTypeEnum` | 值类型枚举（FIXED, DYNAMIC_VALUE）                                                |

## 依赖

```xml
<!-- 无内部模块依赖，仅依赖基础工具 -->
<dependency>
    <groupId>com.ly.ttd</groupId>
    <artifactId>api</artifactId>
</dependency>
```

## 注意事项

- 模块内所有类均为 POJO/DTO/Enum，不包含业务逻辑
- `TxnParamContext` 内部使用 `ConcurrentHashMap` 保证线程安全
- 与 `biz-feature-admin-srv` 中的 entity 类保持独立，两者关注不同层次的数据模型
