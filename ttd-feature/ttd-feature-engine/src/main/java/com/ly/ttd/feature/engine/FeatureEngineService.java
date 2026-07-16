package com.ly.ttd.feature.engine;

import com.ly.ttd.feature.api.req.ModelQueryReq;
import com.ly.ttd.feature.api.req.SingleQueryReq;
import com.ly.ttd.feature.api.req.TxnFeatureReq;
import com.ly.ttd.feature.api.res.ExecuteResult;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

/**
 * 特征引擎服务
 *
 * @author yong.li
 * @since 2026/6/16 16:36
 */
public interface FeatureEngineService {

    /**
     * 实时特征写(异步)
     */
    void asyncWrite(@Validated TxnFeatureReq req) throws FeatureBizException;


    /**
     * 单个查询
     */
    ExecuteResult<Object> getSingleValue(@Validated SingleQueryReq req);


    /**
     * 模型查询
     */
    ExecuteResult<Map<String, Object>> getModelValue(@Validated ModelQueryReq req);


}
