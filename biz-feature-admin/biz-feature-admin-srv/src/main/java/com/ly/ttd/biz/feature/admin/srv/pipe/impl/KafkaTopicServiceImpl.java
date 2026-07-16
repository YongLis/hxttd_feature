package com.ly.ttd.biz.feature.admin.srv.pipe.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.KafkaTopic;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.KafkaTopicMapper;
import com.ly.ttd.biz.feature.admin.srv.resource.ResourceOpFactory;
import com.ly.ttd.feature.admin.api.dto.KafkaTopicDto;
import com.ly.ttd.feature.admin.api.pipe.KafkaTopicService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Kafka Topic RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class KafkaTopicServiceImpl implements KafkaTopicService {
    @Resource
    private KafkaTopicMapper kafkaTopicMapper;

    @Override
    public String add(KafkaTopicDto dto) throws BizException {
        ResourceOpFactory.getService(FeatureResourceType.KAFKA_TOPIC.getType())
                .add(dto);
        return null;
    }

    @Override
    public void update(KafkaTopicDto dto) throws BizException {
        ResourceOpFactory.getService(FeatureResourceType.KAFKA_TOPIC.getType())
                .update(dto);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        ResourceOpFactory.getService(FeatureResourceType.KAFKA_TOPIC.getType())
                .delete(id, opUser);
    }

    @Override
    public KafkaTopicDto queryById(Long id) throws BizException {
        KafkaTopic entity = kafkaTopicMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        KafkaTopicDto dto = new KafkaTopicDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
