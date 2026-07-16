package com.ly.ttd.biz.feature.data.api;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.api.req.ModelQueryReq;
import com.ly.ttd.feature.api.req.SingleQueryReq;
import com.ly.ttd.feature.api.req.TxnFeatureReq;
import com.ly.ttd.feature.api.res.ExecuteResult;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

import java.util.Map;

/**
 * 特征数据服务
 *
 * @author yong.li
 * @since 2026/7/9 21:12
 */
@RpcService(serviceName = "biz-feature-data-srv")
public interface FeatureDataService {


    /**
     * 实时特征写入(异步)
     */
    void write(TxnFeatureReq req) throws BizException;


    /**
     * 单个查询
     */
    ExecuteResult<Object> getSingleValue(SingleQueryReq req);

    /**
     * 模型查询
     */
    ExecuteResult<Map<String, Object>> getModelValue(ModelQueryReq req);

}
