package com.ly.ttd.biz.feature.dem.sweb.service.project;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.project.req.ProjectUserQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.project.res.ProjectUserQueryRes;

import java.util.List;

/**
 * 项目查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface ProjectUserQueryService {

    PageResult<ProjectUserQueryRes> pageQuery(ProjectUserQueryReq req);

    List<ProjectUserQueryRes> getAll(Long projectId);
}
