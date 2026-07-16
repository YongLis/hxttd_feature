package com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.AuditQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.DataStructAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.req.DataStructAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.req.DataStructUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.DataStructEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.DataStructMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.feature.admin.api.dataStruct.DataStructService;
import com.ly.ttd.feature.admin.api.dto.DataStructDto;
import com.ly.ttd.feature.admin.api.project.ProjectService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 数据集管理服务实现
 *
 * @author yong.li
 * @since 2026-07-10
 */
@Slf4j
@Service
public class DataStructAdminServiceImpl implements DataStructAdminService {

    @Rpcwired
    private DataStructService dataStructService;
    @Resource
    private DataStructMapper dataStructMapper;
    @Resource
    private AuditQueryService auditQueryService;
    @Rpcwired
    private ProjectService projectService;

    @Override
    public void add(DataStructAddReq req) throws BizException {
        String resourceKey = projectService.getResourceKey(req.getProjectId(),
                FeatureResourceType.DATA_STRUCT.getPrefix(), req.getResourceKey());
        req.setResourceKey(resourceKey);

        // 检查资源键唯一性
        QueryWrapper<DataStructEntity> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("resource_key", resourceKey);
        checkWrapper.eq("deleted", false);
        if (dataStructMapper.selectCount(checkWrapper) > 0) {
            throw new BizException("资源键已存在");
        }
        auditQueryService.waitAuditCheck(resourceKey);

        DataStructDto dto = convertDto(req);
        dataStructService.add(dto);
    }

    @Override
    public void update(DataStructUpdateReq req) throws BizException {
        // DataStructUpdateReq 仅包含 id 和 resourceName，需先查询获取 resourceKey
        DataStructEntity entity = dataStructMapper.selectById(req.getId());
        if (null == entity) {
            throw new BizException("数据集不存在");
        }
        auditQueryService.waitAuditCheck(entity.getResourceKey());

        DataStructDto dto = new DataStructDto();
        dto.setId(req.getId());
        dto.setResourceKey(entity.getResourceKey());
        dto.setResourceName(req.getResourceName());
        dto.setVersion("V" + System.currentTimeMillis());
//        dto.setProjectId(entity.getProjectId());
        dto.setUptUser(LoginUser.getLoginUserName());

        dataStructService.update(dto);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        DataStructEntity entity = dataStructMapper.selectById(id);
        if (null == entity) {
            throw new BizException("数据集不存在");
        }
        auditQueryService.waitAuditCheck(entity.getResourceKey());
        dataStructService.delete(id, LoginUser.getLoginUserName());
    }

    private DataStructDto convertDto(DataStructAddReq req) {
        DataStructDto dto = new DataStructDto();
        dto.setResourceKey(req.getResourceKey());
        dto.setResourceName(req.getResourceName());
        dto.setVersion("V" + System.currentTimeMillis());
        dto.setProjectId(req.getProjectId());
        dto.setCrtUser(LoginUser.getLoginUserName());
        dto.setDeleted(false);
        return dto;
    }
}
