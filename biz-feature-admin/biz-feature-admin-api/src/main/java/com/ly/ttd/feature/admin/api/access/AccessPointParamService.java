package com.ly.ttd.feature.admin.api.access;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.AccessPointParamDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 接入点参数 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface AccessPointParamService {

    /**
     * 新增接入点参数
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(AccessPointParamDto dto) throws BizException;

    /**
     * 更新接入点参数
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(AccessPointParamDto dto) throws BizException;

    /**
     * 根据ID删除接入点参数
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询接入点参数
     *
     * @param id 主键ID
     * @return 接入点参数信息
     * @throws BizException 业务异常
     */
    AccessPointParamDto queryById(Long id) throws BizException;
}
