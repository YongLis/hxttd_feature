package com.ly.ttd.feature.admin.api.factor;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.DerivativeFactorDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 指标 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface DerivativeFactorService {

    /**
     * 新增指标
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(DerivativeFactorDto dto) throws BizException;

    /**
     * 更新指标
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(DerivativeFactorDto dto) throws BizException;

    /**
     * 根据ID删除指标
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询指标
     *
     * @param id 主键ID
     * @return 指标信息
     * @throws BizException 业务异常
     */
    DerivativeFactorDto queryById(Long id) throws BizException;
}
