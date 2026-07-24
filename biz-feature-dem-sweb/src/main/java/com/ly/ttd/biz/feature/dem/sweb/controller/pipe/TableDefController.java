package com.ly.ttd.biz.feature.dem.sweb.controller.pipe;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.base.result.Result;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.TableDef;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.TableDefAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.TableDefQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.req.TableDefAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.req.TableDefQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.req.TableDefUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.res.TableDefDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据表定义 Controller（接入统一审核）
 *
 * @author yong.li
 * @since 2026-07-14
 */
@Slf4j
@RestController
@RequestMapping("/api/table-def")
@Tag(name = "表定义管理", description = "数据表定义及字段的 CRUD 管理接口，已接入统一审核")
public class TableDefController {

    @Resource
    private TableDefQueryService tableDefQueryService;

    @Resource
    private TableDefAdminService tableDefAdminService;

    @Operation(summary = "分页查询表定义", description = "根据条件分页查询数据表定义列表，包含字段数量")
    @PostMapping("/page")
    public PageResult<TableDefDetail> page(@RequestBody TableDefQueryReq req) {
        return tableDefQueryService.pageQuery(req);
    }

    @Operation(summary = "新增表定义", description = "提交审核，审批通过后正式创建表和字段")
    @PostMapping("/add")
    public Result<Boolean> add(@Valid @RequestBody TableDefAddReq req) {
        try {
            tableDefAdminService.addTableDef(req);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("add table def error", e);
            return Result.error("新增表定义失败");
        }
    }

    @Operation(summary = "更新表定义", description = "提交审核，审批通过后正式更新表和字段")
    @PostMapping("/update")
    public Result<Boolean> update(@Valid @RequestBody TableDefUpdateReq req) {
        try {
            tableDefAdminService.updateTableDef(req);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("update table def error", e);
            return Result.error("更新表定义失败");
        }
    }

    @Operation(summary = "删除表定义", description = "提交审核，审批通过后正式删除")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "表定义ID") @RequestParam String id) {
        try {
            tableDefAdminService.deleteTableDef(id);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("delete table def error", e);
            return Result.error("删除表定义失败");
        }
    }

    @Operation(summary = "查询表定义详情", description = "根据表名查询表定义及字段信息")
    @PostMapping("/detail")
    public Result<TableDefDetail> detail(@Parameter(description = "表名") @RequestParam String tableName) {
        try {
            TableDefDetail detail = tableDefQueryService.getByTableName(tableName);
            if (detail == null) {
                return Result.error("表定义不存在");
            }
            return Result.success(detail);
        } catch (Exception e) {
            log.error("query table def detail error", e);
            return Result.error("查询失败");
        }
    }

    @Operation(summary = "获取所有有效表定义", description = "获取所有有效表定义（下拉列表用）")
    @PostMapping("/getAll")
    public Result<List<Map<String, String>>> getAll() {
        try {
            List<TableDef> list = tableDefQueryService.getAll();
            List<Map<String, String>> result = list.stream().map(e -> {
                Map<String, String> map = new java.util.HashMap<>();
                map.put("tableName", e.getTableName());
                map.put("dataSource", e.getDataSource());
                return map;
            }).collect(Collectors.toList());
            return Result.success(result);
        } catch (Exception e) {
            log.error("get all table def error", e);
            return Result.error("查询失败");
        }
    }
}
