package com.ly.ttd.biz.feature.dem.sweb.controller.meta;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.base.result.Result;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.MetaFieldTestCaseAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.MetaFieldTestCaseQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.TestCaseAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.TestCaseQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.TestCaseUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.res.TestCaseQueryRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 元字段测试用例 Controller
 *
 * @author yong.li
 * @since 2026-05-16
 */
@RestController
@RequestMapping("/api/test-case")
@Tag(name = "元字段测试用例", description = "元字段脚本测试用例管理接口")
public class MetaFieldTestCaseController {

    @Resource
    private MetaFieldTestCaseAdminService testCaseService;
    @Resource
    private MetaFieldTestCaseQueryService testCaseQueryService;


    @Operation(summary = "分页查询测试用例", description = "根据元字段编码、用例类型等条件分页查询")
    @PostMapping("/page")
    public PageResult<TestCaseQueryRes> page(@RequestBody TestCaseQueryReq req) {
       return testCaseQueryService.pageQuery(req);
    }

    @Operation(summary = "新增测试用例", description = "为元字段新增一个测试用例（正常/边界/异常）")
    @PostMapping("/add")
    public Result<Boolean> add(@Valid @RequestBody TestCaseAddReq req) {
        boolean success = testCaseService.addTestCase(req);
        return Result.success(success);
    }

    @Operation(summary = "更新测试用例")
    @PostMapping("/update")
    public Result<Boolean> update(@Valid @RequestBody TestCaseUpdateReq req) {
        boolean success = testCaseService.updateTestCase(req);
        return Result.success(success);
    }

    @Operation(summary = "删除测试用例")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "测试用例ID") @RequestParam Long id) {
        boolean success = testCaseService.deleteTestCase(id);
        return Result.success(success);
    }
}
