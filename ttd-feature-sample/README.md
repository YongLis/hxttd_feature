# ttd-feature-sample — 开发调试启动器

## 概述

`ttd-feature-sample` 是特征计算引擎的开发调试启动模块，集成了 Spring Boot、MyBatis-Plus、Nacos
配置中心、动态线程池等全部引擎依赖，提供可直接运行的开发环境。

## 包结构

```
com.ly.ttd.feature.sample/
├── TtdFeatureSampleApplication       # Spring Boot 启动类
├── controller/                       # 测试控制器
├── mybatis/
│   ├── entity/                       # 本地测试实体（与 admin-srv 的 entity 对应）
│   └── mapper/                       # MyBatis-Plus Mapper
└── resource/
    └── FeatureEngineResource         # 引擎资源配置
```

## 启动配置

```properties
server.port=8081
spring.application.name=ttd-feature-sample
ttd.feature.project.id=1

# Nacos 配置中心
ttd.nacos.server-addr=127.0.0.1:8848
ttd.nacos[0].namespace=dev
ttd.nacos[0].group=ttd-feature-sample
ttd.nacos[0].dataId=application.properties

ttd.nacos[1].dataId=kafka.properties
ttd.nacos[2].dataId=redis.properties

# MyBatis-Plus
mybatis-plus.mapper-locations=classpath*:mapper/**/*.xml
mybatis-plus.configuration.map-underscore-to-camel-case=true
```

## 启动方式

```bash
# Maven 插件
cd ttd-feature-sample
mvn spring-boot:run

# IDEA
# 运行 com.ly.ttd.feature.sample.TtdFeatureSampleApplication 主类

# 验证
curl http://localhost:8081/actuator/health
```

## 依赖

- `ttd-feature-engine` — 引擎入口
- `ttd-feature-biz-jar` — 业务逻辑
- `ttd-nacos-common` — 基础设施
- Spring Boot Starter Web — Web 容器
- MyBatis-Plus — 数据库访问
- dynamic-tp — 线程池管理

## 线程池配置

`application.properties` 中定义了 8 个动态线程池，通过 Nacos 热更新参数：

| 线程池                | 核心/最大 | 用途        |
|--------------------|-------|-----------|
| MetaFieldPool      | 4/8   | 元字段脚本执行   |
| MetricPool         | 8/16  | 指标计算      |
| FeatureWritePool   | 4/8   | 特征异步写入    |
| FeaturePool        | 8/16  | 通用特征处理    |
| LogPool            | 2/4   | 日志记录      |
| FactorGetValuePool | 8/16  | 指标值获取     |
| ConnectorPool      | 4/8   | 连接器查询     |
| HbaseClientPool    | 10/20 | HBase 客户端 |

## 说明

此模块仅用于本地开发和集成测试，不作为生产部署的入口。Flink 聚合任务独立打包部署到 Flink 集群。
