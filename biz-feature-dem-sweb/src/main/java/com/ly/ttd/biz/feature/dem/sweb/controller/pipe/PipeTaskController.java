package com.ly.ttd.biz.feature.dem.sweb.controller.pipe;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.base.result.Result;
import com.ly.ttd.biz.feature.dem.sweb.service.pipe.PipeTaskAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.pipe.PipeTaskQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.pipe.req.PipeTaskQueryReq;
import com.ly.ttd.feature.admin.api.dto.PipeTaskDto;
import com.ly.ttd.feature.admin.api.pipe.PipeTaskService;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 数据管道任务 Controller（已接入统一审核流程）
 *
 * @author yong.li
 * @since 2026-07-22
 */
@Slf4j
@RestController
@RequestMapping("/api/pipe-task")
@Tag(name = "管道任务管理", description = "数据管道任务的 CRUD 管理接口，已接入统一审核")
public class PipeTaskController {

    @Rpcwired
    private PipeTaskService pipeTaskService;
    @Resource
    private PipeTaskAdminService pipeTaskAdminService;
    @Resource
    private PipeTaskQueryService pipeTaskQueryService;

    @Operation(summary = "分页查询管道任务")
    @PostMapping("/page")
    public PageResult<PipeTaskDto> page(@RequestBody PipeTaskQueryReq req) {
        try {
            return pipeTaskQueryService.pageQuery(req);
        } catch (Exception e) {
            log.error("page query pipe task error", e);
            PageResult<PipeTaskDto> result = new PageResult<>();
            result.setCode("9999");
            result.setMessage(e.getMessage());
            return result;
        }
    }

    @Operation(summary = "新增管道任务", description = "提交审核，审批通过后正式创建")
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody PipeTaskDto req) {
        try {
            pipeTaskAdminService.add(req);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("add pipe task error", e);
            return Result.error("新增管道任务失败");
        }
    }

    @Operation(summary = "更新管道任务", description = "提交审核，审批通过后正式更新")
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody PipeTaskDto req) {
        try {
            pipeTaskAdminService.update(req);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("update pipe task error", e);
            return Result.error("更新管道任务失败");
        }
    }

    @Operation(summary = "删除管道任务", description = "提交审核，审批通过后正式删除")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "任务ID") @RequestParam String id) {
        try {
            pipeTaskAdminService.delete(id);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("delete pipe task error", e);
            return Result.error("删除管道任务失败");
        }
    }

    @Operation(summary = "查询管道任务详情")
    @PostMapping("/detail")
    public Result<PipeTaskDto> detail(@Parameter(description = "任务ID") @RequestParam String id) {
        try {
            PipeTaskDto dto = pipeTaskService.queryById(id);
            if (dto == null) {
                return Result.error("管道任务不存在");
            }
            return Result.success(dto);
        } catch (Exception e) {
            log.error("query pipe task detail error", e);
            return Result.error("查询失败");
        }
    }
}
