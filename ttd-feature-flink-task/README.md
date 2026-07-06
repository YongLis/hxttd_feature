# ttd-feature-flink-task — Flink 实时聚合任务

## 概述

`ttd-feature-flink-task` 负责 Apache Flink 实时流处理任务，从 Kafka
消费特征事件，按滑动窗口进行聚合计算（COUNT/COUNT_DISTINCT/SUM/AVG/MAX/MIN），将聚合结果写入 Redis 供在线查询。

## 包结构

```
com.ly.ttd.feature.flink/
├── config/
│   └── DynamicWatermarkGenerator    # 动态水位线生成器（根据窗口大小自适应）
├── kafka/
│   ├── JsonDeserializationSchema    # Kafka JSON 反序列化
│   └── KafkaSourceFactory           # Kafka Source 工厂
├── srv/                             # 通用服务
└── task/                            # Flink 任务
    ├── FeatureCountTask             # COUNT / COUNT_DISTINCT 聚合
    ├── FeatureSumTask               # SUM 聚合
    ├── FeatureAvgTask               # AVG 聚合
    ├── FeatureMaxTask               # MAX 聚合
    ├── FeatureMinTask               # MIN 聚合
    └── fun/                         # ProcessFunction（KeyedProcessFunction）
        ├── DedupCountProcessFunction #   去重计数（ValueState 去重集合）
        ├── SumProcessFunction        #   累加（ValueState<BigDecimal>）
        ├── AvgProcessFunction        #   平均值（ValueState<Sum+Count>）
        ├── MaxProcessFunction        #   最大值（ValueState<BigDecimal>）
        └── MinProcessFunction        #   最小值（ValueState<BigDecimal>）
```

## 五大聚合任务

| 任务                 | 聚合类型                   | State 类型                                  | 窗口类型 |
|--------------------|------------------------|-------------------------------------------|------|
| `FeatureCountTask` | COUNT / COUNT_DISTINCT | `ValueState<Set<String>>`（去重）             | 滑动窗口 |
| `FeatureSumTask`   | SUM                    | `ValueState<BigDecimal>`（累加）              | 滑动窗口 |
| `FeatureAvgTask`   | AVG                    | `ValueState<Pair<BigDecimal,Long>>`（和+计数） | 滑动窗口 |
| `FeatureMaxTask`   | MAX                    | `ValueState<BigDecimal>`（当前最大值）           | 滑动窗口 |
| `FeatureMinTask`   | MIN                    | `ValueState<BigDecimal>`（当前最小值）           | 滑动窗口 |

## 数据流

```
Kafka (VelEventData 事件)
  → Flink Source (JsonDeserializationSchema)
    → KeyBy (masterValue + slaveValue + featureCode)
      → Window (滑动窗口: minute/hour/day)
        → ProcessFunction (ValueState 有状态聚合)
          → Redis Sink (写入聚合结果)
```

## 关键技术点

- **动态水位线**：`DynamicWatermarkGenerator` 根据窗口大小自动调整水位线延迟
- **去重计数**：`DedupCountProcessFunction` 使用 `ValueState<Set<String>>` 存储已见过的 txnId
- **状态过期**：通过 TTL 配置自动清理过期状态，防止状态无限增长

## 依赖

- `ttd-feature-common` — VelEventData 等事件模型
- `ttd-feature-biz-jar` — 业务逻辑（VelDataSunkFactory 等）
- `org.apache.flink:flink-streaming-java:2.2.0`
- `org.apache.flink:flink-connector-kafka:4.0.1-2.0`

## 部署

```bash
# 打包
cd ttd-feature-flink-task
mvn clean package -DskipTests

# 提交到 Flink 集群
flink run -c com.ly.ttd.feature.flink.task.FeatureCountTask \
  -p 4 \
  target/ttd-feature-flink-task-1.0.0-SNAPSHOT.jar
```
