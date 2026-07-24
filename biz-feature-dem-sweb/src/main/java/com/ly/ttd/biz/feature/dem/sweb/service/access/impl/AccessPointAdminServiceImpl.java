package com.ly.ttd.biz.feature.dem.sweb.service.access.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.AccessPointEntity;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.AccessPointMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.access.AccessPointAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.access.req.AccessPointAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.access.req.AccessPointUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.AuditQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.sequence.SequenceQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.feature.admin.api.access.AccessPointService;
import com.ly.ttd.feature.admin.api.dto.AccessPointDto;
import com.ly.ttd.feature.admin.api.dto.AccessPointParamDto;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yong.li
 * @since 2026/7/13 11:36
 */
@Service
@Slf4j
public class AccessPointAdminServiceImpl implements AccessPointAdminService {
    @Rpcwired
    private AccessPointService accessPointService;
    @Resource
    private SequenceQueryService sequenceQueryService;
    @Resource
    private AccessPointMapper accessPointMapper;
    @Resource
    private AuditQueryService auditQueryService;


    @Override
    public void add(AccessPointAddReq req) throws BizException {
        req.setCode(sequenceQueryService.getNextPointCode());
        AccessPointDto dto = convertDto(req);

        // 检查资源键唯一性
        if (accessPointMapper.selectCountByKey(req.getCode()) > 0) {
            throw new BizException("资源键已存在");
        }
        accessPointService.add(dto);
    }


    @Override
    public void update(AccessPointUpdateReq req) throws BizException {
        AccessPointDto dto = convertDto(req);
        dto.setId(req.getId());
        dto.setUptUser(LoginUser.getLoginUserName());

        // 更新前检查待审核记录
        auditQueryService.waitAuditCheck(req.getCode());

        accessPointService.update(dto);
    }

    private AccessPointDto convertDto(AccessPointAddReq req) {
        AccessPointDto dto = new AccessPointDto();
        dto.setCode(req.getCode());
        dto.setName(req.getName());
        dto.setVersion("V" + System.currentTimeMillis());
        dto.setRemark(req.getDescription());
        dto.setProjectId(req.getProjectId());
//        dto.setApiJson();

        List<AccessPointParamDto> paramItems = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(req.getParams())) {
            paramItems.addAll(req.getParams().stream().map(t -> {
                AccessPointParamDto paramDto = new AccessPointParamDto();
                paramDto.setAccessPointCode(dto.getCode());
                paramDto.setParamName(t.getParamName());
                paramDto.setVersion(dto.getVersion());
                paramDto.setParamCode(t.getParamCode());
                paramDto.setParamType(t.getParamType());
                paramDto.setRequired(t.getRequired());
                paramDto.setDefaultValue(t.getDefaultValue());
                paramDto.setDescription(t.getDescription());
                paramDto.setSortOrder(t.getSortOrder());
                paramDto.setParentParamCode(t.getParentParamCode());
                paramDto.setParamLevel(t.getParamLevel());
                paramDto.setCrtUser(dto.getCrtUser());
                paramDto.setUptUser(dto.getUptUser());
                paramDto.setDeleted(false);
                return paramDto;
            }).toList());
        }
        dto.setParamItems(paramItems);
        dto.setCrtUser(LoginUser.getLoginUserName());
        dto.setDeleted(false);
        return dto;
    }


    @Override
    public void delete(Long id, String opUser) throws BizException {
        AccessPointEntity entity = accessPointMapper.selectById(id);
        if (null == entity) {
            throw new BizException("接入点不存在");
        }
        auditQueryService.waitAuditCheck(entity.getCode());
        accessPointService.delete(id, LoginUser.getLoginUserName());
    }
}
