package com.ly.ttd.feature.admin.api.connector;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.JdbcConnectorDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 连接器 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface JdbcConnectorService {

    /**
     * 新增连接器
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(JdbcConnectorDto dto) throws BizException;

    /**
     * 更新连接器
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(JdbcConnectorDto dto) throws BizException;

    /**
     * 根据ID删除连接器
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询连接器
     *
     * @param id 主键ID
     * @return 连接器信息
     * @throws BizException 业务异常
     */
    JdbcConnectorDto queryById(Long id) throws BizException;
}
