# 统一特征平台 — 项目介绍

## 项目概述

统一特征平台是一个**企业级特征全生命周期管理平台**，覆盖特征定义、数据源接入、实时计算、审计发布的全链路。平台采用前后端分离架构，后端基于
Spring Boot 3.5 微服务 + Maven 多模块，前端基于 UmiJS 4 + Ant Design Pro，通过 SPI 插件化连接器体系支持 JDBC/ES/HBase/HTTP
等多种外部数据源，并基于 Apache Flink 2.2 实现实时窗口聚合计算。

---

## 核心职责与成果

作为平台的核心开发人员，主导了以下工作：

### 后端微服务架构

- 设计并实现了 **13 个 Maven 模块** 的分层架构（管理后台 / 特征核心 / 连接器框架 / Flink 任务），模块间通过自研 RPC 框架通信
- 构建 **18 个 Controller + 15 个 Service** 的管理后台服务，覆盖 19 张 MySQL 表的 CRUD 与统一审核流程
- 实现 **SPI 插件化连接器框架**，支持 JDBC/ES/HBase/HTTP 四种数据源接入，新增数据源无需改动核心代码

### 实时特征计算引擎

- 设计 **特征计算管道**（TxnFeatureService → 脚本编译 → 因子计算 → Redis 聚合缓存 → Kafka 监控落库），支撑每日百万级事件处理
- 实现 **5 种聚合方法**（SUM / COUNT 去重 / AVG / MIN / MAX）+ **5 种 Redis 累计类型**（TTL/日/月/年/永久）
- 基于 Flink DataStream API + Kafka 开发 **5 个实时聚合任务**，支持事件时间窗口与 txnId 幂等去重

### 全链路审核体系

- 实现 **统一资源审核流程**，覆盖 8 种资源类型（接入点/因子/特征/元字段/连接器等），变更前后内容 JSON 对比 + 可视化差异视图
- 资源操作通过 **ResourceOpFactory** 工厂模式路由，新增资源类型无需修改审核引擎

### 多租户与安全

- 基于 **Spring Security + SessionIdInterceptor** 实现多租户隔离（X-Project-Id
  请求头），统一响应格式 `{code, message, data}`
- 集成 Nacos 配置中心 + 服务发现，支持动态配置刷新与灰度发布

### 前端工程

- 基于 UmiJS 4 + React 19 + Ant Design Pro 构建 **10 个功能模块、45 个页面** 的单页应用
- 封装 **CustomProTable / GroovyEditor / CompareView / LineageGraph** 等公共组件，支撑全平台标准化交互

---

## 技术栈

```
后端：Spring Boot 3.5 / MyBatis-Plus 3.5 / MySQL 8.0 / Redis / Kafka / Flink 2.2
语言：Java 17 / TypeScript / Groovy / Aviator / Jexl
中间件：Nacos / Druid / Redisson / Caffeine / dynamic-tp
前端：UmiJS 4 / React 19 / Ant Design Pro
工具：Maven / Biome / Husky / SpringDoc OpenAPI
架构：SPI 插件化 / 微服务 / RPC / 事件驱动 / 流式计算
```

---

## 架构特性

| 特性        | 实现                                      |
|-----------|-----------------------------------------|
| **插件化扩展** | SPI 连接器 + 工厂模式路由，新增数据源无需改核心代码           |
| **可编程计算** | 元字段脚本、聚合条件、维度路由支持 Groovy/Aviator/Jexl   |
| **全链路审计** | 8 种资源类型统一审核，变更前后差异对比可视化                 |
| **异步高吞吐** | 异步写入管道 + 线程池隔离，调用方非阻塞                   |
| **缓存先行**  | Caffeine 本地缓存 + Redis 分布式缓存双层架构         |
| **幂等去重**  | 基于 txnId 的幂等设计，保证最终一致性                  |
| **实时聚合**  | Flink DataStream + 事件时间窗口 + 5 种聚合语义     |
| **多租户隔离** | 请求头 X-Project-Id + SessionIdInterceptor |

---

## 模块清单

| 模块                     | 路径         | 说明                                           |
|------------------------|------------|----------------------------------------------|
| biz-feature-admin-srv  | 管理后台微服务    | 18 个 Controller / 15 个 Service / 19 个 Mapper |
| ttd-feature-biz-jar    | 特征业务核心     | 特征计算 / 因子 / 连接器调度 / 降级容错                     |
| ttd-feature-engine     | 计算引擎       | 统一入口 API（FeatureEngineService）               |
| connector-core         | 连接器 SPI 框架 | 拦截器链 + 观察者模式                                 |
| connector-plugin       | 连接器插件实现    | JDBC / ES / HBase / HTTP                     |
| ttd-language           | 脚本引擎       | Groovy 5.0 / Aviator 5.3 / Jexl 3.5          |
| ttd-nacos-common       | 基础设施       | Nacos / Redis / Kafka / dynamic-tp           |
| ttd-feature-flink-task | Flink 实时任务 | 5 个 DataStream 聚合任务                          |
| biz-feature-oss-web    | 前端 SPA     | 10 个模块 / 45 个页面 / React 19                   |
