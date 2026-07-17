package com.ly.ttd.biz.feature.dem.sweb.service.factor.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.FactorQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.FactorQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.res.FactorQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.FactorEntity;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.FactorMapper;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 指标服务实现
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Service
public class FactorQueryServiceImpl implements FactorQueryService {

    @Resource
    private FactorMapper factorMapper;


    @Override
    public PageResult<FactorQueryRes> pageQuery(FactorQueryReq req) {
        PageResult<FactorQueryRes> result = new PageResult<>();
        Page<FactorEntity> page = new Page<>(req.getCurrent(), req.getPageSize());
        List<FactorEntity> records = factorMapper.pageQuery(page, req);
        page.setRecords(records);

        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            result.setData(page.getRecords().stream().map(t -> {
                FactorQueryRes res = new FactorQueryRes();
                res.setId(t.getId());
                res.setResourceKey(t.getResourceKey());
                res.setResourceName(t.getResourceName());
                res.setVersion(t.getVersion());
                res.setProjectId(t.getProjectId());
                res.setFactorType(t.getFactorType());
                res.setReturnType(t.getReturnType());
                res.setDefaultValue(t.getDefaultValue());
                res.setExceptionValue(t.getExceptionValue());
                res.setTimeout(t.getTimeout());
                res.setCrtUser(t.getCrtUser());
                res.setCrtTime(t.getCrtTime());
                res.setResourceJson(t.getResourceJson());
                return res;
            }).collect(Collectors.toList()));
        }
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setCode(FeatureResultCodeEnum.SUCCESS.getCode());
        return result;
    }
}
