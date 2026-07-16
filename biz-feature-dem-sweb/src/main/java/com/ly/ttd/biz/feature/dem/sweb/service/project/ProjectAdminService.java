package com.ly.ttd.biz.feature.dem.sweb.service.project;


import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.project.req.ProjectAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.project.req.ProjectUpdateReq;
import com.ly.ttd.feature.admin.api.dto.ProjectDto;

import java.util.List;

/**
 * 项目服务接口
 *
 * @author yong.li
 * @since 2026-05-16
 */
public interface ProjectAdminService {

    /**
     * 添加项目
     *
     * @param req 项目添加请求
     * @return 是否成功
     */
    void addProject(ProjectAddReq req);

    /**
     * 更新项目
     *
     * @param req 项目更新请求
     * @return 是否成功
     */
    void updateProject(ProjectUpdateReq req);


    /**
     * 查询所有项目
     */
    List<ProjectDto> getAll();


    /**
     * 根据项目ID，获取资源key
     */
    String getResourceKey(Long projectId, String prefix, String key);

    String getPrefix(Long projectId, String prefix);

    /**
     * 删除项目
     */
    void deleteProject(Long projectId) throws BizException;

    /**
     * 添加项目成员
     */
    void addProjectUser(Long projectId, String userAccount) throws BizException;

    /**
     * 删除项目成员
     */
    void deleteProjectUser(Long projectUserId) throws BizException;


    ProjectDto getById(Long projectId);
}
