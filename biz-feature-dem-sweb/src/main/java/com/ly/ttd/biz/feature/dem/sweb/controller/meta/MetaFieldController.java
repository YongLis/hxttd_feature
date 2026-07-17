package com.ly.ttd.biz.feature.dem.sweb.controller.meta;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.base.result.Result;
import com.ly.ttd.biz.feature.dem.sweb.consts.LoginUserUtils;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.MetaFieldAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.MetaFieldQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.*;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.res.MetaFieldQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.res.TestCaseRes;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.MetaFieldEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.language.srv.impl.LanguageExecuteFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 元字段管理 Controller
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Slf4j
@RestController
@RequestMapping("/api/meta-field")
@Tag(name = "元字段管理", description = "事件元字段定义、脚本测试、测试用例管理接口")
public class MetaFieldController {

    @Resource
    private MetaFieldAdminService metaFieldAdminService;
    @Resource
    private MetaFieldQueryService metaFieldQueryService;

    @Operation(summary = "分页查询元字段", description = "根据条件分页查询元字段列表")
    @PostMapping("/page")
    public PageResult<MetaFieldQueryRes> page(@RequestBody MetaFieldQueryReq req) {
        return metaFieldQueryService.pageQuery(req);
    }

    private static MetaFieldQueryRes convertRes(MetaFieldEntity e) {
        MetaFieldQueryRes res = new MetaFieldQueryRes();
        res.setId(e.getId());
        res.setResourceKey(e.getResourceKey());
        res.setResourceName(e.getResourceName());
        res.setVersion(e.getVersion());
        res.setProjectId(e.getProjectId());
        res.setLanguage(e.getLanguage());
        res.setScript(e.getScript());
        res.setReturnType(e.getReturnType());
        res.setDefaultValue(e.getDefaultValue());
        res.setExceptionValue(e.getExceptionValue());
        res.setCategoryTag(e.getCategoryTag());
        res.setCrtUser(e.getCrtUser());
        res.setCrtTime(e.getCrtTime());
        return res;
    }

    @Operation(summary = "添加元字段", description = "新增一个元字段定义，触发审批流程")
    @PostMapping("/add")
    public Result<Boolean> add(@Valid @RequestBody MetaFieldAddReq req) {
        try {
            metaFieldAdminService.add(req);
            return Result.success(true);
        } catch (BizException e1) {
            return Result.error(e1.getMessage());
        } catch (Exception e) {
            log.error("add meta field error", e);
        }
        return Result.error("add meta field error");

    }

    @Operation(summary = "更新元字段", description = "更新元字段定义，触发审批流程")
    @PostMapping("/update")
    public Result<Boolean> update(@Valid @RequestBody MetaFieldUpdateReq req) {
        try {
            metaFieldAdminService.update(req);
            return Result.success(true);
        } catch (BizException e1) {
            return Result.error(e1.getMessage());
        } catch (Exception e) {
            log.error("update meta field error", e);
        }
        return Result.error("update meta field error");
    }

    @Operation(summary = "删除元字段")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "元字段ID") @RequestParam Long id) {
        try {
            metaFieldAdminService.delete(id, LoginUser.getLoginUserName());
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("delete meta field error", e);
            return Result.error("删除失败");
        }
    }

    @Operation(summary = "测试元字段脚本", description = "在线执行 Aviator 脚本并返回执行结果")
    @PostMapping("/test")
    public Result<TestCaseRes> test(@Valid @RequestBody TestCaseReq req) {
        long startTime = System.currentTimeMillis();
        TestCaseRes res = new TestCaseRes();

        try {
            // 准备参数
            Map<String, Object> env = new HashMap<>();
            if (StringUtils.isNotBlank(req.getJsonData())) {
                env = JSON.parseObject(req.getJsonData(), Map.class);
            }
            Object result = LanguageExecuteFactory.getInstance(req.getLanguage())
                    .execute(req.getScript(), env);
            res.setSuccess(true);
            res.setResult(result);
            return Result.success(res);

        } catch (Exception e) {
            log.error("元字段测试执行失败, resourceKey={}", req.getResourceKey(), e);
            res.setSuccess(false);
            res.setErrorMessage(e.getMessage());
        }
        res.setExecutionTime(System.currentTimeMillis() - startTime);
        return Result.success(res);
    }

    // 测试数据查询
    @PostMapping("/queryTestData")
    public Result<String> queryTestData(@Valid @RequestBody QueryTestDataReq req) {
        // TODO根据交易号查询接入请求
        return Result.success("{}");

    }

    @Operation(summary = "查询元字段详情", description = "根据ID查询元字段完整信息")
    @PostMapping("/detail")
    public Result<MetaFieldQueryRes> detail(@Parameter(description = "元字段ID") @RequestParam Long id) {
        MetaFieldEntity entity = metaFieldQueryService.getById(id);
        if (entity == null) {
            return Result.error("元字段不存在");
        }
        return Result.success(convertRes(entity));
    }


    @Operation(summary = "获取全部元字段", description = "获取所有元字段（下拉列表用）")
    @PostMapping("/getAllMeta")
    public Result<List<Map<String, Object>>> getAllMeta() {

        Long projectId = LoginUserUtils.INSTANCE.getProjectId();

        List<MetaFieldEntity> list = metaFieldQueryService.getAllByProjectId(projectId);

        List<Map<String, Object>> result = list.stream().map(e -> {
            Map<String, Object> map = new HashMap<>();
            map.put("resourceKey", e.getResourceKey());
            map.put("resourceName", e.getResourceName());
            return map;
        }).collect(Collectors.toList());

        return Result.success(result);
    }
}
