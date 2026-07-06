package com.ly.ttd.biz.admin.srv.account;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.srv.account.req.AccountQueryReq;
import com.ly.ttd.biz.admin.srv.account.res.AccountQueryRes;

/**
 * 账户查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface AccountQueryService {

    PageResult<AccountQueryRes> pageQuery(AccountQueryReq req);
}
