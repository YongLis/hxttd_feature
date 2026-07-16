package com.ly.ttd.biz.feature.admin.srv.resource.op;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ly.ttd.biz.feature.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.feature.admin.mybatis.entity.KafkaTopic;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.KafkaTopicMapper;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditApproveReq;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditReq;
import com.ly.ttd.biz.feature.admin.srv.resource.AbstractResourceOpService;
import com.ly.ttd.biz.feature.admin.srv.resource.req.ResourceChgReq;
import com.ly.ttd.feature.admin.api.consts.AuditStatusEnum;
import com.ly.ttd.feature.admin.api.consts.OperationTypeEnum;
import com.ly.ttd.feature.admin.api.dto.BaseDto;
import com.ly.ttd.feature.admin.api.dto.KafkaTopicDto;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kafka Topic 资源操作服务（接入统一审核）
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
@Slf4j
public class KafkaTopicOpService extends AbstractResourceOpService {

    @Resource
    private KafkaTopicMapper kafkaTopicMapper;

    @Override
    public String getResourceType() {
        return FeatureResourceType.KAFKA_TOPIC.getType();
    }

    @Override
    public void add(BaseDto req) throws FeatureBizException {
        KafkaTopicDto addReq = (KafkaTopicDto) req;

        // 检查名称唯一性
        QueryWrapper<KafkaTopic> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("name", addReq.getName());
        checkWrapper.eq("deleted", false);
        if (kafkaTopicMapper.selectCount(checkWrapper) > 0) {
            throw new FeatureBizException("Topic名称已存在");
        }

        KafkaTopic entity = new KafkaTopic();
        entity.setName(addReq.getName());
        entity.setPartitions(addReq.getPartitions());
        entity.setReplicas(addReq.getReplicas());
        entity.setConsumerGroup(addReq.getConsumerGroup());
        entity.setRemark(addReq.getRemark());
        entity.setAuditReason(addReq.getAuditReason());
        entity.setTopicStatus(addReq.getTopicStatus());
        entity.setDeleted(false);

        addAudit(new AuditReq(entity.getName(),
                getResourceType(),
                entity.getName(),
                OperationTypeEnum.ADD.getCode(),
                null,
                JSON.toJSONString(entity)));
    }

    @Override
    public void update(BaseDto req) throws FeatureBizException {
        KafkaTopicDto updateReq = (KafkaTopicDto) req;

        KafkaTopic entity = kafkaTopicMapper.selectById(updateReq.getId());
        if (entity == null) {
            throw new FeatureBizException("Topic不存在");
        }
        String beforeJson = JSON.toJSONString(entity);

        entity.setName(updateReq.getName());
        entity.setPartitions(updateReq.getPartitions());
        entity.setReplicas(updateReq.getReplicas());
        entity.setConsumerGroup(updateReq.getConsumerGroup());
        entity.setRemark(updateReq.getRemark());
        entity.setAuditReason(updateReq.getAuditReason());
        entity.setTopicStatus(updateReq.getTopicStatus());
        addAudit(new AuditReq(entity.getName(),
                getResourceType(),
                entity.getName(),
                OperationTypeEnum.UPDATE.getCode(),
                beforeJson,
                JSON.toJSONString(entity)));
    }

    @Override
    @Transactional
    public void submitAudit(AuditApproveReq req) throws FeatureBizException {
        AuditEntity audit = checkAudit(req);
        audit.setAuditStatus(req.getAuditStatus());
        audit.setAuditComment(req.getAuditComment());

        if (req.getAuditStatus().equals(AuditStatusEnum.APPROVED.getCode())) {
            KafkaTopic entity = JSON.parseObject(audit.getAfterContent(), KafkaTopic.class);

            ResourceChgReq chgReq = new ResourceChgReq(
                    audit.getResourceKey(),
                    audit.getResourceType(),
                    audit.getOperationType(),
                    null,
                    null,
                    audit.getBeforeContent(),
                    audit.getAfterContent(),
                    audit.getAuditStatus(),
                    audit.getSubmitUser());

            if (audit.getOperationType().equals(OperationTypeEnum.ADD.getCode())) {
                kafkaTopicMapper.insert(entity);
            } else {
                kafkaTopicMapper.updateById(entity);
            }
            addResourceChg(chgReq);
        }
        updateAuditStatus(audit);
    }

    @Override
    public void delete(Long id, String userName) throws FeatureBizException {
        // KafkaTopic主键为String类型，需转换
        String topicId = String.valueOf(id);
        KafkaTopic entity = kafkaTopicMapper.selectById(topicId);
        if (entity == null) {
            throw new FeatureBizException("Topic不存在");
        }
        String beforeJson = JSON.toJSONString(entity);
        entity.setDeleted(true);
        entity.setUptUser(userName);
        String afterJson = JSON.toJSONString(entity);

        addAudit(new AuditReq(entity.getName(),
                getResourceType(),
                entity.getName(),
                OperationTypeEnum.DELETE.getCode(),
                beforeJson,
                afterJson));
    }
}
