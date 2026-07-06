package com.ly.ttd.biz.admin.srv.dict;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.srv.dict.req.DictCodeQueryReq;
import com.ly.ttd.biz.admin.srv.dict.res.DictCodeQueryRes;

/**
 * 字典键值查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface DictCodeQueryService {

    PageResult<DictCodeQueryRes> pageQuery(DictCodeQueryReq req);
}
