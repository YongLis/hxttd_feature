package com.ly.ttd.biz.admin.srv.factor;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.srv.factor.req.FactorQueryReq;
import com.ly.ttd.biz.admin.srv.factor.res.FactorQueryRes;

/**
 * 指标服务
 *
 * @author yong.li
 * @since 2026-05-16
 */
public interface FactorQueryService {


    PageResult<FactorQueryRes> pageQuery(FactorQueryReq req);

}
