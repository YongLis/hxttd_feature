package com.ly.ttd.biz.admin.srv.project;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.srv.project.req.ProjectQueryReq;
import com.ly.ttd.biz.admin.srv.project.res.ProjectQueryRes;

/**
 * 项目查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface ProjectQueryService {

    PageResult<ProjectQueryRes> pageQuery(ProjectQueryReq req);
}
