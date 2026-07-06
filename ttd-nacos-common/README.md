# ttd-nacos-common — 基础设施公共库

## 概述

`ttd-nacos-common` 是 TTD 特征平台的基础设施公共模块，提供 Nacos 配置中心集成、Redis/Redisson 封装、Kafka 消息序列化、HBase
连接管理以及动态线程池配置。作为通用基础设施层，被所有业务模块依赖。

## 包结构

```
com.ly.ttd.nacos/
├── config/
│   └── NacosConfigLoader              # Nacos 配置加载器（EnvironmentPostProcessor）
├── consts/                            # 常量
├── hbase/
│   └── HbaseConfig                    # HBase 连接配置
├── kafka/
│   ├── KafkaConfig                    # Kafka 生产者/消费者配置
│   ├── client/
│   │   ├── JsonSerializer             # Kafka JSON 序列化器
│   │   └── JsonDeserializer           # Kafka JSON 反序列化器
│   ├── json/                          # JSON 序列化实现
│   └── kryo/
│       ├── KryoSerializer             # Kafka Kryo 高性能序列化器
│       └── KryoDeserializer           # Kafka Kryo 反序列化器
├── redis/
│   ├── XRedisTemplate                 # Redis 操作模板封装
│   └── impl/
│       └── XRedisTemplateImpl         # 基于 Redisson 的实现
├── threadpool/                        # 动态线程池配置（dynamic-tp）
└── util/                              # 工具类
```

## 核心能力

### 1. Nacos 配置加载

`NacosConfigLoader` 实现 `EnvironmentPostProcessor`，在 Spring 容器启动早期自动执行：

```properties
# application.properties 中的配置
ttd.nacos.server-addr=127.0.0.1:8848
ttd.nacos[0].namespace=dev
ttd.nacos[0].group=ttd-feature-sample
ttd.nacos[0].dataId=application.properties
```

**优势**：不依赖 `spring.config.import`，确保 Redis/Kafka Bean 创建时配置已就绪。

### 2. Redis 客户端封装

`XRedisTemplate` 基于 Redisson 封装，提供：

- 基本 KV 操作（get/set/del/exists/expire）
- Hash 操作（hget/hset/hgetAll）
- 分布式锁（RLock）
- 原子操作

### 3. Kafka 序列化

支持两种序列化方式：

| 方式   | 序列化器                                  | 特点            |
|------|---------------------------------------|---------------|
| JSON | `JsonSerializer` / `JsonDeserializer` | 可读性好，适合调试     |
| Kryo | `KryoSerializer` / `KryoDeserializer` | 高性能二进制，适合生产环境 |

### 4. 动态线程池（dynamic-tp）

集成 dromara dynamic-tp，支持：

- 10 个命名线程池的独立配置
- Nacos 配置热更新（core/max pool size, queue capacity）
- 钉钉告警通知（队列使用率/拒绝/超限）
- 线程池监控指标收集

## 依赖

```xml
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-boot-starter</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
</dependency>
<dependency>
    <groupId>org.dromara.dynamictp</groupId>
    <artifactId>dynamic-tp-spring</artifactId>
</dependency>
```

## 使用方

- `ttd-feature-biz-jar` — 使用 Redis、Kafka
- `ttd-feature-sample` — 使用 Nacos 配置、动态线程池
- `biz-feature-admin-srv` — 使用 Nacos 配置
