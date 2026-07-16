package com.ly.ttd.feature.admin.api.pipe;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.KafkaTopicDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * Kafka Topic RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface KafkaTopicService {

    /**
     * 新增Kafka Topic
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    String add(KafkaTopicDto dto) throws BizException;

    /**
     * 更新Kafka Topic
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(KafkaTopicDto dto) throws BizException;

    /**
     * 根据ID删除Kafka Topic
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询Kafka Topic
     *
     * @param id 主键ID
     * @return Kafka Topic信息
     * @throws BizException 业务异常
     */
    KafkaTopicDto queryById(Long id) throws BizException;
}
