package com.ly.ttd.biz.feature.dem.sweb.service.metaField.impl;

import com.ly.ttd.biz.feature.dem.sweb.service.metaField.MetaFieldTestCaseAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.TestCaseAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.TestCaseUpdateReq;
import org.springframework.stereotype.Service;

/**
 * 元字段测试用例服务实现
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Service
public class MetaFieldTestCaseAdminServiceImpl implements MetaFieldTestCaseAdminService {

    @Override
    public boolean addTestCase(TestCaseAddReq req) {
        return false;
    }

    @Override
    public boolean updateTestCase(TestCaseUpdateReq req) {
        return false;
    }

    @Override
    public boolean deleteTestCase(Long id) {
        return false;
    }
}
