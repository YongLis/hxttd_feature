package com.ly.ttd.biz.feature.dem.sweb.service.resource.op;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.AuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.MetaFactorAuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.dependency.req.DependencyQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.FactorEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.FactorMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.FeatureConfigMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.resource.AbstractResourceOpService;
import com.ly.ttd.feature.admin.api.res.DependencyQueryRes;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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
    public AuditDetail getDetail(Long id) throws BizException {
        AuditEntity audit = auditMapper.selectById(id);
        if (audit == null) {
            throw new BizException("审核记录不存在");
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
    public DependencyQueryRes queryDependency(DependencyQueryReq req) throws BizException {
        if (StringUtils.isEmpty(req.getResourceKey())) {
            throw new BizException("资源键不能为空");
        }
        FactorEntity entity = factorMapper.selectByResourceKey(req.getResourceKey());
        if (entity == null) {
            throw new BizException("指标不存在");
        }
        return queryUpstreamDependency(req.getProjectId(), req.getResourceKey());
    }
}