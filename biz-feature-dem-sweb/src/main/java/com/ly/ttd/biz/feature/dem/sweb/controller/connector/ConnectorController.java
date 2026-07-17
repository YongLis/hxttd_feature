package com.ly.ttd.biz.feature.dem.sweb.controller.connector;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.base.result.Result;
import com.ly.ttd.biz.feature.dem.sweb.service.connector.ConnectorAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.connector.ConnectorQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.connector.req.*;
import com.ly.ttd.biz.feature.dem.sweb.service.connector.res.ConnectorQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.ConnectorEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 连接器管理 Controller
 *
 * @author yong.li
 * @since 2026-05-27
 */
@RestController
@RequestMapping("/api/connector")
@Tag(name = "连接器管理", description = "JDBC/ES/HBase/HTTP 数据源连接器 CRUD 管理接口")
@Slf4j
public class ConnectorController {

    @Resource
    private ConnectorQueryService connectorQueryService;
    @Resource
    private ConnectorAdminService connectorAdminService;

    @Operation(summary = "分页查询连接器", description = "根据连接器类型、名称等条件分页查询")
    @PostMapping("/page")
    public PageResult<ConnectorQueryRes> page(@RequestBody ConnectorQueryReq req) {
        return connectorQueryService.pageQuery(req);
    }

    @Operation(summary = "查询连接器详情", description = "根据ID查询连接器完整配置信息")
    @GetMapping("/detail")
    public Result<ConnectorEntity> detail(@Parameter(description = "连接器ID") @RequestParam Long id) {
        ConnectorEntity entity = connectorQueryService.getById(id);
        if (entity == null) {
            return Result.error("连接器不存在");
        }
        return Result.success(entity);
    }

    @Operation(summary = "新增Jdbc连接器", description = "新增一个数据源连接器，触发审批流程")
    @PostMapping("/addJdbc")
    public Result<Boolean> addJdbc(@Valid @RequestBody JdbcConnectorAddReq req) {
        try {
            connectorAdminService.addJdbc(req);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e2) {
            log.error("add jdbc connector error", e2);
        }
        return Result.error("add jdbc connector error");
    }

    @Operation(summary = "更新Jdbc连接器", description = "更新连接器配置，触发审批流程")
    @PostMapping("/updateJdbc")
    public Result<Boolean> updateJdbc(@Valid @RequestBody JdbcConnectorUpdateReq req) {
        try {
            connectorAdminService.updateJdbc(req);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e2) {
            log.error("update jdbc connector error", e2);
        }
        return Result.error("update jdbc connector error");
    }


    @Operation(summary = "新增Http连接器", description = "新增一个数据源连接器，触发审批流程")
    @PostMapping("/addHttp")
    public Result<Boolean> addHttp(@Valid @RequestBody HttpConnectorAddReq req) {
        try {
            connectorAdminService.addHttp(req);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e2) {
            log.error("add http connector error", e2);
        }
        return Result.error("add http connector error");
    }

    @Operation(summary = "更新Http连接器", description = "更新连接器配置，触发审批流程")
    @PostMapping("/updateHttp")
    public Result<Boolean> updateHttp(@Valid @RequestBody HttpConnectorUpdateReq req) {
        try {
            connectorAdminService.updateHttp(req);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e2) {
            log.error("update http connector error", e2);
        }
        return Result.error("update http connector error");
    }


    @Operation(summary = "新增Es连接器", description = "新增一个数据源连接器，触发审批流程")
    @PostMapping("/addEs")
    public Result<Boolean> addEs(@Valid @RequestBody EsConnectorAddReq req) {
        try {
            connectorAdminService.addEs(req);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e2) {
            log.error("add es connector error", e2);
        }
        return Result.error("add es connector error");
    }

    @Operation(summary = "更新es连接器", description = "更新连接器配置，触发审批流程")
    @PostMapping("/updateEs")
    public Result<Boolean> updateEs(@Valid @RequestBody EsConnectorUpdateReq req) {
        try {
            connectorAdminService.updateEs(req);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e2) {
            log.error("update es connector error", e2);
        }
        return Result.error("update es connector error");
    }


    @Operation(summary = "删除连接器")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "连接器ID") @RequestParam Long id,
                                  @Parameter(description = "连接器类型") @RequestParam String connectorType) {
        try {
            connectorAdminService.delete(id, LoginUser.getLoginUserName());
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e2) {
            log.error("delete connector error", e2);
        }
        return Result.error("delete connector error");
    }
}
