package com.ly.ttd.feature.flink.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

/**
 * @author yong.li
 * @since 2026/5/29 15:22
 */
public class JsonDeserializationSchema implements KafkaRecordDeserializationSchema<VelEventData> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> record, Collector<VelEventData> collector) throws IOException {
        String json = new String(record.value());
        VelEventData obj = mapper.readValue(json, VelEventData.class);
        collector.collect(obj);
    }

    @Override
    public TypeInformation<VelEventData> getProducedType() {
        return TypeInformation.of(VelEventData.class);
    }
}
