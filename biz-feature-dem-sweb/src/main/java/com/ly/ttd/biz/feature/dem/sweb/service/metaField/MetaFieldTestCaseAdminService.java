package com.ly.ttd.biz.feature.dem.sweb.service.metaField;


import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.TestCaseAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.TestCaseUpdateReq;

/**
 * 元字段测试用例服务
 *
 * @author yong.li
 * @since 2026-05-16
 */
public interface MetaFieldTestCaseAdminService {

    boolean addTestCase(TestCaseAddReq req);

    boolean updateTestCase(TestCaseUpdateReq req);

    boolean deleteTestCase(Long id);
}
