package com.ly.ttd.biz.feature.dem.sweb.service.project;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.project.req.ProjectQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.project.res.ProjectQueryRes;

/**
 * 项目查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface ProjectQueryService {

    PageResult<ProjectQueryRes> pageQuery(ProjectQueryReq req);
}
