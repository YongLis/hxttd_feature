package com.ly.ttd.biz.admin.controller;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.common.Result;
import com.ly.ttd.biz.admin.mybatis.entity.ConnectorEntity;
import com.ly.ttd.biz.admin.srv.connector.ConnectorQueryService;
import com.ly.ttd.biz.admin.srv.connector.req.ConnectorAddReq;
import com.ly.ttd.biz.admin.srv.connector.req.ConnectorQueryReq;
import com.ly.ttd.biz.admin.srv.connector.req.ConnectorUpdateReq;
import com.ly.ttd.biz.admin.srv.connector.res.ConnectorQueryRes;
import com.ly.ttd.biz.admin.srv.resource.ResourceOpFactory;
import com.ly.ttd.consts.exception.BizException;
import com.ly.ttd.feature.common.enums.ConnectorEnum;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
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

    @Operation(summary = "新增连接器", description = "新增一个数据源连接器，触发审批流程")
    @PostMapping("/add")
    public Result<Boolean> add(@Valid @RequestBody ConnectorAddReq req) {
        try {
            String resourceType = toFeatureResourceType(req.getConnectorType());
            ResourceOpFactory.getService(resourceType).add(req);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e2) {
            log.error("add connector error", e2);
        }
        return Result.error("add connector error");
    }

    @Operation(summary = "更新连接器", description = "更新连接器配置，触发审批流程")
    @PostMapping("/update")
    public Result<Boolean> update(@Valid @RequestBody ConnectorUpdateReq req) {
        try {
            String resourceType = toFeatureResourceType(req.getConnectorType());
            ResourceOpFactory.getService(resourceType).update(req);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e2) {
            log.error("update connector error", e2);
        }
        return Result.error("update connector error");
    }

    /**
     * 将连接器类型码(JDBC/ES/HTTP)映射为 FeatureResourceType
     */
    private String toFeatureResourceType(String connectorType) {
        ConnectorEnum ce = ConnectorEnum.getEnumByCode(connectorType);
        if (ce == null) {
            throw new BizException("01", "不支持的连接器类型: " + connectorType);
        }
        return switch (ce) {
            case JDBC -> FeatureResourceType.CONNECTOR_JDBC.getType();
            case ES -> FeatureResourceType.CONNECTOR_ES.getType();
            case HTTP -> FeatureResourceType.CONNECTOR_HTTP.getType();
            default -> throw new BizException("01", "不支持的连接器类型: " + connectorType);
        };
    }

    @Operation(summary = "删除连接器")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "连接器ID") @RequestParam Long id,
                                  @Parameter(description = "连接器类型") @RequestParam String connectorType) {
        try {
            String resourceType = toFeatureResourceType(connectorType);
            ResourceOpFactory.getService(resourceType).delete(id);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e2) {
            log.error("delete connector error", e2);
        }
        return Result.error("delete connector error");
    }
}
