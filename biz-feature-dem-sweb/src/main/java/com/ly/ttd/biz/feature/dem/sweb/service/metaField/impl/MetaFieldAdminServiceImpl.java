package com.ly.ttd.biz.feature.dem.sweb.service.metaField.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.AuditQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.MetaFieldAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.MetaFieldAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.MetaFieldUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.MetaFieldEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.MetaFieldMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.feature.admin.api.dto.MetaFieldDto;
import com.ly.ttd.feature.admin.api.metaField.MetaFieldService;
import com.ly.ttd.feature.admin.api.project.ProjectService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yong.li
 * @since 2026/7/13 12:21
 */
@Slf4j
@Service
public class MetaFieldAdminServiceImpl implements MetaFieldAdminService {
    @Rpcwired
    private MetaFieldService metaFieldService;
    @Resource
    private AuditQueryService auditQueryService;
    @Rpcwired
    private ProjectService projectService;
    @Resource
    private MetaFieldMapper metaFieldMapper;


    @Override
    public void add(MetaFieldAddReq req) throws BizException {
        String resourceKey = projectService.getResourceKey(req.getProjectId(), FeatureResourceType.META_FIELD.getPrefix(),
                req.getResourceKey());


        MetaFieldDto dto = convertDto(req);
        dto.setResourceKey(resourceKey);

        // 检查资源键唯一性
        QueryWrapper<MetaFieldEntity> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("resource_key", resourceKey);
        checkWrapper.eq("deleted", false);
        if (metaFieldMapper.selectCount(checkWrapper) > 0) {
            throw new BizException("资源键已存在");
        }
        auditQueryService.waitAuditCheck(resourceKey);

        metaFieldService.add(dto);
    }

    private MetaFieldDto convertDto(MetaFieldAddReq req) {
        MetaFieldDto dto = new MetaFieldDto();
        dto.setResourceKey(req.getResourceKey());
        dto.setResourceName(req.getResourceName());
        dto.setVersion(req.getVersion());
        dto.setProjectId(req.getProjectId());
        dto.setLanguage(req.getLanguage());
        dto.setScript(req.getScript());
        dto.setReturnType(req.getReturnType());
        dto.setDefaultValue(req.getDefaultValue());
        dto.setExceptionValue(req.getExceptionValue());
        dto.setCategoryTag(req.getCategoryTag());
        dto.setCrtUser(LoginUser.getLoginUserName());
//        dto.setUptUser();
//        dto.setCrtTime();
//        dto.setUptTime();
        dto.setDeleted(false);
        return dto;
    }

    @Override
    public void update(MetaFieldUpdateReq req) throws BizException {
        MetaFieldDto dto = convertDto(req);
        dto.setId(req.getId());
        dto.setUptUser(LoginUser.getLoginUserName());

        auditQueryService.waitAuditCheck(req.getResourceKey());

        metaFieldService.update(dto);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        metaFieldService.delete(id, opUser);
    }
}
