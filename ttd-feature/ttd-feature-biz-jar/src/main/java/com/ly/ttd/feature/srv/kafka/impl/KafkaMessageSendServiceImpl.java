package com.ly.ttd.feature.srv.kafka.impl;

import com.ly.ttd.feature.srv.kafka.KafkaMessageSendService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

/**
 * @author yong.li
 * @since 2026/6/11 11:30
 */
@Service
@Slf4j
public class KafkaMessageSendServiceImpl implements KafkaMessageSendService {
    @Resource
    private KafkaProducer kafkaProducer;

    @Override
    public void sendMessage(String topic, String key, String message) {
        kafkaProducer.send(new ProducerRecord<>(topic, key, message));
    }
}
