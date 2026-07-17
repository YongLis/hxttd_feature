package com.ly.ttd.biz.feature.dem.sweb.service.factor.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.FactorMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.AuditQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.MetaFactorAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.MetaFactorAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.MetaFactorUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.feature.admin.api.dto.MetaFactorDto;
import com.ly.ttd.feature.admin.api.factor.MetaFactorService;
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
public class MetaFactorAdminServiceImpl implements MetaFactorAdminService {

    @Rpcwired
    private MetaFactorService metaFactorService;
    @Resource
    private FactorMapper factorMapper;
    @Rpcwired
    private ProjectService projectService;
    @Resource
    private AuditQueryService auditQueryService;

    @Override
    public void add(MetaFactorAddReq req) throws BizException {
        String resourceKey = projectService.getResourceKey(req.getProjectId(),
                FeatureResourceType.FACTOR_META.getPrefix(), req.getResourceKey());
        MetaFactorDto dto = convertDto(req);
        dto.setResourceKey(resourceKey);

        // 检查资源键唯一性
        if (factorMapper.selectCountByKey(resourceKey) > 0) {
            throw new BizException("资源键已存在");
        }

        // add前检查待审核记录
        auditQueryService.waitAuditCheck(resourceKey);

        metaFactorService.add(dto);
    }

    private MetaFactorDto convertDto(MetaFactorAddReq req) {
        MetaFactorDto dto = new MetaFactorDto();
        dto.setFactorType(req.getFactorType());
        dto.setReturnType(req.getReturnType());
        dto.setMetaFieldCode(req.getMetaFieldCode());
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
    public void update(MetaFactorUpdateReq req) throws BizException {
        MetaFactorDto dto = convertDto(req);
        dto.setId(req.getId());
        dto.setUptUser(LoginUser.getLoginUserName());

        // 更新前检查待审核记录
        auditQueryService.waitAuditCheck(req.getResourceKey());

        metaFactorService.update(dto);
    }

    @Override
    public void delete(Long id, String opUser) {
        metaFactorService.delete(id, opUser);
    }
}
