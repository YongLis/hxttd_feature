package com.ly.ttd.feature.flink.task;

import com.ly.ttd.feature.common.consts.FeatureTopic;
import com.ly.ttd.feature.common.enums.VelocityCaluateTypeEnum;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.flink.task.fun.DedupCountProcessFunction;
import com.ly.ttd.feature.flink.task.watermark.DynamicWatermarkGenerator;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Flink 特征聚合任务（动态时间窗口）
 * <p>
 * 从 Kafka 消费特征事件流（VelEventDataDto），按 statedKey 分区后，
 * 根据 calateType 执行不同的聚合运算。
 * <ul>
 *   <li>COUNT：slaveValue 去重计数</li>
 * </ul>
 *
 * @author yong.li
 * @since 2026/5/29 14:49
 */
public class FeatureCountTask extends AbstractTask {
    @Override
    public VelocityCaluateTypeEnum getCaluateTypeEnum() {
        return VelocityCaluateTypeEnum.COUNT;
    }

    @Override
    public void start() throws Exception {
        // ========================================
        // 1. 创建执行环境
        // ========================================
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);
        env.setParallelism(4);
        env.setMaxParallelism(8);


        // ========================================
        // 2. 创建 Kafka 数据源
        // ========================================
        KafkaSource<VelEventData> kafkaSource = KafkaSource.<VelEventData>builder()
                .setBootstrapServers("")
                .setGroupId("feature_count_consumer")
                .setTopics(FeatureTopic.FEATURE_COUNT)
                .setStartingOffsets(OffsetsInitializer.latest())
                .setDeserializer(new JsonDeserializationSchema())
                .build();

        // ========================================
        // 3. 水位线策略：最大乱序 5 秒 + 事件时间戳提取
        // ========================================
        WatermarkStrategy<VelEventData> watermarkStrategy = WatermarkStrategy
                .<VelEventData>forGenerator(ctx -> new DynamicWatermarkGenerator<VelEventData>(appConfig))
                .withTimestampAssigner((event, timestamp) -> event.getTxnTime().getTime());

        DataStreamSource<VelEventData> streamSource = env.fromSource(
                kafkaSource, watermarkStrategy, "kafka-source");

        // ========================================
        // 4. 特征聚合：动态窗口 + 多聚合类型
        //    - keyBy: statedKey（velocityCode + masterValue + 时间窗口标识）
        //    - process: DedupCountProcessFunction
        //      · expireNum + expireType → 窗口时长
        //      · 事件时间定时器 + TTL 延长机制
        // ========================================
        SingleOutputStreamOperator<Long> result = streamSource
                .keyBy(VelEventData::getStatedKey)
                .process(new DedupCountProcessFunction());

        // ========================================
        // 5. 输出结果
        // ========================================
        result.print();

        env.execute("FeatureCountTask");

    }
}