package com.ly.ttd.biz.feature.dem.sweb.service.dict.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.DictQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.res.DictQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.DictEntity;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.DictMapper;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典查询服务实现
 *
 * @author yong.li
 * @since 2026-06-23
 */
@Service
public class DictQueryServiceImpl implements DictQueryService {
    @Resource
    private DictMapper dictMapper;

    @Override
    public PageResult<DictQueryRes> pageQuery(DictQueryReq req) {
        PageResult<DictQueryRes> result = new PageResult<>();
        Page<DictEntity> page = new Page<>(req.getCurrent(), req.getPageSize());
        List<DictEntity> records = dictMapper.pageQuery(page, req);
        page.setRecords(records);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            result.setData(page.getRecords().stream().map(e -> {
                DictQueryRes res = new DictQueryRes();
                res.setId(e.getId());
                res.setSystemCode(e.getSystemCode());
                res.setDictCode(e.getDictCode());
                res.setDictName(e.getDictName());
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
