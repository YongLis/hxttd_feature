package com.ly.ttd.feature.flink.kafka;

import lombok.Getter;

/**
 * @author yong.li
 * @since 2026/5/29 15:00
 */
@Getter
public class FlinkKafkaConfig {

    private String bootstrapServers;

    public FlinkKafkaConfig(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }
}
