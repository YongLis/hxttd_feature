package com.ly.ttd.biz.feature.dem.sweb.service.project.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.project.ProjectAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.project.req.ProjectAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.project.req.ProjectUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.feature.admin.api.dto.ProjectDto;
import com.ly.ttd.feature.admin.api.dto.ProjectUserDto;
import com.ly.ttd.feature.admin.api.project.ProjectService;
import com.ly.ttd.feature.admin.api.project.ProjectUserService;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目服务实现类
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Service
@Slf4j
public class ProjectAdminServiceImpl implements ProjectAdminService {
    @Rpcwired
    private ProjectService projectService;
    @Rpcwired
    private ProjectUserService projectUserService;

    @Override
    public void addProject(ProjectAddReq req) {
        ProjectDto dto = new ProjectDto();
        dto.setProjectCode(req.getProjectCode());
        dto.setName(req.getName());
        dto.setCrtUser(LoginUser.getLoginUserName());
        dto.setDeleted(false);
        projectService.add(dto);
    }

    @Override
    public void updateProject(ProjectUpdateReq req) {
        ProjectDto dto = projectService.queryById(req.getId());
        dto.setName(req.getName());
        dto.setUptUser(LoginUser.getLoginUserName());
        projectService.update(dto);
    }

    @Override
    public List<ProjectDto> getAll() {
        return projectService.queryAll();
    }

    @Override
    public String getResourceKey(Long projectId, String prefix, String key) {
        return projectService.getResourceKey(projectId, prefix, key);
    }

    @Override
    public String getPrefix(Long projectId, String prefix) {
        return projectService.getPrefix(projectId, prefix);
    }

    @Override
    public void deleteProject(Long projectId) throws BizException {
        projectService.delete(projectId, LoginUser.getLoginUserName());
    }

    @Override
    public void addProjectUser(Long projectId, String userAccount) throws BizException {
        ProjectUserDto dto = new ProjectUserDto();
        dto.setProjectId(projectId);
        dto.setUserAccount(userAccount);
        dto.setCrtUser(LoginUser.getLoginUserName());
        projectUserService.add(dto);
    }

    @Override
    public void deleteProjectUser(Long projectUserId) throws BizException {
        projectUserService.delete(projectUserId, LoginUser.getLoginUserName());
    }

    @Override
    public ProjectDto getById(Long projectId) {
        return projectService.queryById(projectId);
    }
}
