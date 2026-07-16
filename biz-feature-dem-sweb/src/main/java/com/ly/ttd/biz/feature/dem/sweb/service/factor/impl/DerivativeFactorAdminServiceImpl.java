package com.ly.ttd.biz.feature.dem.sweb.service.factor.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.AuditQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.DerivativeFactorAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.DerivativeFactorAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.DerivativeFactorUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.FactorEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.FactorMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.feature.admin.api.dto.DerivativeFactorDto;
import com.ly.ttd.feature.admin.api.factor.DerivativeFactorService;
import com.ly.ttd.feature.admin.api.project.ProjectService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yong.li
 * @since 2026/7/13 15:25
 */
@Service
@Slf4j
public class DerivativeFactorAdminServiceImpl implements DerivativeFactorAdminService {
    @Rpcwired
    private DerivativeFactorService derivativeFactorService;
    @Resource
    private FactorMapper factorMapper;
    @Rpcwired
    private ProjectService projectService;
    @Resource
    private AuditQueryService auditQueryService;

    @Override
    public void add(DerivativeFactorAddReq req) throws BizException {

        String resourceKey = projectService.getResourceKey(req.getProjectId(),
                FeatureResourceType.FACTOR_DERIVATIVE.getPrefix(), req.getResourceKey());
        DerivativeFactorDto dto = convertDto(req);
        dto.setResourceKey(resourceKey);

        // 检查资源键唯一性
        QueryWrapper<FactorEntity> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("resource_key", resourceKey);
        checkWrapper.eq("deleted", false);
        if (factorMapper.selectCount(checkWrapper) > 0) {
            throw new BizException("资源键已存在");
        }

        // add前检查待审核记录
        auditQueryService.waitAuditCheck(resourceKey);

        derivativeFactorService.add(dto);

    }

    private DerivativeFactorDto convertDto(DerivativeFactorAddReq req) {
        DerivativeFactorDto dto = new DerivativeFactorDto();
        dto.setFactorType(req.getFactorType());
        dto.setReturnType(req.getReturnType());
        dto.setFactorCodes(req.getFactorCodes());
        dto.setConnectorType(req.getConnectorType());
        dto.setConnectorCode(req.getConnectorCode());
        dto.setParams(req.getParams());
        dto.setLanguage(req.getLanguage());
        dto.setConditionScript(req.getConditionScript());
        dto.setScript(req.getScript());
//        dto.setId(req.getId());
        dto.setResourceKey(req.getResourceKey());
        dto.setResourceName(req.getResourceName());
        dto.setVersion(req.getVersion());
        dto.setProjectId(req.getProjectId());
        dto.setFactorType(req.getFactorType());
        dto.setReturnType(req.getReturnType());
        dto.setDefaultValue(req.getDefaultValue());
        dto.setExceptionValue(req.getExceptionValue());
        dto.setTimeout(req.getTimeout());
//        dto.setResourceJson(req.getResourceJson());
//        dto.setRefFeatureCode(req.getRefFeatureCode());
        dto.setCrtUser(LoginUser.getLoginUserName());
        dto.setDeleted(false);
        return dto;
    }

    @Override
    public void update(DerivativeFactorUpdateReq req) throws BizException {
        DerivativeFactorDto dto = convertDto(req);
        dto.setId(req.getId());
        dto.setUptUser(LoginUser.getLoginUserName());

        // 更新前检查待审核记录
        auditQueryService.waitAuditCheck(req.getResourceKey());

        derivativeFactorService.update(dto);
    }

    @Override
    public void delete(Long id, String opUser) {
        derivativeFactorService.delete(id, opUser);
    }
}
