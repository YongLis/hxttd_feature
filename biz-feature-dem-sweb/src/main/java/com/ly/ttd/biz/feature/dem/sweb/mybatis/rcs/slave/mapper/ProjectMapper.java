package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.ProjectEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.project.req.ProjectQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.project.res.ProjectQueryRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface ProjectMapper {

    List<ProjectQueryRes> pageQuery(Page<ProjectQueryRes> page, @Param("req") ProjectQueryReq req);

    /**
     * 根据ID查询
     */
    ProjectEntity selectById(@Param("id") Long id);
    /**
     * 查询全部
     */
    List<ProjectEntity> selectAll();
}
