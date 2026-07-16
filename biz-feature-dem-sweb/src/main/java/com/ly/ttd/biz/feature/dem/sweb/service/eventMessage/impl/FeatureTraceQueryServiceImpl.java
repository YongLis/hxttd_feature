package com.ly.ttd.biz.feature.dem.sweb.service.eventMessage.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.eventMessage.FeatureTraceQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.eventMessage.req.FeatureTraceQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.eventMessage.res.FeatureTraceQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.EventMessageEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.EventMessageMapper;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 特征溯源查询服务实现
 *
 * @author yong.li
 * @since 2026-06-23
 */
@Service
public class FeatureTraceQueryServiceImpl implements FeatureTraceQueryService {
    @Resource
    private EventMessageMapper eventMessageMapper;

    @Override
    public PageResult<FeatureTraceQueryRes> pageQuery(FeatureTraceQueryReq req) {
        PageResult<FeatureTraceQueryRes> result = new PageResult<>();
        Page<EventMessageEntity> page = new Page<>(req.getCurrent(), req.getPageSize());
        eventMessageMapper.pageQuery(page, req);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            result.setData(page.getRecords().stream().map(e -> {
                FeatureTraceQueryRes res = new FeatureTraceQueryRes();
                res.setId(e.getId());
                res.setEventId(e.getEventId());
                res.setEventType(e.getEventType());
                res.setOperationType(e.getOperationType());
                res.setBody(e.getBody());
                res.setStatus(e.getStatus());
                res.setRetryCount(e.getRetryCount());
                res.setErrorMessage(e.getErrorMessage());
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
}
