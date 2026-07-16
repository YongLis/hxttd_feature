package com.ly.ttd.biz.feature.admin.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ly.ttd.biz.feature.admin.mybatis.entity.ProjectUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 项目用户 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface ProjectUserMapper extends BaseMapper<ProjectUserEntity> {


    List<ProjectUserEntity> getAll(Long projectId);
}
