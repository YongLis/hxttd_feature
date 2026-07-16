package com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.AuditQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.KafkaTopicAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.req.KafkaTopicAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.req.KafkaTopicUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.KafkaTopic;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.KafkaTopicMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.project.ProjectAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.feature.admin.api.dto.KafkaTopicDto;
import com.ly.ttd.feature.admin.api.pipe.KafkaTopicService;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Kafka Topic 管理服务实现
 *
 * @author yong.li
 * @since 2026/7/8
 */
@Slf4j
@Service
public class KafkaTopicAdminServiceImpl implements KafkaTopicAdminService {
    @Rpcwired
    private KafkaTopicService kafkaTopicService;
    @Resource
    private ProjectAdminService projectAdminService;
    @Resource
    private AuditQueryService auditQueryService;
    @Resource
    private KafkaTopicMapper kafkaTopicMapper;

    @Override
    public void addTopic(KafkaTopicAddReq req) throws BizException {
        KafkaTopicDto dto = convertDto(req);

        // 检查资源键唯一性
        QueryWrapper<KafkaTopic> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("name", req.getName());
        checkWrapper.eq("deleted", false);
        if (kafkaTopicMapper.selectCount(checkWrapper) > 0) {
            throw new BizException("topic已存在");
        }
        auditQueryService.waitAuditCheck(req.getName());
        kafkaTopicService.add(dto);
    }

    private KafkaTopicDto convertDto(KafkaTopicAddReq req) {
        KafkaTopicDto dto = new KafkaTopicDto();
        dto.setName(req.getName());
        dto.setPartitions(req.getPartitions());
        dto.setReplicas(req.getReplicas());
        dto.setConsumerGroup(req.getConsumerGroup());
        dto.setTopicStatus(req.getTopicStatus());
        dto.setRemark(req.getRemark());
//        dto.setAuditReason();
        dto.setCrtUser(LoginUser.getLoginUserName());
//        dto.setUptUser();
//        dto.setCrtTime();
//        dto.setUptTime();
        dto.setDeleted(false);
        return dto;
    }

    @Override
    public void updateTopic(KafkaTopicUpdateReq req) throws BizException {
        KafkaTopicDto dto = convertDto(req);
        dto.setId(req.getId());
        auditQueryService.waitAuditCheck(req.getName());
        kafkaTopicService.add(dto);
    }

    @Override
    public void deleteTopic(Long id) throws BizException {
        kafkaTopicService.delete(id, LoginUser.getLoginUserName());
    }
}
