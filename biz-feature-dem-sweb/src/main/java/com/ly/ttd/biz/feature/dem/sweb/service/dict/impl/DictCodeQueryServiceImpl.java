package com.ly.ttd.biz.feature.dem.sweb.service.dict.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.DictCodeQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictCodeQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.res.DictCodeQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.DictCodeEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.DictCodeMapper;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 字典键值查询服务实现
 *
 * @author yong.li
 * @since 2026-06-23
 */
@Service
public class DictCodeQueryServiceImpl implements DictCodeQueryService {
    @Resource
    private DictCodeMapper dictCodeMapper;

    @Override
    public PageResult<DictCodeQueryRes> pageQuery(DictCodeQueryReq req) {
        PageResult<DictCodeQueryRes> result = new PageResult<>();
        Page<DictCodeEntity> page = new Page<>(req.getCurrent(), req.getPageSize());
        dictCodeMapper.pageQuery(page, req);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            result.setData(page.getRecords().stream().map(e -> {
                DictCodeQueryRes res = new DictCodeQueryRes();
                res.setId(e.getId());
                res.setDictId(e.getDictId());
                res.setDictKey(e.getDictKey());
                res.setDictValue(e.getDictValue());
                res.setDeleted(e.getDeleted());
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
