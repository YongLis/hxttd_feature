package com.ly.ttd.biz.feature.dem.sweb.service.feature;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.req.FeatureConfigAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.req.FeatureConfigUpdateReq;

/**
 * 特征配置 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
public interface FeatureConfigAdminService {

    /**
     * 新增特征配置
     *
     * @param req 请求参数
     * @throws BizException 业务异常
     */
    void add(FeatureConfigAddReq req) throws BizException;

    /**
     * 更新特征配置
     *
     * @param req 请求参数
     * @throws BizException 业务异常
     */
    void update(FeatureConfigUpdateReq req) throws BizException;

    /**
     * 根据ID删除特征配置
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

}
