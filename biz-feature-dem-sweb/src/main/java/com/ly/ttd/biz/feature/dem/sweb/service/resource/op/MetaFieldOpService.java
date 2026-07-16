package com.ly.ttd.biz.feature.dem.sweb.service.resource.op;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.AuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.MetaFieldAuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.dependency.req.DependencyQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.MetaFieldEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.MetaFieldMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.resource.AbstractResourceOpService;
import com.ly.ttd.feature.admin.api.res.DependencyQueryRes;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
    public AuditDetail getDetail(Long id) throws BizException {
        AuditEntity entity = auditMapper.selectById(id);

        MetaFieldAuditDetail detail = new MetaFieldAuditDetail();
        BeanUtils.copyProperties(entity, detail);

        if (StringUtils.isNotBlank(entity.getBeforeContent())) {
            detail.setBefore(MetaFieldAuditDetail.jsonConvert(entity.getBeforeContent()));
        }
        if (StringUtils.isNotBlank(entity.getAfterContent())) {
            detail.setAfter(MetaFieldAuditDetail.jsonConvert(entity.getAfterContent()));
        }
        return detail;
    }

    @Override
    public DependencyQueryRes queryDependency(DependencyQueryReq req) throws BizException {
        if (StringUtils.isEmpty(req.getResourceKey())) {
            throw new BizException("资源键不能为空");
        }
        MetaFieldEntity fieldEntity = metaFieldMapper.selectByResourceKey(req.getResourceKey());
        if (fieldEntity == null) {
            throw new BizException("元字段不存在");
        }
        return queryUpstreamDependency(req.getProjectId(), req.getResourceKey());
    }
}
