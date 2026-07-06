package com.ly.ttd.biz.admin.srv.project;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.srv.project.req.ProjectUserQueryReq;
import com.ly.ttd.biz.admin.srv.project.res.ProjectUserQueryRes;

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
