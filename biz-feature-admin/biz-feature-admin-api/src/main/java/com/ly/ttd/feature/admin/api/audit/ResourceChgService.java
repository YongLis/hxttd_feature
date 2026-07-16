package com.ly.ttd.feature.admin.api.audit;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.ResourceChgDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 资源版本变更记录 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface ResourceChgService {

    /**
     * 新增资源版本变更记录
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(ResourceChgDto dto) throws BizException;

    /**
     * 更新资源版本变更记录
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(ResourceChgDto dto) throws BizException;

    /**
     * 根据ID删除资源版本变更记录
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询资源版本变更记录
     *
     * @param id 主键ID
     * @return 资源版本变更记录信息
     * @throws BizException 业务异常
     */
    ResourceChgDto queryById(Long id) throws BizException;
}
