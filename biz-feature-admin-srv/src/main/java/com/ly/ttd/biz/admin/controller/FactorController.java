package com.ly.ttd.biz.admin.controller;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.common.Result;
import com.ly.ttd.biz.admin.srv.factor.FactorQueryService;
import com.ly.ttd.biz.admin.srv.factor.req.*;
import com.ly.ttd.biz.admin.srv.factor.res.FactorQueryRes;
import com.ly.ttd.biz.admin.srv.project.ProjectService;
import com.ly.ttd.biz.admin.srv.resource.ResourceOpFactory;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 指标管理 Controller
 *
 * @author yong.li
 * @since 2026-05-16
 */
@RestController
@RequestMapping("/api/factor")
@Tag(name = "指标管理", description = "指标（Factor）CRUD 管理接口")
@Slf4j
public class FactorController {

    @Resource
    private FactorQueryService factorQueryService;
    @Resource
    private ProjectService projectService;

    @Operation(summary = "分页查询指标", description = "根据指标编码、名称、类型等条件分页查询")
    @PostMapping("/page")
    public PageResult<FactorQueryRes> page(@RequestBody FactorQueryReq req) {
        return factorQueryService.pageQuery(req);
    }

    @Operation(summary = "添加元字段指标", description = "新增一条指标定义，触发审批流程")
    @PostMapping("/addMetaFactor")
    public Result<Boolean> addMetaFactor(@Valid @RequestBody MetaFactorAddReq req) {
        try {
            ResourceOpFactory.getService(FeatureResourceType.FACTOR_META.getType()).add(req);
            return Result.success(true);
        } catch (Exception e) {
            log.error("add error, {}", e);
            return Result.error("添加指标失败");
        }
    }

    @Operation(summary = "添加衍生指标", description = "新增一条指标定义，触发审批流程")
    @PostMapping("/addDerivativeFactor")
    public Result<Boolean> addDerivativeFactor(@Valid @RequestBody DerivativeFactorAddReq req) {
        try {
            ResourceOpFactory.getService(FeatureResourceType.FACTOR_DERIVATIVE.getType()).add(req);
            return Result.success(true);
        } catch (Exception e) {
            log.error("add error, {}", e);
            return Result.error("添加指标失败");
        }
    }

    @Operation(summary = "添加实时特征指标", description = "新增一条指标定义，触发审批流程")
    @PostMapping("/addFeatureFactor")
    public Result<Boolean> addFeatureFactor(@Valid @RequestBody FeatureFactorAddReq req) {
        try {
            ResourceOpFactory.getService(FeatureResourceType.FACTOR_FEATURE.getType()).add(req);
            return Result.success(true);
        } catch (Exception e) {
            log.error("add error, {}", e);
            return Result.error("添加指标失败");
        }
    }

    @Operation(summary = "更新元字段指标", description = "更新元字段指标定义，触发审批流程")
    @PostMapping("/updateMetaFactor")
    public Result<Boolean> updateMetaFactor(@Valid @RequestBody MetaFactorUpdateReq req) {
        try {
            ResourceOpFactory.getService(FeatureResourceType.FACTOR_META.getType()).update(req);
            return Result.success(true);
        } catch (Exception e) {
            log.error("update error, {}", e);
            return Result.error("更新指标失败");
        }
    }

    @Operation(summary = "更新衍生指标", description = "更新衍生指标定义，触发审批流程")
    @PostMapping("/updateDerivativeFactor")
    public Result<Boolean> updateDerivativeFactor(@Valid @RequestBody DerivativeFactorUpdateReq req) {
        try {
            ResourceOpFactory.getService(FeatureResourceType.FACTOR_DERIVATIVE.getType()).update(req);
            return Result.success(true);
        } catch (Exception e) {
            log.error("update error, {}", e);
            return Result.error("更新指标失败");
        }
    }

    @Operation(summary = "更新实时特征指标", description = "更新实时特征指标定义，触发审批流程")
    @PostMapping("/updateFeatureFactor")
    public Result<Boolean> updateFeatureFactor(@Valid @RequestBody FeatureFactorUpdateReq req) {
        try {
            ResourceOpFactory.getService(FeatureResourceType.FACTOR_FEATURE.getType()).update(req);
            return Result.success(true);
        } catch (Exception e) {
            log.error("update error, {}", e);
            return Result.error("更新指标失败");
        }
    }

    @Operation(summary = "删除指标")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "指标ID") @RequestParam Long id,
                                  @Parameter(description = "指标类型") @RequestParam String factorType) {
        try {
            String resourceType = switch (factorType) {
                case "META" -> FeatureResourceType.FACTOR_META.getType();
                case "DERIVATIVE" -> FeatureResourceType.FACTOR_DERIVATIVE.getType();
                case "FEATURE" -> FeatureResourceType.FACTOR_FEATURE.getType();
                default -> throw new RuntimeException("不支持的指标类型: " + factorType);
            };
            ResourceOpFactory.getService(resourceType).delete(id);
            return Result.success(true);
        } catch (Exception e) {
            log.error("delete factor error", e);
            return Result.error("删除失败");
        }
    }
}
