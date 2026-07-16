package com.ly.ttd.feature.admin.api.eventMessage;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.EventMessageDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 事件消息 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface EventMessageService {

    /**
     * 新增事件消息
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(EventMessageDto dto) throws BizException;

    /**
     * 更新事件消息
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(EventMessageDto dto) throws BizException;

    /**
     * 根据ID删除事件消息
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询事件消息
     *
     * @param id 主键ID
     * @return 事件消息信息
     * @throws BizException 业务异常
     */
    EventMessageDto queryById(Long id) throws BizException;
}
