package com.ly.ttd.feature.admin.api.feature;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.FeatureServiceDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 特征服务 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface FeatureSrvService {

    /**
     * 新增特征服务
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(FeatureServiceDto dto) throws BizException;

    /**
     * 更新特征服务
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(FeatureServiceDto dto) throws BizException;

    /**
     * 根据ID删除特征服务
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询特征服务
     *
     * @param id 主键ID
     * @return 特征服务信息
     * @throws BizException 业务异常
     */
    FeatureServiceDto queryById(Long id) throws BizException;
}
