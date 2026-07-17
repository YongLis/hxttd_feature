package com.ly.ttd.biz.feature.dem.sweb.service.feature.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.FeatureConfigEntity;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.FeatureConfigMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.AuditQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.FeatureConfigAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.req.FeatureConfigAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.req.FeatureConfigUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.feature.admin.api.dto.FeatureConfigDto;
import com.ly.ttd.feature.admin.api.feature.FeatureConfigService;
import com.ly.ttd.feature.admin.api.project.ProjectService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 特征配置管理服务实现
 *
 * @author yong.li
 * @since 2026-07-10
 */
@Slf4j
@Service
public class FeatureConfigAdminServiceImpl implements FeatureConfigAdminService {

    @Rpcwired
    private FeatureConfigService featureConfigService;
    @Resource
    private FeatureConfigMapper featureConfigMapper;
    @Resource
    private AuditQueryService auditQueryService;
    @Rpcwired
    private ProjectService projectService;

    @Override
    public void add(FeatureConfigAddReq req) throws BizException {
        String resourceKey = projectService.getResourceKey(req.getProjectId(),
                FeatureResourceType.FEATURE_CONFIG.getPrefix(), req.getResourceKey());
        req.setResourceKey(resourceKey);

        // 检查资源键唯一性;
        if (featureConfigMapper.selectCountByKey(resourceKey) > 0) {
            throw new BizException("资源键已存在");
        }
        auditQueryService.waitAuditCheck(resourceKey);

        FeatureConfigDto dto = convertDto(req);
        featureConfigService.add(dto);
    }

    @Override
    public void update(FeatureConfigUpdateReq req) throws BizException {
        FeatureConfigDto dto = convertDto(req);
        dto.setId(req.getId());
        dto.setUptUser(LoginUser.getLoginUserName());

        auditQueryService.waitAuditCheck(req.getResourceKey());

        featureConfigService.update(dto);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        FeatureConfigEntity entity = featureConfigMapper.selectById(id);
        if (null == entity) {
            throw new BizException("特征配置不存在");
        }
        auditQueryService.waitAuditCheck(entity.getResourceKey());
        featureConfigService.delete(id, LoginUser.getLoginUserName());
    }

    private FeatureConfigDto convertDto(FeatureConfigAddReq req) {
        FeatureConfigDto dto = new FeatureConfigDto();
        dto.setResourceKey(req.getResourceKey());
        dto.setResourceName(req.getResourceName());
        dto.setVersion("V" + System.currentTimeMillis());
        dto.setProjectId(req.getProjectId());
        dto.setFeatureCode(req.getFeatureCode());
        dto.setDefaultValue(req.getDefaultValue());
        dto.setExceptionValue(req.getExceptionValue());
        dto.setTimeout(req.getTimeout());
        dto.setReturnType(req.getReturnType());
        dto.setMainDimension(req.getMainDimension());
        dto.setSlaveDimension(req.getSlaveDimension());
        dto.setValueType(req.getValueType());
        dto.setValueDimension(req.getValueDimension());
        dto.setFixValue(req.getFixValue());
        dto.setAggregateMode(req.getAggregateMode());
        dto.setTimeMode(req.getTimeMode());
        dto.setTimeUnit(req.getTimeUnit());
        dto.setTimeWindow(req.getTimeWindow());
        dto.setConditions(req.getConditions());
        dto.setCrtUser(LoginUser.getLoginUserName());
        dto.setDeleted(false);
        return dto;
    }
}
