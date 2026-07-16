package com.ly.ttd.feature.admin.api.feature;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.FeatureDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 元数据实体 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface FeatureService {

    /**
     * 新增元数据实体
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(FeatureDto dto) throws BizException;

    /**
     * 更新元数据实体
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(FeatureDto dto) throws BizException;

    /**
     * 根据ID删除元数据实体
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询元数据实体
     *
     * @param id 主键ID
     * @return 元数据实体信息
     * @throws BizException 业务异常
     */
    FeatureDto queryById(Long id) throws BizException;
}
