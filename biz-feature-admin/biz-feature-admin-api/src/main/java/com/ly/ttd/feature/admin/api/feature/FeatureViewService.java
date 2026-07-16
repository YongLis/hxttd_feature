package com.ly.ttd.feature.admin.api.feature;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.FeatureViewDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 特征视图 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface FeatureViewService {

    /**
     * 新增特征视图
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(FeatureViewDto dto) throws BizException;

    /**
     * 更新特征视图
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(FeatureViewDto dto) throws BizException;

    /**
     * 根据ID删除特征视图
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询特征视图
     *
     * @param id 主键ID
     * @return 特征视图信息
     * @throws BizException 业务异常
     */
    FeatureViewDto queryById(Long id) throws BizException;
}
