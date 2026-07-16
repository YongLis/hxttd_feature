package com.ly.ttd.biz.feature.dem.sweb.service.dict;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictCodeQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.res.DictCodeQueryRes;

/**
 * 字典键值查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface DictCodeQueryService {

    PageResult<DictCodeQueryRes> pageQuery(DictCodeQueryReq req);
}
