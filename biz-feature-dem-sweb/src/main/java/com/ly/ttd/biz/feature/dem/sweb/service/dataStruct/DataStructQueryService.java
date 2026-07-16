package com.ly.ttd.biz.feature.dem.sweb.service.dataStruct;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.req.DataStructQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.res.DataStructQueryRes;

/**
 * 数据集查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface DataStructQueryService {

    PageResult<DataStructQueryRes> pageQuery(DataStructQueryReq req);
}
