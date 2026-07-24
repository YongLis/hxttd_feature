package com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.KafkaTopic;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.req.KafkaTopicQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.res.KafkaTopicDetail;

import java.util.List;

/**
 * Kafka Topic 查询服务
 *
 * @author yong.li
 * @since 2026/7/8
 */
public interface KafkaTopicQueryService {

    /**
     * 分页查询
     */
    PageResult<KafkaTopicDetail> pageQuery(KafkaTopicQueryReq req);


    KafkaTopicDetail getByName(String topicName);

    List<KafkaTopic> getAvailable();
}
