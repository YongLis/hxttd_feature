package com.ly.ttd.biz.feature.dem.sweb.service.metaField.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.MetaFieldTestCaseQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.TestCaseQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.res.TestCaseQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.MetaFieldTestCaseEntity;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.MetaFieldTestCaseMapper;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yong.li
 * @since 2026/7/14 13:04
 */
@Service
public class MetaFieldTestCaseQueryServiceImpl implements MetaFieldTestCaseQueryService {
    @Resource
    private MetaFieldTestCaseMapper testCaseMapper;

    @Override
    public PageResult<TestCaseQueryRes> pageQuery(TestCaseQueryReq req) {
        PageResult<TestCaseQueryRes> result = new PageResult<>();
        Page<MetaFieldTestCaseEntity> page = new Page<>(req.getCurrent(), req.getPageSize());
        List<MetaFieldTestCaseEntity> records = testCaseMapper.pageQuery(page, req);
        page.setRecords(records);
        if (CollectionUtils.isNotEmpty(records)) {
            List<TestCaseQueryRes> list = page.getRecords().stream().map(MetaFieldTestCaseQueryServiceImpl::convertRes).collect(Collectors.toList());
            result.setData(list);
        }
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setCode(FeatureResultCodeEnum.SUCCESS.getCode());
        return result;
    }

    private static TestCaseQueryRes convertRes(MetaFieldTestCaseEntity e) {
        TestCaseQueryRes res = new TestCaseQueryRes();
        res.setId(e.getId());
        res.setMetaFieldCode(e.getMetaFieldCode());
        res.setCaseType(e.getCaseType());
        res.setBizOrderNo(e.getBizOrderNo());
        res.setCaseContent(e.getCaseContent());
        res.setTargetValue(e.getTargetValue());

        res.setCrtUser(e.getCrtUser());
        res.setCrtTime(e.getCrtTime());
        return res;
    }
}
