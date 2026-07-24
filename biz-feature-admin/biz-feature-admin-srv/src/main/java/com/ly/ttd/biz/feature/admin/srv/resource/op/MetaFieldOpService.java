package com.ly.ttd.biz.feature.admin.srv.resource.op;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ly.ttd.biz.feature.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.feature.admin.mybatis.entity.MetaFieldEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.MetaFieldMapper;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditApproveReq;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditReq;
import com.ly.ttd.biz.feature.admin.srv.resource.AbstractResourceOpService;
import com.ly.ttd.biz.feature.admin.srv.resource.req.ResourceChgReq;
import com.ly.ttd.feature.admin.api.consts.AuditStatusEnum;
import com.ly.ttd.feature.admin.api.consts.OperationTypeEnum;
import com.ly.ttd.feature.admin.api.dto.BaseDto;
import com.ly.ttd.feature.admin.api.dto.MetaFieldDto;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yong.li
 * @since 2026/6/23 13:13
 */
@Service
@Slf4j
public class MetaFieldOpService extends AbstractResourceOpService {
    @Resource
    private MetaFieldMapper metaFieldMapper;


    @Override
    public String getResourceType() {
        return FeatureResourceType.META_FIELD.getType();
    }

    @Override
    public void add(BaseDto req) throws FeatureBizException {
        MetaFieldDto addReq = (MetaFieldDto) req;
        String key = projectService.getResourceKey(addReq.getProjectId(), FeatureResourceType.META_FIELD.getPrefix(), addReq.getResourceKey());
        addReq.setResourceKey(key);
        // 检查资源键唯一性
        LambdaQueryWrapper<MetaFieldEntity> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(MetaFieldEntity::getResourceKey, addReq.getResourceKey());
        checkWrapper.eq(MetaFieldEntity::getDeleted, false);
        if (metaFieldMapper.selectCount(checkWrapper) > 0) {
            throw new FeatureBizException("资源键已存在");
        }

        MetaFieldEntity entity = new MetaFieldEntity();
        entity.setResourceKey(addReq.getResourceKey());
        entity.setResourceName(addReq.getResourceName());
        entity.setVersion("V" + System.currentTimeMillis());
        entity.setLanguage(addReq.getLanguage());
        entity.setScript(addReq.getScript());
        entity.setReturnType(addReq.getReturnType());
        entity.setDefaultValue(addReq.getDefaultValue());
        entity.setExceptionValue(addReq.getExceptionValue());
        entity.setCategoryTag(addReq.getCategoryTag() != null ? addReq.getCategoryTag() : "public");

        // 从当前上下文获取租户和项目ID
        entity.setProjectId(addReq.getProjectId());
        entity.setDeleted(false);


        addAudit(new AuditReq(addReq.getResourceKey(),
                getResourceType(),
                addReq.getResourceName(),
                OperationTypeEnum.ADD.getCode(),
                null,
                JSON.toJSONString(entity), addReq.getCrtUser()));

    }

    @Override
    public void update(BaseDto req) throws FeatureBizException {
        MetaFieldDto updateReq = (MetaFieldDto) req;

        MetaFieldEntity entity = metaFieldMapper.selectById(updateReq.getId());
        if (entity == null) {
            throw new FeatureBizException("元字段不存在");
        }
        String beforeJson = JSON.toJSONString(entity);

        entity.setResourceName(updateReq.getResourceName());
        entity.setLanguage(updateReq.getLanguage());
        entity.setScript(updateReq.getScript());
        entity.setReturnType(updateReq.getReturnType());

        entity.setVersion("V" + System.currentTimeMillis());
        entity.setDefaultValue(updateReq.getDefaultValue());
        entity.setExceptionValue(updateReq.getExceptionValue());
        entity.setCategoryTag(updateReq.getCategoryTag());
        entity.setUptUser(updateReq.getUptUser());


        addAudit(new AuditReq(entity.getResourceKey(),
                getResourceType(),
                updateReq.getResourceName(),
                OperationTypeEnum.UPDATE.getCode(),
                beforeJson, JSON.toJSONString(entity), updateReq.getUptUser()));

    }

    @Override
    @Transactional
    public void submitAudit(AuditApproveReq req) throws FeatureBizException {
        AuditEntity audit = checkAudit(req);
        audit.setAuditStatus(req.getAuditStatus());
        audit.setAuditComment(req.getAuditComment());
        if (req.getAuditStatus().equals(AuditStatusEnum.APPROVED.getCode())) {
            MetaFieldEntity fieldEntity = JSON.parseObject(audit.getAfterContent(), MetaFieldEntity.class);

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
                metaFieldMapper.insert(fieldEntity);
                chgReq.setAfterVersion(fieldEntity.getVersion());
            } else {
                metaFieldMapper.updateById(fieldEntity);
                MetaFieldEntity before = JSON.parseObject(audit.getBeforeContent(), MetaFieldEntity.class);
                chgReq.setBeforeVersion(before.getVersion());
            }
            addResourceChg(chgReq);
        }
        updateAuditStatus(audit);

    }

    @Override
    public void delete(Long id, String userName) throws FeatureBizException {
        MetaFieldEntity entity = metaFieldMapper.selectById(id);
        if (entity == null) {
            throw new FeatureBizException("元字段不存在");
        }
        String beforeJson = JSON.toJSONString(entity);
        entity.setDeleted(true);
        entity.setUptUser(userName);
        String afterJson = JSON.toJSONString(entity);
        addAudit(new AuditReq(entity.getResourceKey(), getResourceType(), entity.getResourceName(),
                OperationTypeEnum.DELETE.getCode(), beforeJson, afterJson,userName));
    }
}
