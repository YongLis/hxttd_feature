package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.req.KafkaTopicQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.KafkaTopic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Kafka Topic Mapper
 *
 * @author yong.li
 * @since 2026/7/8
 */
@Mapper
public interface KafkaTopicMapper extends BaseMapper<KafkaTopic> {

    IPage<KafkaTopic> pageQuery(Page<KafkaTopic> page, @Param("req") KafkaTopicQueryReq req);

    KafkaTopic selectByTopicName(String topicName);
}
