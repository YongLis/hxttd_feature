package com.ly.ttd.biz.feature.admin.srv.project.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.ProjectEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.ProjectMapper;
import com.ly.ttd.feature.admin.api.dto.ProjectDto;
import com.ly.ttd.feature.admin.api.project.ProjectService;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class ProjectServiceImpl implements ProjectService {
    @Resource
    private ProjectMapper projectMapper;

    @Override
    public Long add(ProjectDto dto) throws BizException {
        ProjectEntity entity = new ProjectEntity();
        BeanUtils.copyProperties(dto, entity);
        projectMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(ProjectDto dto) throws BizException {
        ProjectEntity entity = new ProjectEntity();
        BeanUtils.copyProperties(dto, entity);
        projectMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        projectMapper.deleteById(id);
    }

    @Override
    public ProjectDto queryById(Long id) throws BizException {
        ProjectEntity entity = projectMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        ProjectDto dto = new ProjectDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public List<ProjectDto> queryAll() {
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", false);
        List<ProjectEntity> list = projectMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().map(t -> {
            ProjectDto dto = new ProjectDto();
            BeanUtils.copyProperties(t, dto);
            return dto;
        }).toList();
    }

    @Override
    public String getResourceKey(Long projectId, String prefix, String resourceKey) {
        ProjectEntity projectEntity = projectMapper.selectById(projectId);
        if (StringUtils.isEmpty(prefix)) {
            return String.format("%s_%s", projectEntity.getProjectCode(), resourceKey);
        }
        return String.format("%s_%s_%s", projectEntity.getProjectCode(), prefix, resourceKey);
    }

    @Override
    public String getPrefix(Long projectId, String prefix) {
        ProjectEntity projectEntity = projectMapper.selectById(projectId);
        return String.format("%s_%s_", projectEntity.getProjectCode(), prefix);
    }
}
