package com.ly.ttd.biz.feature.dem.sweb.service.tableDef.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.AuditQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.project.ProjectAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.TableDefAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.req.TableColumnReq;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.req.TableDefAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.req.TableDefUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.feature.admin.api.dto.TableColumnDto;
import com.ly.ttd.feature.admin.api.dto.TableDefDto;
import com.ly.ttd.feature.admin.api.pipe.TableDefService;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据表定义 管理服务实现（接入统一审核）
 *
 * @author yong.li
 * @since 2026-07-14
 */
@Slf4j
@Service
public class TableDefAdminServiceImpl implements TableDefAdminService {

    @Rpcwired
    private TableDefService tableDefService;

    @Resource
    private ProjectAdminService projectAdminService;

    @Resource
    private AuditQueryService auditQueryService;

    @Override
    public void addTableDef(TableDefAddReq req) throws BizException {
        String loginUser = LoginUser.getLoginUserName();
        TableDefDto dto = convertAddDto(req);
        dto.setCrtUser(loginUser);
        dto.setUptUser(loginUser);
        dto.setDeleted(false);

        // 检查待审核记录
        auditQueryService.waitAuditCheck(dto.getTableName());

        tableDefService.add(dto);
    }

    @Override
    public void updateTableDef(TableDefUpdateReq req) throws BizException {
        String loginUser = LoginUser.getLoginUserName();
        TableDefDto dto = convertUpdateDto(req);
        dto.setUptUser(loginUser);

        // 检查待审核记录
        auditQueryService.waitAuditCheck(dto.getTableName());

        tableDefService.update(dto);
    }

    @Override
    public void deleteTableDef(String id) throws BizException {
        String loginUser = LoginUser.getLoginUserName();
        tableDefService.delete(id, loginUser);
    }

    private TableDefDto convertAddDto(TableDefAddReq req) {
        String loginUser = LoginUser.getLoginUserName();
        TableDefDto dto = new TableDefDto();
        dto.setTableName(req.getTableName());
        dto.setDataSource(req.getDataSource());
        dto.setColumns(convertColumns(req.getTableName(), req.getColumns(), loginUser));
        return dto;
    }

    private TableDefDto convertUpdateDto(TableDefUpdateReq req) {
        TableDefDto dto = new TableDefDto();
        dto.setId(req.getId());
        dto.setTableName(req.getTableName());
        dto.setDataSource(req.getDataSource());
        dto.setColumns(convertColumns(req.getTableName(), req.getColumns(), null));
        return dto;
    }

    private List<TableColumnDto> convertColumns(String tableName, List<TableColumnReq> columns, String crtUser) {
        if (columns == null) {
            return null;
        }
        return columns.stream().map(c -> {
            TableColumnDto col = new TableColumnDto();
            col.setId(c.getId());
            col.setTableName(tableName);
            col.setColumnName(c.getColumnName());
            col.setColumnType(c.getColumnType());
            col.setNullAble(c.getNullAble());
            col.setFactorCode(c.getFactorCode());
            col.setCrtUser(crtUser);
            col.setUptUser(crtUser);
            col.setDeleted(false);
            return col;
        }).collect(Collectors.toList());
    }
}
