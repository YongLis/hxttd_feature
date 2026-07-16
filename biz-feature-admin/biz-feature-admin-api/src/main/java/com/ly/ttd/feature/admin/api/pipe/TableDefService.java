package com.ly.ttd.feature.admin.api.pipe;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.TableDefDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 数据表定义 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface TableDefService {

    /**
     * 新增数据表定义
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    void add(TableDefDto dto) throws BizException;

    /**
     * 更新数据表定义
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(TableDefDto dto) throws BizException;

    /**
     * 根据ID删除数据表定义
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(String id, String opUser) throws BizException;

    /**
     * 根据ID查询数据表定义
     *
     * @param id 主键ID
     * @return 数据表定义信息
     * @throws BizException 业务异常
     */
    TableDefDto queryById(String id) throws BizException;
}
