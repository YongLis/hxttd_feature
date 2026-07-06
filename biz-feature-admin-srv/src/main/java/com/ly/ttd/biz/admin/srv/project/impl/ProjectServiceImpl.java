package com.ly.ttd.biz.admin.srv.project.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.ttd.biz.admin.mybatis.entity.ProjectEntity;
import com.ly.ttd.biz.admin.mybatis.entity.ProjectUserEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.ProjectMapper;
import com.ly.ttd.biz.admin.mybatis.mapper.ProjectUserMapper;
import com.ly.ttd.biz.admin.srv.project.ProjectService;
import com.ly.ttd.biz.admin.srv.project.req.ProjectAddReq;
import com.ly.ttd.biz.admin.srv.project.req.ProjectUpdateReq;
import com.ly.ttd.biz.admin.srv.user.LoginUser;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectEntity> implements ProjectService {

    @Resource
    private ProjectUserMapper projectUserMapper;

    @Override
    public Boolean addProject(ProjectAddReq req) {
        ProjectEntity project = new ProjectEntity();
        project.setProjectCode(req.getProjectCode());
        project.setName(req.getName());
        project.setDeleted(false);
        project.setCrtUser(LoginUser.getLoginUserName());

        return save(project);
    }

    @Override
    public Boolean updateProject(ProjectUpdateReq req) {
        ProjectEntity project = getById(req.getId());
        if (project == null) {
            throw new RuntimeException("项目不存在");
        }
        UpdateWrapper<ProjectEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", req.getId());
        updateWrapper.set("name", req.getName());

        return update(updateWrapper);
    }

    @Override
    public List<ProjectEntity> getAll() {
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", false);
        queryWrapper.orderByAsc("id");
        return this.list(queryWrapper);
    }

    @Override
    public String getResourceKey(Long projectId, String prefix, String key) {
        ProjectEntity projectEntity = getById(projectId);
        if (StringUtils.isEmpty(prefix)) {
            return String.format("%s_%s", projectEntity.getProjectCode(), key);
        }
        return String.format("%s_%s_%s", projectEntity.getProjectCode(), prefix, key);
    }

    @Override
    public String getPrefix(Long projectId, String prefix) {
        ProjectEntity projectEntity = getById(projectId);
        return String.format("%s_%s_", projectEntity.getProjectCode(), prefix);
    }

    @Override
    public Boolean deleteProject(Long projectId) throws FeatureBizException {
        checkProject(projectId);

        UpdateWrapper<ProjectEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", projectId);
        updateWrapper.set("deleted", true);
        updateWrapper.set("upt_user", LoginUser.getLoginUserName());
        return update(updateWrapper);
    }

    private void checkProject(Long projectId) {
        ProjectEntity projectEntity = getById(projectId);
        if (null == projectEntity) {
            throw new FeatureBizException("01", "项目不存在");
        }
    }

    @Override
    public void addProjectUser(Long projectId, String userAccount) throws FeatureBizException {
        checkProject(projectId);

        QueryWrapper<ProjectUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", projectId);
        queryWrapper.eq("user_account", userAccount);

        ProjectUserEntity projectUserEntity = projectUserMapper.selectOne(queryWrapper);
        if (null != projectUserEntity) {
            UpdateWrapper<ProjectUserEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", projectUserEntity.getId());
            updateWrapper.set("deleted", false);
            updateWrapper.set("upt_user", LoginUser.getLoginUserName());
            projectUserMapper.update(updateWrapper);
        } else {
            projectUserEntity = new ProjectUserEntity();
            projectUserEntity.setProjectId(projectId);
            projectUserEntity.setUserAccount(userAccount);
            projectUserEntity.setCrtUser(LoginUser.getLoginUserName());
            projectUserEntity.setDeleted(false);
            projectUserMapper.insert(projectUserEntity);
        }
    }

    @Override
    public void deleteProjectUser(Long projectId, String userName) throws FeatureBizException {

        QueryWrapper<ProjectUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", projectId);
        queryWrapper.eq("user_name", userName);
        queryWrapper.eq("deleted", false);

        ProjectUserEntity projectUserEntity = projectUserMapper.selectOne(queryWrapper);
        if (null != projectUserEntity) {
            UpdateWrapper<ProjectUserEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", projectUserEntity.getId());
            updateWrapper.set("deleted", true);
            updateWrapper.set("upt_user", LoginUser.getLoginUserName());
            projectUserMapper.update(updateWrapper);
        }

    }
}
