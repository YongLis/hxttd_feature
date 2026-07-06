package com.ly.ttd.biz.admin.srv.dict.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.mybatis.entity.DictEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.DictMapper;
import com.ly.ttd.biz.admin.srv.dict.DictQueryService;
import com.ly.ttd.biz.admin.srv.dict.req.DictQueryReq;
import com.ly.ttd.biz.admin.srv.dict.res.DictQueryRes;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

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
        dictMapper.pageQuery(page, req);
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
