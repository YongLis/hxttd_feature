package com.ly.ttd.biz.admin.srv.sequence.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.mybatis.entity.SequenceEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.SequenceMapper;
import com.ly.ttd.biz.admin.srv.sequence.SequenceQueryService;
import com.ly.ttd.biz.admin.srv.sequence.req.SequenceQueryReq;
import com.ly.ttd.biz.admin.srv.sequence.res.SequenceQueryRes;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 序列查询服务实现
 *
 * @author yong.li
 * @since 2026-06-23
 */
@Service
public class SequenceQueryServiceImpl implements SequenceQueryService {
    @Resource
    private SequenceMapper sequenceMapper;

    @Override
    public PageResult<SequenceQueryRes> pageQuery(SequenceQueryReq req) {
        PageResult<SequenceQueryRes> result = new PageResult<>();
        Page<SequenceEntity> page = new Page<>(req.getCurrent(), req.getPageSize());
        sequenceMapper.pageQuery(page, req);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            result.setData(page.getRecords().stream().map(e -> {
                SequenceQueryRes res = new SequenceQueryRes();
                res.setId(e.getId());
                res.setSeqCode(e.getSeqCode());
                res.setSeqName(e.getSeqName());
                res.setVal(e.getVal());
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
