package com.ly.ttd.biz.feature.dem.sweb.controller.system;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.base.result.Result;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.DictCodeAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.DictCodeQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictCodeAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictCodeQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictCodeUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.res.DictCodeQueryRes;
import com.ly.ttd.feature.admin.api.dto.DictCodeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典键值管理 Controller
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Slf4j
@RestController
@RequestMapping("/api/dict-code")
@Tag(name = "字典编码管理", description = "字典键值对 CRUD 管理接口")
public class DictCodeController {

    @Resource
    private DictCodeQueryService dictCodeQueryService;

    @Resource
    private DictCodeAdminService dictCodeAdminService;

    @Operation(summary = "分页查询字典编码", description = "根据条件分页查询字典键值对")
    @PostMapping("/page")
    public PageResult<DictCodeQueryRes> page(@RequestBody DictCodeQueryReq req) {
        return dictCodeQueryService.pageQuery(req);
    }

    @Operation(summary = "新增字典编码")
    @PostMapping("/add")
    public Result<Boolean> add(@Valid @RequestBody DictCodeAddReq req) {
        dictCodeAdminService.addDictCode(req);
        return Result.success(true);
    }

    @Operation(summary = "更新字典编码")
    @PostMapping("/update")
    public Result<Boolean> update(@Valid @RequestBody DictCodeUpdateReq req) {
        dictCodeAdminService.updateDictCode(req);
        return Result.success(true);
    }

    @Operation(summary = "根据字典标识查询编码列表", description = "通过 systemCode + dictCode 查询字典的所有键值对")
    @PostMapping("/getDictCode")
    public Result<List<DictCodeQueryRes>> getDictCode(@RequestBody DictQueryReq req) {
        try {
            List<DictCodeDto> dictCodeDtos = dictCodeAdminService.getDictCode(req.getSystemCode(), req.getDictCode());
            if (CollectionUtils.isNotEmpty(dictCodeDtos)) {
                List<DictCodeQueryRes> list = dictCodeDtos
                        .stream().map(t -> entityConvert(t)).collect(Collectors.toList());
                return Result.success(list);
            }
            return Result.success(Arrays.asList());
        } catch (Exception e) {
            log.error("get dict code error", e);
            return Result.error("查询字典键值失败");
        }

    }

    @Operation(summary = "删除字典编码")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "字典编码ID") @RequestParam Long id) {
        dictCodeAdminService.deleteDictCode(id);
        return Result.success(true);
    }

    private DictCodeQueryRes entityConvert(DictCodeDto e) {
        DictCodeQueryRes res = new DictCodeQueryRes();
        res.setId(e.getId());
        res.setDictId(e.getDictId());
        res.setDictKey(e.getDictKey());
        res.setDictValue(e.getDictValue());
        res.setCrtUser(e.getCrtUser());
        res.setCrtTime(e.getCrtTime());
        return res;
    }
}
