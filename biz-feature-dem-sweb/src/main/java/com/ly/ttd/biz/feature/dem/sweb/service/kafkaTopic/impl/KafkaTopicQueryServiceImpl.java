package com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.KafkaTopicQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.req.KafkaTopicQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.res.KafkaTopicDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.KafkaTopic;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.KafkaTopicMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Kafka Topic 查询服务实现
 *
 * @author yong.li
 * @since 2026/7/8
 */
@Slf4j
@Service
public class KafkaTopicQueryServiceImpl implements KafkaTopicQueryService {

    @Resource
    private KafkaTopicMapper kafkaTopicMapper;

    @Override
    public PageResult<KafkaTopicDetail> pageQuery(KafkaTopicQueryReq req) {
        PageResult<KafkaTopicDetail> result = new PageResult<>();
        Page<KafkaTopic> page = new Page<>(req.getCurrent(), req.getPageSize());
        IPage<KafkaTopic> iPage = kafkaTopicMapper.pageQuery(page, req);
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            result.setData(iPage.getRecords().stream().map(this::entityConvert).collect(Collectors.toList()));
        }
        result.setTotal(iPage.getTotal());
        result.setCurrent(iPage.getCurrent());
        result.setPageSize(iPage.getSize());
        result.setCode("0000");
        return result;
    }

    @Override
    public KafkaTopicDetail getByName(String topicName) {
        KafkaTopic topic = kafkaTopicMapper.selectByTopicName(topicName);
        return entityConvert(topic);
    }

    private KafkaTopicDetail entityConvert(KafkaTopic e) {
        KafkaTopicDetail res = new KafkaTopicDetail();
        res.setId(e.getId());
        res.setName(e.getName());
        res.setPartitions(e.getPartitions());
        res.setReplicas(e.getReplicas());
        res.setConsumerGroup(e.getConsumerGroup());

        res.setRemark(e.getRemark());
        res.setAuditReason(e.getAuditReason());
        res.setCrtUser(e.getCrtUser());
        res.setCrtTime(e.getCrtTime());
        res.setDeleted(e.getDeleted());
        return res;
    }
}
