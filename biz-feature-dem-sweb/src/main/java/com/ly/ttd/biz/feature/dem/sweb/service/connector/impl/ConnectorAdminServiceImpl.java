package com.ly.ttd.biz.feature.dem.sweb.service.connector.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.AuditQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.connector.ConnectorAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.connector.req.*;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.ConnectorEntity;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.ConnectorMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.feature.admin.api.connector.EsConnectorService;
import com.ly.ttd.feature.admin.api.connector.HttpConnectorService;
import com.ly.ttd.feature.admin.api.connector.JdbcConnectorService;
import com.ly.ttd.feature.admin.api.dto.EsConnectorDto;
import com.ly.ttd.feature.admin.api.dto.HttpConnectorDto;
import com.ly.ttd.feature.admin.api.dto.JdbcConnectorDto;
import com.ly.ttd.feature.admin.api.project.ProjectService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 连接器管理服务实现
 *
 * @author yong.li
 * @since 2026-07-10
 */
@Slf4j
@Service
public class ConnectorAdminServiceImpl implements ConnectorAdminService {

    @Rpcwired
    private JdbcConnectorService jdbcConnectorService;
    @Rpcwired
    private HttpConnectorService httpConnectorService;

    @Rpcwired
    private EsConnectorService esConnectorService;

    @Resource
    private ConnectorMapper connectorMapper;
    @Resource
    private AuditQueryService auditQueryService;
    @Rpcwired
    private ProjectService projectService;

    @Override
    public void addJdbc(JdbcConnectorAddReq req) throws BizException {
        String resourceKey = projectService.getResourceKey(req.getProjectId(),
                FeatureResourceType.CONNECTOR_JDBC.getPrefix(), req.getResourceKey());
        req.setResourceKey(resourceKey);

        if (connectorMapper.selectCountByKey(resourceKey) > 0) {
            throw new BizException("资源键已存在");
        }
        auditQueryService.waitAuditCheck(resourceKey);

        JdbcConnectorDto dto = convertJdbcDto(req);
        jdbcConnectorService.add(dto);
    }

    @Override
    public void updateJdbc(JdbcConnectorUpdateReq req) throws BizException {
        JdbcConnectorDto dto = convertJdbcDto(req);
        dto.setId(req.getId());
        dto.setUptUser(LoginUser.getLoginUserName());

        auditQueryService.waitAuditCheck(req.getResourceKey());

        jdbcConnectorService.update(dto);
    }

    @Override
    public void addHttp(HttpConnectorAddReq req) throws BizException {
        String resourceKey = projectService.getResourceKey(req.getProjectId(),
                FeatureResourceType.CONNECTOR_HTTP.getPrefix(), req.getResourceKey());
        req.setResourceKey(resourceKey);

        // 检查资源键唯一性
        if (connectorMapper.selectCountByKey(resourceKey) > 0) {
            throw new BizException("资源键已存在");
        }
        auditQueryService.waitAuditCheck(resourceKey);

        HttpConnectorDto dto = convertHttpDto(req);
        httpConnectorService.add(dto);
    }

    private HttpConnectorDto convertHttpDto(HttpConnectorAddReq req) {
        HttpConnectorDto dto = new HttpConnectorDto();
        dto.setResourceKey(req.getResourceKey());
        dto.setResourceName(req.getResourceName());
        dto.setVersion("V" + System.currentTimeMillis());
        dto.setProjectId(req.getProjectId());
        dto.setConnectorType(req.getConnectorType());
        dto.setDefaultValue(req.getDefaultValue());
        dto.setExceptionValue(req.getExceptionValue());
        dto.setReturnType(req.getReturnType());
        dto.setTimeout(req.getTimeout());
        dto.setUrl(req.getUrl());
        dto.setMethod(req.getMethod());
        dto.setHeader(req.getHeader());
        dto.setParam(req.getParam());
        dto.setCrtUser(LoginUser.getLoginUserName());
        dto.setDeleted(false);
        return dto;
    }

    private EsConnectorDto convertEsDto(EsConnectorAddReq req) {
        EsConnectorDto dto = new EsConnectorDto();
        dto.setResourceKey(req.getResourceKey());
        dto.setResourceName(req.getResourceName());
        dto.setVersion("V" + System.currentTimeMillis());
        dto.setProjectId(req.getProjectId());
        dto.setConnectorType(req.getConnectorType());
        dto.setDefaultValue(req.getDefaultValue());
        dto.setExceptionValue(req.getExceptionValue());
        dto.setReturnType(req.getReturnType());
        dto.setTimeout(req.getTimeout());
        dto.setCondition(req.getCondition());
        dto.setFields(req.getFields());

        dto.setDsl(req.getDsl());
        dto.setEndpoint(req.getEndpoint());
        dto.setCrtUser(LoginUser.getLoginUserName());
        dto.setDeleted(false);
        return dto;

    }

    @Override
    public void updateHttp(HttpConnectorUpdateReq req) throws BizException {
        HttpConnectorDto dto = convertHttpDto(req);
        dto.setId(req.getId());
        dto.setUptUser(LoginUser.getLoginUserName());

        auditQueryService.waitAuditCheck(req.getResourceKey());

        httpConnectorService.update(dto);
    }

    @Override
    public void addEs(EsConnectorAddReq req) throws BizException {
        String resourceKey = projectService.getResourceKey(req.getProjectId(),
                FeatureResourceType.CONNECTOR_ES.getPrefix(), req.getResourceKey());
        req.setResourceKey(resourceKey);

        // 检查资源键唯一性
        if (connectorMapper.selectCountByKey(resourceKey) > 0) {
            throw new BizException("资源键已存在");
        }
        auditQueryService.waitAuditCheck(resourceKey);

        EsConnectorDto dto = convertEsDto(req);
        esConnectorService.add(dto);
    }

    @Override
    public void updateEs(EsConnectorUpdateReq req) throws BizException {
        EsConnectorDto dto = convertEsDto(req);
        dto.setId(req.getId());
        dto.setUptUser(LoginUser.getLoginUserName());

        auditQueryService.waitAuditCheck(req.getResourceKey());

        esConnectorService.update(dto);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        ConnectorEntity entity = connectorMapper.selectById(id);
        if (null == entity) {
            throw new BizException("连接器不存在");
        }
        auditQueryService.waitAuditCheck(entity.getResourceKey());
        jdbcConnectorService.delete(id, LoginUser.getLoginUserName());
    }

    private JdbcConnectorDto convertJdbcDto(JdbcConnectorAddReq req) {
        JdbcConnectorDto dto = new JdbcConnectorDto();
        dto.setResourceKey(req.getResourceKey());
        dto.setResourceName(req.getResourceName());
        dto.setVersion("V" + System.currentTimeMillis());
        dto.setProjectId(req.getProjectId());
        dto.setConnectorType(req.getConnectorType());
        dto.setDefaultValue(req.getDefaultValue());
        dto.setExceptionValue(req.getExceptionValue());
        dto.setTimeout(req.getTimeout());
        dto.setCrtUser(LoginUser.getLoginUserName());
        dto.setDeleted(false);

        dto.setCondition(req.getCondition());
        dto.setConnectorType(req.getConnectorType());
        dto.setDataSourceName(req.getDataSourceName());
        dto.setFields(req.getFields());
        dto.setReturnType(req.getReturnType());
        dto.setSql(req.getSql());
        return dto;
    }
}
