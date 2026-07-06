package com.ly.ttd.biz.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.common.Result;
import com.ly.ttd.biz.admin.mybatis.entity.MetaFieldTestCaseEntity;
import com.ly.ttd.biz.admin.srv.metaField.MetaFieldTestCaseService;
import com.ly.ttd.biz.admin.srv.metaField.req.TestCaseAddReq;
import com.ly.ttd.biz.admin.srv.metaField.req.TestCaseQueryReq;
import com.ly.ttd.biz.admin.srv.metaField.req.TestCaseUpdateReq;
import com.ly.ttd.biz.admin.srv.metaField.res.TestCaseQueryRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    private MetaFieldTestCaseService testCaseService;

    @Operation(summary = "分页查询测试用例", description = "根据元字段编码、用例类型等条件分页查询")
    @PostMapping("/page")
    public PageResult<TestCaseQueryRes> page(@RequestBody TestCaseQueryReq req) {
        Page<MetaFieldTestCaseEntity> page = new Page<>(req.getCurrent(), req.getPageSize());

        QueryWrapper<MetaFieldTestCaseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);

        if (StringUtils.isNotEmpty(req.getMetaFieldCode())) {
            wrapper.eq("meta_field_code", req.getMetaFieldCode());
        }
        if (StringUtils.isNotEmpty(req.getCaseType())) {
            wrapper.eq("case_type", req.getCaseType());
        }
        if (StringUtils.isNotEmpty(req.getBizOrderNo())) {
            wrapper.like("bizOrderNo", req.getBizOrderNo());
        }

        wrapper.orderByDesc("crt_time");

        Page<MetaFieldTestCaseEntity> pageResult = testCaseService.page(page, wrapper);

        PageResult<TestCaseQueryRes> result = new PageResult<>();
        result.setCurrent(pageResult.getCurrent());
        result.setPageSize(pageResult.getSize());
        result.setTotal(pageResult.getTotal());

        List<TestCaseQueryRes> resList = pageResult.getRecords().stream().map(e -> {
            TestCaseQueryRes r = new TestCaseQueryRes();
            r.setId(e.getId());
            r.setMetaFieldCode(e.getMetaFieldCode());
            r.setCaseType(e.getCaseType());
            r.setBizOrderNo(e.getBizOrderNo());
            r.setCaseContent(e.getCaseContent());
            r.setTargetValue(e.getTargetValue());
            r.setCrtUser(e.getCrtUser());
            r.setCrtTime(e.getCrtTime());
            return r;
        }).collect(Collectors.toList());

        result.setData(resList);
        return result;
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
