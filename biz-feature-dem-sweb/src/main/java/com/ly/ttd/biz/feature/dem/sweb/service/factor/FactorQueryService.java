package com.ly.ttd.biz.feature.dem.sweb.service.factor;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.FactorQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.res.FactorQueryRes;

/**
 * 指标服务
 *
 * @author yong.li
 * @since 2026-05-16
 */
public interface FactorQueryService {


    PageResult<FactorQueryRes> pageQuery(FactorQueryReq req);

}
