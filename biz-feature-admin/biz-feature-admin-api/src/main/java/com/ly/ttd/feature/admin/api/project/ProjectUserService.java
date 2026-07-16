package com.ly.ttd.feature.admin.api.project;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.ProjectUserDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 项目用户 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface ProjectUserService {

    /**
     * 新增项目用户
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(ProjectUserDto dto) throws BizException;

    /**
     * 更新项目用户
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(ProjectUserDto dto) throws BizException;

    /**
     * 根据ID删除项目用户
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询项目用户
     *
     * @param id 主键ID
     * @return 项目用户信息
     * @throws BizException 业务异常
     */
    ProjectUserDto queryById(Long id) throws BizException;
}
