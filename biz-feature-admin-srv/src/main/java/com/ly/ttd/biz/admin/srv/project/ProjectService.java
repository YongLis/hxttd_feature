package com.ly.ttd.biz.admin.srv.project;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.ttd.biz.admin.mybatis.entity.ProjectEntity;
import com.ly.ttd.biz.admin.srv.project.req.ProjectAddReq;
import com.ly.ttd.biz.admin.srv.project.req.ProjectUpdateReq;
import com.ly.ttd.feature.common.exception.FeatureBizException;

import java.util.List;

/**
 * 项目服务接口
 *
 * @author yong.li
 * @since 2026-05-16
 */
public interface ProjectService extends IService<ProjectEntity> {

    /**
     * 添加项目
     *
     * @param req 项目添加请求
     * @return 是否成功
     */
    Boolean addProject(ProjectAddReq req);

    /**
     * 更新项目
     *
     * @param req 项目更新请求
     * @return 是否成功
     */
    Boolean updateProject(ProjectUpdateReq req);


    /**
     * 查询所有项目
     */
    List<ProjectEntity> getAll();


    /**
     * 根据项目ID，获取资源key
     */
    String getResourceKey(Long projectId, String prefix, String key);

    String getPrefix(Long projectId, String prefix);

    /**
     * 删除项目
     */
    Boolean deleteProject(Long projectId) throws FeatureBizException;

    /**
     * 添加项目成员
     */
    void addProjectUser(Long projectId, String userAccount) throws FeatureBizException;

    /**
     * 删除项目成员
     */
    void deleteProjectUser(Long projectId, String userAccount) throws FeatureBizException;


}
