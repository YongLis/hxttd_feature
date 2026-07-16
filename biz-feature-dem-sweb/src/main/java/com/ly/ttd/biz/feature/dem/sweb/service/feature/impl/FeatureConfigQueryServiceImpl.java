package com.ly.ttd.biz.feature.dem.sweb.service.feature.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.FeatureConfigQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.req.FeatureConfigQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.res.FeatureConfigListRes;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.res.FeatureConfigQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.FeatureConfigEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.FeatureConfigMapper;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yong.li
 * @since 2026/6/2 22:36
 */
@Service
public class FeatureConfigQueryServiceImpl implements FeatureConfigQueryService {
    @Resource
    private FeatureConfigMapper featureConfigMapper;

    @Override
    public PageResult<FeatureConfigQueryRes> pageQuery(FeatureConfigQueryReq req) {
        PageResult<FeatureConfigQueryRes> result = new PageResult<>();
        Page<FeatureConfigEntity> page = new Page<>(req.getCurrent(), req.getPageSize());
        featureConfigMapper.pageQuery(page, req);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            result.setData(page.getRecords().stream().map(e -> {
                FeatureConfigQueryRes res = new FeatureConfigQueryRes();
                res.setId(e.getId());
                res.setResourceKey(e.getResourceKey());
                res.setResourceName(e.getResourceName());
                res.setVersion(e.getVersion());
                res.setProjectId(e.getProjectId());
                res.setFeatureCode(e.getFeatureCode());
                res.setLanguage(e.getLanguage());
                res.setReturnType(e.getReturnType());
                res.setValueType(e.getValueType());
                res.setConditionScript(e.getConditionScript());
                res.setMainDimScript(e.getMainDimScript());
                res.setSlaveDimScript(e.getSlaveDimScript());
                res.setMetaFields(e.getMetaFields());
                res.setValueScript(e.getValueScript());
                res.setFixValue(e.getFixValue());
                res.setAggregateMode(e.getAggregateMode());
                res.setTimeMode(e.getTimeMode());
                res.setTimeUnit(e.getTimeUnit());
                res.setTimeWindow(e.getTimeWindow());
                res.setCrtUser(e.getCrtUser());
                res.setCrtTime(e.getCrtTime());
                return res;
            }).collect(Collectors.toList()));
        }
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setCode(FeatureResultCodeEnum.SUCCESS.getCode());
        return result;
    }

    @Override
    public FeatureConfigEntity getByResourceKey(String resourceKey) {
        return featureConfigMapper.getByResourceKey(resourceKey);
    }

    @Override
    public List<FeatureConfigEntity> getByProjectId(Long projectId) {
        return featureConfigMapper.selectByProjectId(projectId);
    }

    @Override
    public List<FeatureConfigListRes> getListByProjectId(Long projectId) {
        return featureConfigMapper.getListByProjectId(projectId);
    }

    @Override
    public FeatureConfigEntity getById(Long id) {
        return featureConfigMapper.selectById(id);
    }
}
