package com.ly.ttd.feature.admin.api.dataStruct;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.DataFieldDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 字段 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface DataFieldService {

    /**
     * 新增字段
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(DataFieldDto dto) throws BizException;

    /**
     * 更新字段
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(DataFieldDto dto) throws BizException;

    /**
     * 根据ID删除字段
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询字段
     *
     * @param id 主键ID
     * @return 字段信息
     * @throws BizException 业务异常
     */
    DataFieldDto queryById(Long id) throws BizException;
}
