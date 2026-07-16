package com.ly.ttd.biz.feature.dem.sweb.controller.system;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.base.result.Result;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.DictAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.DictQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.res.DictQueryRes;
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
    private DictAdminService dictAdminService;

    @Operation(summary = "分页查询字典", description = "根据条件分页查询字典主表")
    @PostMapping("/page")
    public PageResult<DictQueryRes> page(@RequestBody DictQueryReq req) {
        return dictQueryService.pageQuery(req);
    }

    @Operation(summary = "新增字典")
    @PostMapping("/add")
    public Result<Boolean> add(@Valid @RequestBody DictAddReq req) {
        dictAdminService.addDict(req);
        return Result.success(true);
    }

    @Operation(summary = "更新字典")
    @PostMapping("/update")
    public Result<Boolean> update(@Valid @RequestBody DictUpdateReq req) {
        dictAdminService.updateDict(req);
        return Result.success(true);
    }

    @Operation(summary = "删除字典")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "字典ID") @RequestParam Long id) {
        dictAdminService.deleteDict(id);
        return Result.success(true);
    }
}
