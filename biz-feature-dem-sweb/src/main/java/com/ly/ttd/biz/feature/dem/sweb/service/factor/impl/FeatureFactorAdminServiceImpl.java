package com.ly.ttd.biz.feature.dem.sweb.service.factor.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.AuditQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.FeatureFactorAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.FeatureFactorAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.FeatureFactorUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.FactorEntity;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.FactorMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.feature.admin.api.dto.FactorDto;
import com.ly.ttd.feature.admin.api.factor.FeatureFactorService;
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
public class FeatureFactorAdminServiceImpl implements FeatureFactorAdminService {

    @Rpcwired
    private FeatureFactorService featureFactorService;
    @Resource
    private FactorMapper factorMapper;
    @Rpcwired
    private ProjectService projectService;
    @Resource
    private AuditQueryService auditQueryService;

    @Override
    public void add(FeatureFactorAddReq req) throws BizException {
        String resourceKey = projectService.getResourceKey(req.getProjectId(),
                FeatureResourceType.FACTOR_FEATURE.getPrefix(), req.getResourceKey());
        FactorDto dto = convertDto(req);
        dto.setResourceKey(resourceKey);

        // 检查资源键唯一性
        if (factorMapper.selectCountByKey(resourceKey) > 0) {
            throw new BizException("资源键已存在");
        }

        // add前检查待审核记录
        auditQueryService.waitAuditCheck(resourceKey);

        featureFactorService.add(dto);
    }

    private FactorDto convertDto(FeatureFactorAddReq req) {
        FactorDto dto = new FactorDto();
        dto.setFactorType(req.getFactorType());
        dto.setReturnType(req.getReturnType());
        dto.setRefFeatureCode(req.getFeatureCode());
        dto.setResourceKey(req.getResourceKey());
        dto.setResourceName(req.getResourceName());
        dto.setVersion(req.getVersion());
        dto.setProjectId(req.getProjectId());
        dto.setDefaultValue(req.getDefaultValue());
        dto.setExceptionValue(req.getExceptionValue());
        dto.setTimeout(req.getTimeout());
        dto.setCrtUser(LoginUser.getLoginUserName());
        dto.setDeleted(false);
        return dto;
    }

    @Override
    public void update(FeatureFactorUpdateReq req) throws BizException {
        FactorDto dto = convertDto(req);
        dto.setId(req.getId());
        dto.setUptUser(LoginUser.getLoginUserName());

        // 更新前检查待审核记录
        auditQueryService.waitAuditCheck(req.getResourceKey());

        featureFactorService.update(dto);
    }

    @Override
    public void delete(Long id, String opUser) {
        featureFactorService.delete(id, opUser);
    }
}
