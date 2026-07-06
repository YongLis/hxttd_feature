package com.ly.ttd.biz.admin.srv.metaField.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.ttd.biz.admin.mybatis.entity.MetaFieldTestCaseEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.MetaFieldTestCaseMapper;
import com.ly.ttd.biz.admin.srv.metaField.MetaFieldTestCaseService;
import com.ly.ttd.biz.admin.srv.metaField.req.TestCaseAddReq;
import com.ly.ttd.biz.admin.srv.metaField.req.TestCaseUpdateReq;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 元字段测试用例服务实现
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Service
public class MetaFieldTestCaseServiceImpl extends ServiceImpl<MetaFieldTestCaseMapper, MetaFieldTestCaseEntity>
        implements MetaFieldTestCaseService {

    @Override
    public boolean addTestCase(TestCaseAddReq req) {
        MetaFieldTestCaseEntity entity = new MetaFieldTestCaseEntity();
        entity.setMetaFieldCode(req.getMetaFieldCode());
        entity.setCaseType(req.getCaseType());
        entity.setBizOrderNo(req.getBizOrderNo());
        entity.setCaseContent(req.getCaseContent());
        entity.setTargetValue(req.getTargetValue());
        entity.setDeleted(false);
        return save(entity);
    }

    @Override
    public boolean updateTestCase(TestCaseUpdateReq req) {
        MetaFieldTestCaseEntity entity = getById(req.getId());
        if (entity == null) {
            throw new RuntimeException("测试用例不存在");
        }
        if (StringUtils.hasText(req.getMetaFieldCode())) {
            entity.setMetaFieldCode(req.getMetaFieldCode());
        }
        if (StringUtils.hasText(req.getCaseType())) {
            entity.setCaseType(req.getCaseType());
        }
        if (StringUtils.hasText(req.getBizOrderNo())) {
            entity.setBizOrderNo(req.getBizOrderNo());
        }
        entity.setCaseContent(req.getCaseContent());
        entity.setTargetValue(req.getTargetValue());
        return updateById(entity);
    }

    @Override
    public boolean deleteTestCase(Long id) {
        MetaFieldTestCaseEntity entity = getById(id);
        if (entity == null) {
            throw new RuntimeException("测试用例不存在");
        }
        entity.setDeleted(true);
        return updateById(entity);
    }
}
