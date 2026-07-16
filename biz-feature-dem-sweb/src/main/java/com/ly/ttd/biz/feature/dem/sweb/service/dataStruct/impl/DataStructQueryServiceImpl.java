package com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.DataStructQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.req.DataStructQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.res.DataStructQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.DataStructEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.DataStructMapper;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 数据集查询服务实现
 *
 * @author yong.li
 * @since 2026-06-23
 */
@Service
public class DataStructQueryServiceImpl implements DataStructQueryService {
    @Resource
    private DataStructMapper dataStructMapper;

    @Override
    public PageResult<DataStructQueryRes> pageQuery(DataStructQueryReq req) {
        PageResult<DataStructQueryRes> result = new PageResult<>();
        Page<DataStructEntity> page = new Page<>(req.getCurrent(), req.getPageSize());
        dataStructMapper.pageQuery(page, req);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            result.setData(page.getRecords().stream().map(e -> {
                DataStructQueryRes res = new DataStructQueryRes();
                res.setId(e.getId());
                res.setResourceKey(e.getResourceKey());
                res.setResourceName(e.getResourceName());
                res.setVersion(e.getVersion());
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
