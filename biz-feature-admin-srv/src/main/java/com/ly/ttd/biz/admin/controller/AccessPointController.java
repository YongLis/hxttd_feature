package com.ly.ttd.biz.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.common.Result;
import com.ly.ttd.biz.admin.mybatis.entity.AccessPointEntity;
import com.ly.ttd.biz.admin.srv.access.AccessPointQueryService;
import com.ly.ttd.biz.admin.srv.access.req.AccessPointAddReq;
import com.ly.ttd.biz.admin.srv.access.req.AccessPointQueryReq;
import com.ly.ttd.biz.admin.srv.access.req.AccessPointUpdateReq;
import com.ly.ttd.biz.admin.srv.access.res.AccessPointDocRes;
import com.ly.ttd.biz.admin.srv.access.res.AccessPointQueryRes;
import com.ly.ttd.biz.admin.srv.resource.ResourceOpFactory;
import com.ly.ttd.biz.admin.srv.user.LoginUser;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 接入点管理 Controller
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Slf4j
@RestController
@RequestMapping("/api/access-point")
@Tag(name = "接入点管理", description = "业务接入点注册、文档自动生成、API 文档导出接口")
public class AccessPointController {

    @Resource
    private AccessPointQueryService accessPointQueryService;

    @Operation(summary = "分页查询接入点", description = "根据条件分页查询接入点列表")
    @PostMapping("/page")
    public PageResult<AccessPointQueryRes> page(@RequestBody AccessPointQueryReq req) {
        return accessPointQueryService.pageQuery(req);
    }

    @Operation(summary = "添加接入点", description = "新增一个接入点，触发审批流程")
    @PostMapping("/add")
    public Result<Boolean> add(@Valid @RequestBody AccessPointAddReq req) {
        try {
            ResourceOpFactory.getService(FeatureResourceType.POINT.getType())
                    .add(req);
            return Result.success(true);
        } catch (Exception e) {
            log.error("add error", e);
            return Result.error("添加接入点失败");
        }

    }

    @Operation(summary = "更新接入点", description = "更新接入点配置，触发审批流程")
    @PostMapping("/update")
    public Result<Boolean> update(@Valid @RequestBody AccessPointUpdateReq req) {
        try {
            ResourceOpFactory.getService(FeatureResourceType.POINT.getType())
                    .update(req);
            return Result.success(true);
        } catch (Exception e) {
            log.error("update error, {}", e);
            return Result.error("更新接入点失败");
        }


    }

    @Operation(summary = "删除接入点")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "接入点ID") @RequestParam Long id) {
        try {
            ResourceOpFactory.getService(FeatureResourceType.POINT.getType()).delete(id);
            return Result.success(true);
        } catch (Exception e) {
            log.error("delete error, id={}", id, e);
            return Result.error("删除接入点失败");
        }

    }

    @Operation(summary = "获取接入点详情", description = "根据ID获取接入点完整信息（含请求参数列表）")
    @PostMapping("/detail")
    public Result<AccessPointQueryRes> detail(@Parameter(description = "接入点ID") @RequestParam Long id) {
        try {
            AccessPointQueryRes res = accessPointQueryService.getDetail(id);
            return Result.success(res);
        } catch (Exception e) {
            log.error("detail error, {}", e);
            return Result.error("获取接入点详情失败");
        }
    }

    @Operation(summary = "获取接入点API文档", description = "根据接入点ID自动生成API接口文档")
    @PostMapping("/getApiDoc")
    public Result<AccessPointDocRes> getApiDoc(@Parameter(description = "接入点ID") @RequestParam Long id) {
        try {
            AccessPointDocRes res = accessPointQueryService.getPointDoc(id);
            return Result.success(res);
        } catch (Exception e) {
            log.error("getApiDoc error, {}", e);
            return Result.error("获取接入点详情失败");
        }
    }

    @Operation(summary = "获取全部接入点", description = "获取所有接入点（下拉列表用）")
    @PostMapping("/getAll")
    public Result<List<Map<String, Object>>> getAll() {
        QueryWrapper<AccessPointEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);

        Long projectId = LoginUser.getProjectId();
        List<AccessPointEntity> list = accessPointQueryService.list(projectId);

        List<Map<String, Object>> result = list.stream().map(e -> {
            Map<String, Object> map = new HashMap<>();
            map.put("code", e.getCode());
            map.put("name", e.getName());
            return map;
        }).collect(Collectors.toList());

        return Result.success(result);
    }

    /**
     * 驼峰命名转下划线
     */
    private static String camelToUnderscore(String camel) {
        if (camel == null || camel.isEmpty()) return camel;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camel.length(); i++) {
            char c = camel.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
