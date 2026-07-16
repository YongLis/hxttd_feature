package com.ly.ttd.biz.feature.dem.sweb.service.eventMessage;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.eventMessage.req.FeatureTraceQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.eventMessage.res.FeatureTraceQueryRes;

/**
 * 特征溯源查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface FeatureTraceQueryService {

    PageResult<FeatureTraceQueryRes> pageQuery(FeatureTraceQueryReq req);
}
