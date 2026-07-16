package com.ly.ttd.biz.feature.dem.sweb.service.dict;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.res.DictQueryRes;

/**
 * 字典查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface DictQueryService {

    PageResult<DictQueryRes> pageQuery(DictQueryReq req);
}
