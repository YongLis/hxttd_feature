package com.ly.ttd.biz.feature.dem.sweb.controller.system;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.base.result.Result;
import com.ly.ttd.biz.feature.dem.sweb.service.project.ProjectAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.project.ProjectUserQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.project.req.DeleteReq;
import com.ly.ttd.biz.feature.dem.sweb.service.project.req.ProjectUserAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.project.req.ProjectUserQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.project.res.ProjectUserQueryRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目用户管理控制器
 *
 * @author yong.li
 * @since 2026-05-16
 */
@RestController
@RequestMapping("/api/project-user")
@Tag(name = "项目用户管理", description = "项目用户（租户）CRUD 管理接口")
@Slf4j
public class ProjectUserController {

    @Resource
    private ProjectUserQueryService projectUserQueryService;

    @Resource
    private ProjectAdminService projectAdminService;

    @Operation(summary = "分页查询项目用户列表", description = "根据条件分页查询项目用户")
    @PostMapping("/page")
    public PageResult<ProjectUserQueryRes> page(@RequestBody ProjectUserQueryReq req) {
        return projectUserQueryService.pageQuery(req);
    }

    @Operation(summary = "添加项目成员")
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody ProjectUserAddReq req) {
        try {
            projectAdminService.addProjectUser(req.getProjectId(), req.getUserAccount());
            return Result.success();
        } catch (Exception e) {
            log.error("添加项目成员失败", e);
            return Result.error("添加项目成员失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除项目成员")
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody DeleteReq req) {
        try {
            projectAdminService.deleteProjectUser(req.getId());
            return Result.success();
        } catch (Exception e) {
            log.error("删除项目成员失败", e);
            return Result.error("删除项目成员失败: " + e.getMessage());
        }
    }
}
