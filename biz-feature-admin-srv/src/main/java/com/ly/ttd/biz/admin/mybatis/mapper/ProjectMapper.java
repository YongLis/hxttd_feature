package com.ly.ttd.biz.admin.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.admin.mybatis.entity.ProjectEntity;
import com.ly.ttd.biz.admin.srv.project.req.ProjectQueryReq;
import com.ly.ttd.biz.admin.srv.project.res.ProjectQueryRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 项目 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface ProjectMapper extends BaseMapper<ProjectEntity> {

    IPage<ProjectQueryRes> pageQuery(Page<ProjectQueryRes> page, @Param("req") ProjectQueryReq req);
}
