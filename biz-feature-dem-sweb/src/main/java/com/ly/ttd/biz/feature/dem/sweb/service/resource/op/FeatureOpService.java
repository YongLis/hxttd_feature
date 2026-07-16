package com.ly.ttd.biz.feature.dem.sweb.service.resource.op;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.AuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.FeatureConfigAuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.FeatureConfigMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.resource.AbstractResourceOpService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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


    @Override
    public String getResourceType() {
        return FeatureResourceType.FEATURE_CONFIG.getType();
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