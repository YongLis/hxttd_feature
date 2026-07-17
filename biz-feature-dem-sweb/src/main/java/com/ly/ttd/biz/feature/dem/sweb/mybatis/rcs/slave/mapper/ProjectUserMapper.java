package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.ProjectUserEntity;
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
public interface ProjectUserMapper {


    /**
     * 分页查询连接器列表
     */
    List<ProjectUserEntity> pageQuery(Page<ProjectUserEntity> page, @Param("req") ProjectUserQueryReq req);


    List<ProjectUserEntity> getAll(Long projectId);

    /**
     * 根据ID查询
     */
    ProjectUserEntity selectById(@Param("id") Long id);
    /**
     * 查询全部
     */
    List<ProjectUserEntity> selectAllByProjectId(Long projectId);

    List<ProjectUserEntity> selectUserProject(String userName, Long projectId);
}
