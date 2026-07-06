# connector-core — 连接器框架（SPI 插件体系）

## 概述

`connector-core` 是基于 Java SPI（ServiceLoader）的多数据源连接器框架，提供统一的接口定义、请求拦截器链、结果缓存、输出映射和服务发现机制。支持
JDBC、Elasticsearch、HBase、HTTP 四种数据源后端。

## 模块结构

```
connector-core/
├── connector-api/                     # 接口定义层（框架核心）
│   └── src/.../connector/
│       ├── api/
│       │   ├── AbstractConnector.java      # 抽象连接器（拦截器链 + Observer 通知）
│       │   ├── interceptor/
│       │   │   └── ConnectorInterceptor     # 请求拦截器 SPI 接口
│       │   └── spi/
│       │       └── ConnectorProvider        # 连接器提供者 SPI 接口（ServiceLoader）
│       ├── engine/
│       │   ├── ConnectorEngine              # 执行引擎（注册/缓存/路由/分发）
│       │   ├── cache/ConnectorCache         # 结果缓存（可配置策略）
│       │   ├── filter/ConnectorFilter       # 前置过滤器
│       │   └── mapper/OutputMapper           # 输出字段映射
│       └── enums/
│           └── ConnectorTypeEnum            # 连接器类型枚举
│
└── connector-plugin/                  # 插件实现层
    ├── connector-jdbc-plugin/         # JDBC 连接器（NamedParameterJdbcTemplate）
    ├── connector-es-plugin/          # Elasticsearch 连接器
    ├── connector-hbase-plugin/       # HBase 连接器
    └── connector-http-plugin/        # HTTP 连接器（Apache HttpClient）
```

## 核心机制

### SPI 服务发现

```
META-INF/services/
  com.ly.ttd.connector.api.spi.ConnectorProvider
    → connector-jdbc-plugin:  JdbcConnectorProvider
    → connector-es-plugin:    EsConnectorProvider
    → connector-hbase-plugin: HbaseConnectorProvider
    → connector-http-plugin:  HttpConnectorProvider
```

运行时由 `ServiceLoader<ConnectorProvider>` 自动发现并注册。

### 执行流程

```
ConnectorEngine.execute(request)
  → ConnectorCache.get(request)          # 1. 检查缓存
  → ConnectorFilter.filter(request)      # 2. 前置过滤
  → ConnectorProvider.getConnector(type)  # 3. 路由到具体连接器
  → AbstractConnector.execute(request)   # 4. 拦截器链执行
    → ConnectorInterceptor.before(request)  # 4a. 请求前拦截
    → doExecute(request)                    # 4b. 实际查询
    → ConnectorInterceptor.after(response)  # 4c. 请求后处理
  → OutputMapper.map(response)          # 5. 输出映射
  → ConnectorObserver.notify(result)    # 6. 观察者通知
```

### 拦截器链

`ConnectorInterceptor` 支持请求前后的处理逻辑：

- 参数校验 / 注入
- 日志记录
- 权限检查
- 请求限流

## 插件列表

| 插件                         | 后端技术                                     | Maven artifact           |
|----------------------------|------------------------------------------|--------------------------|
| **connector-jdbc-plugin**  | `NamedParameterJdbcTemplate` + Druid 连接池 | `connector-jdbc-plugin`  |
| **connector-es-plugin**    | Elasticsearch Java Client 8.1.2          | `connector-es-plugin`    |
| **connector-hbase-plugin** | HBase Client 2.6.4                       | `connector-hbase-plugin` |
| **connector-http-plugin**  | Apache HttpClient 4.5.14                 | `connector-http-plugin`  |

### JDBC 连接器

支持命名参数 SQL 模板：

```sql
SELECT * FROM risk_blacklist WHERE card_no = :cardNo AND merchant_id = :merchantId
```

变量从 `TxnParamContext` 中动态注入，支持前置条件脚本（Groovy）。

### Elasticsearch 连接器

支持 DSL 查询模板、索引名动态计算、结果映射。

### HBase 连接器

支持表名/列族/过滤器配置，rowKey 动态构建。

### HTTP 连接器

支持 REST API 调用、Header 模板、Body 模板（JSON/XML）、SSL 配置。

## 依赖

- `ttd-feature-common` — 配置模型
- `ttd-feature-cfg-jar` — 连接器配置读取

## 扩展新连接器

1. 实现 `ConnectorProvider` 接口
2. 继承 `AbstractConnector<K, V>` 实现 `doExecute()`
3. 在 `META-INF/services/` 中注册 SPI
4. 添加 Maven 依赖到 `connector-plugin` POM
