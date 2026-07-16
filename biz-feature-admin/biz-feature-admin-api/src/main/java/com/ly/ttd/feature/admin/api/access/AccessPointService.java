package com.ly.ttd.feature.admin.api.access;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.AccessPointDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 接入点 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface AccessPointService {

    /**
     * 新增接入点
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    void add(AccessPointDto dto) throws BizException;

    /**
     * 更新接入点
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(AccessPointDto dto) throws BizException;

    /**
     * 根据ID删除接入点
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询接入点
     *
     * @param id 主键ID
     * @return 接入点信息
     * @throws BizException 业务异常
     */
    AccessPointDto queryById(Long id) throws BizException;
}
