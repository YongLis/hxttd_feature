package com.ly.ttd.biz.admin.srv.eventMessage;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.srv.eventMessage.req.FeatureTraceQueryReq;
import com.ly.ttd.biz.admin.srv.eventMessage.res.FeatureTraceQueryRes;

/**
 * 特征溯源查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface FeatureTraceQueryService {

    PageResult<FeatureTraceQueryRes> pageQuery(FeatureTraceQueryReq req);
}
