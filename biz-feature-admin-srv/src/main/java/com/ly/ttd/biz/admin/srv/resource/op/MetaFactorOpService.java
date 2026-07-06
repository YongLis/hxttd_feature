package com.ly.ttd.biz.admin.srv.resource.op;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ly.ttd.biz.admin.consts.AuditStatusEnum;
import com.ly.ttd.biz.admin.consts.OperationTypeEnum;
import com.ly.ttd.biz.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.admin.mybatis.entity.FactorEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.FactorMapper;
import com.ly.ttd.biz.admin.mybatis.mapper.FeatureConfigMapper;
import com.ly.ttd.biz.admin.req.BaseRequest;
import com.ly.ttd.biz.admin.srv.audit.req.AuditApproveReq;
import com.ly.ttd.biz.admin.srv.audit.req.AuditReq;
import com.ly.ttd.biz.admin.srv.audit.res.AuditDetail;
import com.ly.ttd.biz.admin.srv.audit.res.MetaFactorAuditDetail;
import com.ly.ttd.biz.admin.srv.dependency.req.DependencyQueryReq;
import com.ly.ttd.biz.admin.srv.dependency.res.DependencyQueryRes;
import com.ly.ttd.biz.admin.srv.factor.req.MetaFactorAddReq;
import com.ly.ttd.biz.admin.srv.factor.req.MetaFactorUpdateReq;
import com.ly.ttd.biz.admin.srv.resource.AbstractResourceOpService;
import com.ly.ttd.biz.admin.srv.resource.req.ResourceChgReq;
import com.ly.ttd.biz.admin.srv.user.LoginUser;
import com.ly.ttd.feature.common.enums.FactorTypeEnum;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.common.model.factor.resource.DerivativeFactorResourceModel;
import com.ly.ttd.feature.common.model.factor.resource.MetaFactorResourceModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * 指标资源操作服务
 *
 * @author yong.li
 * @since 2026/6/23 13:13
 */
@Service
@Slf4j
public class MetaFactorOpService extends AbstractResourceOpService {
    @Resource
    private FactorMapper factorMapper;
    @Resource
    private FeatureConfigMapper featureConfigMapper;

    @Override
    public String getResourceType() {
        return FeatureResourceType.FACTOR_META.getType();
    }

    @Override
    public void add(BaseRequest req) throws FeatureBizException {
        MetaFactorAddReq addReq = (MetaFactorAddReq) req;
        String metaFieldCodePrefix = projectService.getPrefix(addReq.getProjectId(), FeatureResourceType.META_FIELD.getPrefix());
        String tmp = addReq.getMetaFieldCode().replace(metaFieldCodePrefix, "");

        String key = checkAndBuildResourceKey(addReq.getProjectId(), tmp);
        addReq.setResourceKey(key);
        FactorEntity entity = buildMetaFactorEntity(addReq);

        addAudit(new AuditReq(entity.getResourceKey(),
                getResourceType(),
                entity.getResourceName(),
                OperationTypeEnum.ADD.getCode(),
                null,
                JSON.toJSONString(entity)));

    }

    private String checkAndBuildResourceKey(Long projectId, String resourceKey) throws FeatureBizException {
        String key = projectService.getResourceKey(projectId, FeatureResourceType.FACTOR_META.getPrefix(), resourceKey);
        // 检查资源键唯一性
        LambdaQueryWrapper<FactorEntity> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(FactorEntity::getResourceKey, key);
        checkWrapper.eq(FactorEntity::getDeleted, false);
        if (factorMapper.selectCount(checkWrapper) > 0) {
            throw new FeatureBizException("资源键已存在");
        }
        return key;
    }

    private FactorEntity buildMetaFactorEntity(MetaFactorAddReq addReq) {
        FactorEntity entity = new FactorEntity();
        entity.setResourceKey(addReq.getResourceKey());
        entity.setResourceName(addReq.getResourceName());
        entity.setVersion("V" + System.currentTimeMillis());
        entity.setFactorType(FactorTypeEnum.META.getCode());
        entity.setReturnType(addReq.getReturnType());
        entity.setProjectId(addReq.getProjectId());
        entity.setDeleted(false);
        entity.setDefaultValue(addReq.getDefaultValue());
        entity.setExceptionValue(addReq.getExceptionValue());
        entity.setTimeout(addReq.getTimeout());
        MetaFactorResourceModel metaFactorResourceModel = new MetaFactorResourceModel();
        metaFactorResourceModel.setMetaFieldCode(addReq.getMetaFieldCode());
        entity.setResourceJson(JSON.toJSONString(metaFactorResourceModel));
        return entity;
    }


    @Override
    public void update(BaseRequest req) throws FeatureBizException {

        MetaFactorUpdateReq updateReq = (MetaFactorUpdateReq) req;
        FactorEntity entity = buildMetaFactorEntity(updateReq);
        entity.setId(updateReq.getId());
        entity.setUptUser(LoginUser.getLoginUserName());

        FactorEntity old = factorMapper.selectById(entity.getId());
        entity.setCrtUser(old.getCrtUser());
        entity.setCrtTime(old.getCrtTime());
        entity.setDeleted(old.getDeleted());

        String beforeJson = JSON.toJSONString(old);

        entity.setVersion("V" + System.currentTimeMillis());

        addAudit(new AuditReq(entity.getResourceKey(),
                getResourceType(),
                entity.getResourceName(),
                OperationTypeEnum.UPDATE.getCode(),
                beforeJson, JSON.toJSONString(entity)));

    }

    @Override
    @Transactional
    public void submitAudit(AuditApproveReq req) throws FeatureBizException {
        AuditEntity audit = checkAudit(req);
        audit.setAuditStatus(req.getAuditStatus());
        audit.setAuditComment(req.getAuditComment());
        if (req.getAuditStatus().equals(AuditStatusEnum.APPROVED.getCode())) {
            FactorEntity factorEntity = JSON.parseObject(audit.getAfterContent(), FactorEntity.class);

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

            String operator = null;
            boolean isDelete = audit.getOperationType().equals(OperationTypeEnum.DELETE.getCode());
            if (audit.getOperationType().equals(OperationTypeEnum.ADD.getCode())) {
                factorMapper.insert(factorEntity);
                chgReq.setAfterVersion(factorEntity.getVersion());
                operator = factorEntity.getCrtUser();
            } else {
                factorMapper.updateById(factorEntity);
                FactorEntity before = JSON.parseObject(audit.getBeforeContent(), FactorEntity.class);
                chgReq.setBeforeVersion(before.getVersion());
                operator = factorEntity.getUptUser();
            }
            addResourceChg(chgReq);

            if (!isDelete && FactorTypeEnum.META.getCode().equals(factorEntity.getFactorType())
                    && StringUtils.isNoneEmpty(factorEntity.getResourceJson())) {
                MetaFactorResourceModel resourceModel =
                        MetaFactorResourceModel.convertFromJson(factorEntity.getResourceJson());
                addFactorDependency(factorEntity.getProjectId(),
                        audit.getResourceKey(),
                        getResourceType(),
                        Arrays.asList(resourceModel.getMetaFieldCode()),
                        operator);
            }

            if (!isDelete && FactorTypeEnum.DERIVATIVE.getCode().equals(factorEntity.getFactorType())
                    && StringUtils.isNoneEmpty(factorEntity.getResourceJson())) {
                DerivativeFactorResourceModel resourceModel =
                        DerivativeFactorResourceModel.convertFromJson(factorEntity.getResourceJson());
                addFactorDependency(factorEntity.getProjectId(),
                        audit.getResourceKey(),
                        getResourceType(),
                        resourceModel.getFactorCodes(),
                        operator);
            }
        }
        updateAuditStatus(audit);

    }

    @Override
    public void delete(Long id) throws FeatureBizException {
        FactorEntity entity = factorMapper.selectById(id);
        if (entity == null) {
            throw new FeatureBizException("指标不存在");
        }
        String beforeJson = JSON.toJSONString(entity);
        entity.setDeleted(true);
        String afterJson = JSON.toJSONString(entity);
        addAudit(new AuditReq(entity.getResourceKey(), getResourceType(), entity.getResourceName(),
                OperationTypeEnum.DELETE.getCode(), beforeJson, afterJson));
    }

    @Override
    public AuditDetail getDetail(Long id) throws FeatureBizException {
        AuditEntity audit = auditMapper.selectById(id);
        if (audit == null) {
            throw new FeatureBizException("审核记录不存在");
        }
        MetaFactorAuditDetail detail = new MetaFactorAuditDetail();
        detail.setId(audit.getId());
        detail.setResourceType(audit.getResourceType());
        detail.setResourceKey(audit.getResourceKey());
        detail.setResourceName(audit.getResourceName());
        detail.setAuditStatus(audit.getAuditStatus());
        detail.setOperationType(audit.getOperationType());
        detail.setAuditComment(audit.getAuditComment());
        detail.setSubmitUser(audit.getSubmitUser());
        detail.setSubmitTime(audit.getSubmitTime());
        detail.setAuditUser(audit.getAuditUser());
        detail.setAuditTime(audit.getAuditTime());

        if (audit.getBeforeContent() != null) {
            detail.setBefore(MetaFactorAuditDetail.jsonConvert(audit.getBeforeContent()));
        }
        if (audit.getAfterContent() != null) {
            detail.setAfter(MetaFactorAuditDetail.jsonConvert(audit.getAfterContent()));
        }
        return detail;
    }

    @Override
    public DependencyQueryRes queryDependency(DependencyQueryReq req) throws FeatureBizException {
        if (StringUtils.isEmpty(req.getResourceKey())) {
            throw new FeatureBizException("资源键不能为空");
        }
        FactorEntity entity = factorMapper.selectByResourceKey(req.getResourceKey());
        if (entity == null) {
            throw new FeatureBizException("指标不存在");
        }
        return queryUpstreamDependency(req.getProjectId(), req.getResourceKey());
    }
}