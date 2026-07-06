package com.ly.ttd.biz.admin.srv.metaField;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.ttd.biz.admin.mybatis.entity.MetaFieldTestCaseEntity;
import com.ly.ttd.biz.admin.srv.metaField.req.TestCaseAddReq;
import com.ly.ttd.biz.admin.srv.metaField.req.TestCaseUpdateReq;

/**
 * 元字段测试用例服务
 *
 * @author yong.li
 * @since 2026-05-16
 */
public interface MetaFieldTestCaseService extends IService<MetaFieldTestCaseEntity> {

    boolean addTestCase(TestCaseAddReq req);

    boolean updateTestCase(TestCaseUpdateReq req);

    boolean deleteTestCase(Long id);
}
