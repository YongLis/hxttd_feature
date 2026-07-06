package com.ly.ttd.feature.engine;

import com.ly.ttd.consts.exception.BizException;
import com.ly.ttd.feature.engine.req.ModelQueryReq;
import com.ly.ttd.feature.engine.req.SingleQueryReq;
import com.ly.ttd.feature.engine.req.TxnFeatureReq;
import com.ly.ttd.feature.engine.res.ExecuteResult;
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
    void asyncWrite(@Validated TxnFeatureReq req) throws BizException;


    /**
     * 单个查询
     */
    ExecuteResult<Object> getSingleValue(@Validated SingleQueryReq req);


    /**
     * 模型查询
     */
    ExecuteResult<Map<String, Object>> getModelValue(@Validated ModelQueryReq req);


}
