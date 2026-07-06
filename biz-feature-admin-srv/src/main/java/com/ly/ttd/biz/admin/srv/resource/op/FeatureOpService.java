package com.ly.ttd.biz.admin.srv.resource.op;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ly.ttd.biz.admin.consts.AuditStatusEnum;
import com.ly.ttd.biz.admin.consts.OperationTypeEnum;
import com.ly.ttd.biz.admin.consts.SequenceConst;
import com.ly.ttd.biz.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.admin.mybatis.entity.FeatureConfigEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.FeatureConfigMapper;
import com.ly.ttd.biz.admin.req.BaseRequest;
import com.ly.ttd.biz.admin.srv.audit.req.AuditApproveReq;
import com.ly.ttd.biz.admin.srv.audit.req.AuditReq;
import com.ly.ttd.biz.admin.srv.audit.res.AuditDetail;
import com.ly.ttd.biz.admin.srv.audit.res.FeatureConfigAuditDetail;
import com.ly.ttd.biz.admin.srv.feature.express.FeatureConfigFormService;
import com.ly.ttd.biz.admin.srv.feature.req.FeatureConfigAddReq;
import com.ly.ttd.biz.admin.srv.feature.req.FeatureConfigForm;
import com.ly.ttd.biz.admin.srv.feature.req.FeatureConfigUpdateReq;
import com.ly.ttd.biz.admin.srv.resource.AbstractResourceOpService;
import com.ly.ttd.biz.admin.srv.resource.req.ResourceChgReq;
import com.ly.ttd.biz.admin.srv.sequence.LocalSeqService;
import com.ly.ttd.consts.enums.ScriptType;
import com.ly.ttd.consts.exception.BizException;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.feature.common.model.vel.FeatureConfigModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 特征资源操作服务
 *
 * @author yong.li
 * @since 2026/6/23 13:13
 */
@Service
@Slf4j
public class FeatureOpService extends AbstractResourceOpService {
    @Resource
    private FeatureConfigMapper featureConfigMapper;
    @Resource
    private LocalSeqService localSeqService;
    @Resource
    private FeatureConfigFormService featureConfigFormService;


    @Override
    public String getResourceType() {
        return FeatureResourceType.FEATURE_CONFIG.getType();
    }

    @Override
    public void add(BaseRequest req) throws BizException {
        FeatureConfigAddReq addReq = (FeatureConfigAddReq) req;
        String key = projectService.getResourceKey(addReq.getProjectId(), FeatureResourceType.FEATURE_CONFIG.getPrefix(), addReq.getResourceKey());
        addReq.setResourceKey(key);
        // 检查资源键唯一性
        LambdaQueryWrapper<FeatureConfigEntity> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(FeatureConfigEntity::getResourceKey, addReq.getResourceKey());
        checkWrapper.eq(FeatureConfigEntity::getDeleted, false);
        if (featureConfigMapper.selectCount(checkWrapper) > 0) {
            throw new BizException("资源键已存在");
        }
        String featureCode = getFeatureCode();
        FeatureConfigEntity entity = new FeatureConfigEntity();
        entity.setResourceKey(addReq.getResourceKey());
        entity.setResourceName(addReq.getResourceName());
        entity.setVersion("V" + System.currentTimeMillis());
        entity.setFeatureCode(featureCode);
        entity.setReturnType(addReq.getReturnType());

        entity.setDefaultValue(addReq.getDefaultValue());
        entity.setExceptionValue(addReq.getExceptionValue());
        entity.setTimeout(addReq.getTimeout());
        entity.setMainDimension(addReq.getMainDimension());
        entity.setSlaveDimension(addReq.getSlaveDimension());
        entity.setValueType(addReq.getValueType());
        entity.setFixValue(addReq.getFixValue());
        entity.setAggregateMode(addReq.getAggregateMode());
        entity.setTimeMode(addReq.getTimeMode());
        entity.setTimeUnit(addReq.getTimeUnit());
        entity.setTimeWindow(addReq.getTimeWindow());
        entity.setLanguage(ScriptType.JEXL.getCode());

        FeatureConfigForm form = addReq.buildForm();
        entity.setResourceJson(JSON.toJSONString(form));

        FeatureConfigModel configModel = featureConfigFormService.convertForm(form);
        entity.setConditionScript(configModel.getConditionScript());
        entity.setMainDimScript(configModel.getMainDimScript());
        entity.setSlaveDimScript(configModel.getSlaveDimScript());


        entity.setProjectId(addReq.getProjectId());
        entity.setDeleted(false);

        addAudit(new AuditReq(addReq.getResourceKey(),
                getResourceType(),
                addReq.getResourceName(),
                OperationTypeEnum.ADD.getCode(),
                null,
                JSON.toJSONString(entity)));

    }

    @Override
    public void update(BaseRequest req) throws BizException {
        FeatureConfigUpdateReq updateReq = (FeatureConfigUpdateReq) req;

        FeatureConfigEntity entity = featureConfigMapper.selectById(updateReq.getId());
        if (entity == null) {
            throw new BizException("特征配置不存在");
        }
        String beforeJson = JSON.toJSONString(entity);

        entity.setResourceName(updateReq.getResourceName());
        entity.setFeatureCode(updateReq.getFeatureCode());
        entity.setReturnType(updateReq.getReturnType());
        entity.setDefaultValue(updateReq.getDefaultValue());
        entity.setExceptionValue(updateReq.getExceptionValue());
        entity.setTimeout(updateReq.getTimeout());

        entity.setMainDimension(updateReq.getMainDimension());
        entity.setSlaveDimension(updateReq.getSlaveDimension());
        entity.setValueType(updateReq.getValueType());
        entity.setFixValue(updateReq.getFixValue());
        entity.setAggregateMode(updateReq.getAggregateMode());
        entity.setTimeMode(updateReq.getTimeMode());
        entity.setTimeUnit(updateReq.getTimeUnit());
        entity.setTimeWindow(updateReq.getTimeWindow());
        entity.setVersion("V" + System.currentTimeMillis());

        addAudit(new AuditReq(entity.getResourceKey(),
                getResourceType(),
                updateReq.getResourceName(),
                OperationTypeEnum.UPDATE.getCode(),
                beforeJson, JSON.toJSONString(entity)));

    }

    private String getFeatureCode() {
        String featureCode = localSeqService.generateSeq("V", 0, SequenceConst.VELOCITY_CODE);
        return featureCode;
    }

    @Override
    @Transactional
    public void submitAudit(AuditApproveReq req) throws BizException {
        AuditEntity audit = checkAudit(req);
        audit.setAuditStatus(req.getAuditStatus());
        audit.setAuditComment(req.getAuditComment());
        if (req.getAuditStatus().equals(AuditStatusEnum.APPROVED.getCode())) {
            FeatureConfigEntity featureEntity = JSON.parseObject(audit.getAfterContent(), FeatureConfigEntity.class);

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
                if (StringUtils.isEmpty(featureEntity.getFeatureCode())) {
                    featureEntity.setFeatureCode(getFeatureCode());
                }
                featureConfigMapper.insert(featureEntity);
                chgReq.setAfterVersion(featureEntity.getVersion());
            } else {
                featureConfigMapper.updateById(featureEntity);
                FeatureConfigEntity before = JSON.parseObject(audit.getBeforeContent(), FeatureConfigEntity.class);
                chgReq.setBeforeVersion(before.getVersion());
            }
            addResourceChg(chgReq);
        }
        updateAuditStatus(audit);

    }

    @Override
    public void delete(Long id) throws BizException {
        FeatureConfigEntity entity = featureConfigMapper.selectById(id);
        if (entity == null) {
            throw new BizException("特征配置不存在");
        }
        String beforeJson = JSON.toJSONString(entity);
        entity.setDeleted(true);
        String afterJson = JSON.toJSONString(entity);
        addAudit(new AuditReq(entity.getResourceKey(), getResourceType(), entity.getResourceName(),
                OperationTypeEnum.DELETE.getCode(), beforeJson, afterJson));
    }

    @Override
    public AuditDetail getDetail(Long id) throws BizException {
        AuditEntity entity = auditMapper.selectById(id);

        FeatureConfigAuditDetail detail = new FeatureConfigAuditDetail();
        BeanUtils.copyProperties(entity, detail);

        if (StringUtils.isNotBlank(entity.getBeforeContent())) {
            detail.setBefore(FeatureConfigAuditDetail.jsonConvert(entity.getBeforeContent()));
        }
        if (StringUtils.isNotBlank(entity.getAfterContent())) {
            detail.setAfter(FeatureConfigAuditDetail.jsonConvert(entity.getAfterContent()));
        }
        return detail;
    }
}