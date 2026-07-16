package com.ly.ttd.biz.feature.dem.sweb.service.account;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.account.req.AccountQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.account.res.AccountQueryRes;

/**
 * 账户查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface AccountQueryService {

    PageResult<AccountQueryRes> pageQuery(AccountQueryReq req);
}
