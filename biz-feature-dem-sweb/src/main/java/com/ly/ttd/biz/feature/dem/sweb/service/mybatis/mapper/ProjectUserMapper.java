package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.ProjectUserEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.project.req.ProjectUserQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目用户 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface ProjectUserMapper extends BaseMapper<ProjectUserEntity> {


    /**
     * 分页查询连接器列表
     */
    IPage<ProjectUserEntity> pageQuery(Page<ProjectUserEntity> page, @Param("req") ProjectUserQueryReq req);


    List<ProjectUserEntity> getAll(Long projectId);
}
