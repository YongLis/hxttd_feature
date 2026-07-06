package com.ly.ttd.biz.admin.controller;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.common.Result;
import com.ly.ttd.biz.admin.srv.dict.DictQueryService;
import com.ly.ttd.biz.admin.srv.dict.DictService;
import com.ly.ttd.biz.admin.srv.dict.req.DictAddReq;
import com.ly.ttd.biz.admin.srv.dict.req.DictQueryReq;
import com.ly.ttd.biz.admin.srv.dict.req.DictUpdateReq;
import com.ly.ttd.biz.admin.srv.dict.res.DictQueryRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 字典管理 Controller
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Slf4j
@RestController
@RequestMapping("/api/dict")
@Tag(name = "字典管理", description = "字典主表 CRUD 管理接口")
public class DictController {

    @Resource
    private DictQueryService dictQueryService;

    @Resource
    private DictService dictService;

    @Operation(summary = "分页查询字典", description = "根据条件分页查询字典主表")
    @PostMapping("/page")
    public PageResult<DictQueryRes> page(@RequestBody DictQueryReq req) {
        return dictQueryService.pageQuery(req);
    }

    @Operation(summary = "新增字典")
    @PostMapping("/add")
    public Result<Boolean> add(@Valid @RequestBody DictAddReq req) {
        boolean success = dictService.addDict(req);
        return Result.success(success);
    }

    @Operation(summary = "更新字典")
    @PostMapping("/update")
    public Result<Boolean> update(@Valid @RequestBody DictUpdateReq req) {
        boolean success = dictService.updateDict(req);
        return Result.success(success);
    }

    @Operation(summary = "删除字典")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "字典ID") @RequestParam Long id) {
        boolean success = dictService.deleteDict(id);
        return Result.success(success);
    }
}
