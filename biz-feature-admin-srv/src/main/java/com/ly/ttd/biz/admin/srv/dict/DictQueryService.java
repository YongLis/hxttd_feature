package com.ly.ttd.biz.admin.srv.dict;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.srv.dict.req.DictQueryReq;
import com.ly.ttd.biz.admin.srv.dict.res.DictQueryRes;

/**
 * 字典查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface DictQueryService {

    PageResult<DictQueryRes> pageQuery(DictQueryReq req);
}
