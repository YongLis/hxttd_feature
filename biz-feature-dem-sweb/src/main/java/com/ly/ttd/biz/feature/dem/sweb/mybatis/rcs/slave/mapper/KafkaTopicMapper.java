package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.KafkaTopic;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.req.KafkaTopicQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Kafka Topic Mapper
 *
 * @author yong.li
 * @since 2026/7/8
 */
@Mapper
public interface KafkaTopicMapper {

    List<KafkaTopic> pageQuery(Page<KafkaTopic> page, @Param("req") KafkaTopicQueryReq req);

    KafkaTopic selectByTopicName(String topicName);

    /**
     * 根据ID查询
     */
    KafkaTopic selectById(@Param("id") Long id);


    long selectCountByName(String topicName);

    List<KafkaTopic> selectAvailable();
}
