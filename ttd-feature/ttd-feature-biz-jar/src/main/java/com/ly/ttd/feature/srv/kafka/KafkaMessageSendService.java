package com.ly.ttd.feature.srv.kafka;

/**
 * kafkax消息服务发送
 *
 * @author yong.li
 * @since 2026/6/11 11:29
 */
public interface KafkaMessageSendService {

    void sendMessage(String topic, String key, String message);
}
