package com.ly.ttd.biz.admin.srv.dataStruct;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.srv.dataStruct.req.DataStructQueryReq;
import com.ly.ttd.biz.admin.srv.dataStruct.res.DataStructQueryRes;

/**
 * 数据集查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface DataStructQueryService {

    PageResult<DataStructQueryRes> pageQuery(DataStructQueryReq req);
}
