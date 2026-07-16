package com.ly.ttd.biz.feature.admin.srv.project.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.ProjectUserEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.ProjectUserMapper;
import com.ly.ttd.feature.admin.api.dto.ProjectUserDto;
import com.ly.ttd.feature.admin.api.project.ProjectUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 项目用户 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class ProjectUserServiceImpl implements ProjectUserService {
    @Resource
    private ProjectUserMapper projectUserMapper;

    @Override
    public Long add(ProjectUserDto dto) throws BizException {
        ProjectUserEntity entity = new ProjectUserEntity();
        BeanUtils.copyProperties(dto, entity);
        projectUserMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(ProjectUserDto dto) throws BizException {
        ProjectUserEntity entity = new ProjectUserEntity();
        BeanUtils.copyProperties(dto, entity);
        projectUserMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        projectUserMapper.deleteById(id);
    }

    @Override
    public ProjectUserDto queryById(Long id) throws BizException {
        ProjectUserEntity entity = projectUserMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        ProjectUserDto dto = new ProjectUserDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
