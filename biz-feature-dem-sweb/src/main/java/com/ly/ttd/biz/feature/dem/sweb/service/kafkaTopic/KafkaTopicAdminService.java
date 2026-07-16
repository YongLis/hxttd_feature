package com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.req.KafkaTopicAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.req.KafkaTopicUpdateReq;

/**
 * Kafka Topic 管理服务
 *
 * @author yong.li
 * @since 2026/7/8
 */
public interface KafkaTopicAdminService {

    /**
     * 添加Topic
     */
    void addTopic(KafkaTopicAddReq req) throws BizException;


    void updateTopic(KafkaTopicUpdateReq req) throws BizException;


    void deleteTopic(Long id) throws BizException;

}
