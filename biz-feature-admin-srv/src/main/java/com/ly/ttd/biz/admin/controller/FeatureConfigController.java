package com.ly.ttd.biz.admin.controller;

import com.ly.ttd.biz.admin.common.ConditionExpressOpEnum;
import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.common.Result;
import com.ly.ttd.biz.admin.mybatis.entity.FeatureConfigEntity;
import com.ly.ttd.biz.admin.srv.feature.FeatureConfigQueryService;
import com.ly.ttd.biz.admin.srv.feature.req.FeatureConfigAddReq;
import com.ly.ttd.biz.admin.srv.feature.req.FeatureConfigQueryReq;
import com.ly.ttd.biz.admin.srv.feature.req.FeatureConfigUpdateReq;
import com.ly.ttd.biz.admin.srv.feature.res.FeatureConfigDetailRes;
import com.ly.ttd.biz.admin.srv.feature.res.FeatureConfigListRes;
import com.ly.ttd.biz.admin.srv.feature.res.FeatureConfigQueryRes;
import com.ly.ttd.biz.admin.srv.resource.ResourceOpFactory;
import com.ly.ttd.biz.admin.srv.user.LoginUser;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 特征配置 Controller
 *
 * @author yong.li
 * @since 2026-05-16
 */
@RestController
@RequestMapping("/api/feature-config")
@Tag(name = "特征配置", description = "实时特征配置 CRUD 管理接口")
@Slf4j
public class FeatureConfigController {

    @Resource
    private FeatureConfigQueryService featureConfigQueryService;

    @Operation(summary = "分页查询特征配置", description = "根据项目、特征编码等条件分页查询")
    @PostMapping("/page")
    public PageResult<FeatureConfigQueryRes> page(@RequestBody FeatureConfigQueryReq req) {
        return featureConfigQueryService.pageQuery(req);
    }

    @Operation(summary = "查询特征配置列表", description = "查询当前项目下所有特征配置（下拉列表用）")
    @PostMapping("/list")
    public Result<List<FeatureConfigListRes>> list() {
        try {
            Long projectId = LoginUser.getProjectId();
            List<FeatureConfigListRes> list = featureConfigQueryService.getListByProjectId(projectId);
            return Result.success(list);
        } catch (Exception e) {
            log.error("get feature config list failed", e);
            return Result.error("get failed");
        }

    }

    /**
     * 获取条件操作符列表
     */
    @Operation(summary = "获取条件操作符列表", description = "返回所有支持的条件表达式操作符（等于/大于/小于/包含等）")
    @PostMapping("/getCondtionExpressOp")
    public Result<List<Map<String, String>>> getCondtionExpressOp() {
        List<Map<String, String>> result = new ArrayList<>();
        for (ConditionExpressOpEnum op : ConditionExpressOpEnum.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("code", op.getCode());
            map.put("msg", op.getMsg());
            result.add(map);
        }
        return Result.success(result);
    }

    @Operation(summary = "新增特征配置", description = "新增一条特征配置，触发审批流程")
    @PostMapping("/add")
    public Result<Boolean> add(@Valid @RequestBody FeatureConfigAddReq req) {
        try {
            ResourceOpFactory.getService(FeatureResourceType.FEATURE_CONFIG.getType())
                    .add(req);
            return Result.success();
        } catch (FeatureBizException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e1) {
            log.error("add feature config failed", e1);
            return Result.error("add failed");
        }

    }

    @Operation(summary = "更新特征配置", description = "更新特征配置，触发审批流程")
    @PostMapping("/update")
    public Result<Boolean> update(@Valid @RequestBody FeatureConfigUpdateReq req) {
        try {
            ResourceOpFactory.getService(FeatureResourceType.FEATURE_CONFIG.getType())
                    .update(req);
            return Result.success();
        } catch (FeatureBizException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e1) {
            log.error("update feature config failed", e1);
            return Result.error("update failed");
        }

    }

    /**
     * 获取特征配置详情（含 configForm）
     */
    @Operation(summary = "查询特征配置详情", description = "根据ID查询特征配置完整信息")
    @GetMapping("/detail")
    public Result<FeatureConfigDetailRes> detail(@Parameter(description = "特征配置ID") @RequestParam Long id) {
        FeatureConfigEntity entity = featureConfigQueryService.getById(id);
        if (entity == null) {
            return Result.error("配置不存在");
        }
        return Result.success(FeatureConfigDetailRes.fromEntity(entity));
    }

    @Operation(summary = "删除特征配置")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "特征配置ID") @RequestParam Long id) {
        try {
            ResourceOpFactory.getService(FeatureResourceType.FEATURE_CONFIG.getType()).delete(id);
            return Result.success(true);
        } catch (FeatureBizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e1) {
            log.error("delete feature config failed", e1);
            return Result.error("delete failed");
        }

    }
}
