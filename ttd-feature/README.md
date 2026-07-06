# ttd-feature — 实时特征计算引擎

## 概述

`ttd-feature` 是 TTD 统一特征平台的核心计算引擎，采用 Maven 多模块架构，基于 Apache Flink 实现实时流处理与特征聚合，支持多数据源连接器（SPI
插件体系）。

## 模块列表

| 模块                         | 说明                                   | 代码量   |
|----------------------------|--------------------------------------|-------|
| **ttd-feature-common**     | 公共模型 / 枚举 / DTO / 事件对象               | 59 个类 |
| **ttd-feature-cfg-jar**    | 特征配置容器（FeatureConfiguration + 资源加载器） | 15 个类 |
| **ttd-feature-biz-jar**    | 业务逻辑层（计算 / 降级 / 连接 / Kafka）          | 81 个类 |
| **ttd-feature-engine**     | 特征计算引擎公共 API（对外统一入口）                 | 13 个类 |
| **ttd-feature-flink-task** | Flink 实时聚合任务（COUNT/SUM/AVG/MAX/MIN）  | 16 个类 |
| **connector-core**         | 连接器框架（SPI 插件体系），包含 API 和插件实现         | —     |

## 技术栈

| 组件               | 版本     | 用途           |
|------------------|--------|--------------|
| Spring Framework | 6.0.16 | IoC 容器       |
| Apache Flink     | 2.2.0  | 实时流处理框架      |
| Kafka Client     | 4.2.0  | 事件消息队列       |
| Groovy           | 5.0.4  | 动态脚本执行       |
| Aviator          | 5.3.3  | 表达式引擎        |
| Drools           | 9.44.0 | 规则引擎         |
| LiteFlow         | 2.15.3 | 规则编排         |
| Redisson         | 3.52.0 | Redis 分布式客户端 |
| JDK              | 17     | 编译与运行        |

## 模块依赖关系

```
ttd-feature (parent)
  ├── ttd-feature-common           ← 所有模块的基础依赖
  ├── ttd-feature-cfg-jar          ← 依赖 common
  ├── ttd-feature-biz-jar           ← 依赖 common + cfg-jar + connector-api
  ├── ttd-feature-engine            ← 依赖 common + cfg-jar + biz-jar
  ├── ttd-feature-flink-task        ← 依赖 common + biz-jar
  └── connector-core
        ├── connector-api           ← 连接器框架接口（SPI + 引擎）
        └── connector-plugin
              ├── connector-jdbc-plugin   ← JDBC 数据源（MySQL/Druid）
              ├── connector-es-plugin     ← Elasticsearch 数据源
              ├── connector-hbase-plugin  ← HBase 数据源
              └── connector-http-plugin   ← HTTP 数据源
```

## 构建

```bash
cd ttd-feature
mvn clean install -DskipTests
```

## 各模块详细说明

- [ttd-feature-common](ttd-feature-common/README.md) — 公共数据结构
- [ttd-feature-cfg-jar](ttd-feature-cfg-jar/README.md) — 配置容器
- [ttd-feature-biz-jar](ttd-feature-biz-jar/README.md) — 业务服务层
- [ttd-feature-engine](ttd-feature-engine/README.md) — 引擎入口
- [ttd-feature-flink-task](ttd-feature-flink-task/README.md) — Flink 聚合任务
- [connector-core](connector-core/README.md) — 连接器框架
