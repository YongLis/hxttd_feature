package com.ly.ttd.biz.feature.dem.sweb.service.metaField;


import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.TestCaseQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.res.TestCaseQueryRes;

/**
 * 元字段测试用例服务
 *
 * @author yong.li
 * @since 2026-05-16
 */
public interface MetaFieldTestCaseQueryService {

    PageResult<TestCaseQueryRes> pageQuery(TestCaseQueryReq req);
}
