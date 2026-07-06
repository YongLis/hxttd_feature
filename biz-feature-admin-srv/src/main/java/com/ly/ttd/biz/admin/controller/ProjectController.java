package com.ly.ttd.biz.admin.controller;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.common.Result;
import com.ly.ttd.biz.admin.mybatis.entity.ProjectEntity;
import com.ly.ttd.biz.admin.srv.project.ProjectQueryService;
import com.ly.ttd.biz.admin.srv.project.ProjectService;
import com.ly.ttd.biz.admin.srv.project.req.ProjectAddReq;
import com.ly.ttd.biz.admin.srv.project.req.ProjectQueryReq;
import com.ly.ttd.biz.admin.srv.project.req.ProjectUpdateReq;
import com.ly.ttd.biz.admin.srv.project.res.ProjectQueryRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目管理控制器
 *
 * @author yong.li
 * @since 2026-05-16
 */
@RestController
@RequestMapping("/api/project")
@Tag(name = "项目管理", description = "项目（租户）CRUD 管理接口")
@Slf4j
public class ProjectController {

    @Resource
    private ProjectQueryService projectQueryService;

    @Resource
    private ProjectService projectService;

    @Operation(summary = "分页查询项目列表", description = "根据条件分页查询项目")
    @PostMapping("/page")
    public PageResult<ProjectQueryRes> page(@RequestBody ProjectQueryReq req) {
        return projectQueryService.pageQuery(req);
    }

    @Operation(summary = "获取全部项目", description = "获取所有项目（下拉列表用）")
    @PostMapping("/getAll")
    public Result<List<ProjectQueryRes>> getAll() {

        List<ProjectEntity> list = projectService.getAll();

        List<ProjectQueryRes> resList = list.stream()
                .map(t -> {
                    ProjectQueryRes queryRes = new ProjectQueryRes();
                    queryRes.setId(t.getId());
                    queryRes.setProjectCode(t.getProjectCode());
                    queryRes.setName(t.getName());
                    queryRes.setCrtUser(t.getCrtUser());
                    queryRes.setCrtTime(t.getCrtTime());
                    return queryRes;
                }).collect(Collectors.toList());

        Result<List<ProjectQueryRes>> resPage = new Result<>();
        resPage.setCode("0000");
        resPage.setMessage("success");
        resPage.setData(resList);

        return resPage;
    }

    @Operation(summary = "添加项目")
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody ProjectAddReq req) {
        try {
            return Result.success(projectService.addProject(req));
        } catch (Exception e) {
            log.error("添加项目失败", e);
            return Result.error("添加项目失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新项目")
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody ProjectUpdateReq req) {
        try {
            return Result.success(projectService.updateProject(req));
        } catch (Exception e) {
            log.error("更新项目失败", e);
            return Result.error("更新项目失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除项目（逻辑删除）")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "项目ID") @RequestParam Long id) {
        try {
            boolean success = projectService.removeById(id);
            return success ? Result.success(true) : Result.error("删除失败");
        } catch (Exception e) {
            log.error("删除项目失败", e);
            return Result.error("删除项目失败: " + e.getMessage());
        }
    }
}
