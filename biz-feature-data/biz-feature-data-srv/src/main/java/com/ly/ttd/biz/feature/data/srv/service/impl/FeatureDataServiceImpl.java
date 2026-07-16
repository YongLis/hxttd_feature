package com.ly.ttd.biz.feature.data.srv.service.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.data.api.FeatureDataService;
import com.ly.ttd.feature.api.req.ModelQueryReq;
import com.ly.ttd.feature.api.req.SingleQueryReq;
import com.ly.ttd.feature.api.req.TxnFeatureReq;
import com.ly.ttd.feature.api.res.ExecuteResult;
import com.ly.ttd.feature.engine.FeatureEngineService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yong.li
 * @since 2026/7/10 09:07
 */
@Service
@Slf4j
public class FeatureDataServiceImpl implements FeatureDataService {

    @Resource
    private FeatureEngineService featureEngineService;

    @Override
    public void write(TxnFeatureReq req) throws BizException {
        featureEngineService.asyncWrite(req);
    }

    @Override
    public ExecuteResult<Object> getSingleValue(SingleQueryReq req) {
        return featureEngineService.getSingleValue(req);
    }

    @Override
    public ExecuteResult<Map<String, Object>> getModelValue(ModelQueryReq req) {
        return featureEngineService.getModelValue(req);
    }
}
