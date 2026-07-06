package com.ly.ttd.feature.flink.task;

import com.ly.ttd.feature.common.consts.FeatureTopic;
import com.ly.ttd.feature.common.enums.VelocityCaluateTypeEnum;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.flink.task.fun.FeatureMaxProcessFunction;
import com.ly.ttd.feature.flink.task.watermark.DynamicWatermarkGenerator;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.math.BigDecimal;

/**
 * Flink 特征最大值任务（动态时间窗口）
 * <p>
 * 从 Kafka 消费特征事件流，按 statedKey 分区后追踪 velValue 最大值。
 *
 * @author yong.li
 * @since 2026/5/30
 */
public class FeatureMaxTask extends AbstractTask {

    @Override
    public VelocityCaluateTypeEnum getCaluateTypeEnum() {
        return VelocityCaluateTypeEnum.MAX;
    }

    @Override
    public void start() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);
        env.setParallelism(4);
        env.setMaxParallelism(8);

        KafkaSource<VelEventData> kafkaSource = KafkaSource.<VelEventData>builder()
                .setBootstrapServers("")
                .setGroupId("feature_max_consumer")
                .setTopics(FeatureTopic.FEATURE_MAX)
                .setStartingOffsets(OffsetsInitializer.latest())
                .setDeserializer(new JsonDeserializationSchema())
                .build();

        WatermarkStrategy<VelEventData> watermarkStrategy = WatermarkStrategy
                .<VelEventData>forGenerator(ctx -> new DynamicWatermarkGenerator<VelEventData>(appConfig))
                .withTimestampAssigner((event, timestamp) -> event.getTxnTime().getTime());

        DataStreamSource<VelEventData> streamSource = env.fromSource(
                kafkaSource, watermarkStrategy, "kafka-source");

        SingleOutputStreamOperator<BigDecimal> result = streamSource
                .keyBy(VelEventData::getStatedKey)
                .process(new FeatureMaxProcessFunction());

        result.print();
        env.execute("FeatureMaxTask");
    }
}
