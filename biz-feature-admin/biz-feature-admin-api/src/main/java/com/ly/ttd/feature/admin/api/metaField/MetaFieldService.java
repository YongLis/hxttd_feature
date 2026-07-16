package com.ly.ttd.feature.admin.api.metaField;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.MetaFieldDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

import java.util.Map;

/**
 * 元字段 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface MetaFieldService {
    /**
     * 新增元字段
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(MetaFieldDto dto) throws BizException;

    /**
     * 更新元字段
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(MetaFieldDto dto) throws BizException;

    /**
     * 根据ID删除元字段
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询元字段
     *
     * @param id 主键ID
     * @return 元字段信息
     * @throws BizException 业务异常
     */
    MetaFieldDto queryById(Long id) throws BizException;

    Map<String, MetaFieldDto> getByProjectId(Long projectId);
}
