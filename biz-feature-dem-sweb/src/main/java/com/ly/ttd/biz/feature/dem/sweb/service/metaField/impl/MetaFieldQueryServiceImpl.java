package com.ly.ttd.biz.feature.dem.sweb.service.metaField.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.MetaFieldQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.MetaFieldQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.res.MetaFieldQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.MetaFieldEntity;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.MetaFieldMapper;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import com.ly.ttd.feature.common.enums.ObjectTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yong.li
 * @since 2026/5/27 20:49
 */
@Slf4j
@Service
public class MetaFieldQueryServiceImpl implements MetaFieldQueryService {
    @Resource
    private MetaFieldMapper metaFieldMapper;

    @Override
    public PageResult<MetaFieldQueryRes> pageQuery(MetaFieldQueryReq req) {
        PageResult<MetaFieldQueryRes> result = new PageResult<>();
        Page<MetaFieldEntity> page = new Page<>(req.getCurrent(), req.getPageSize());
        List<MetaFieldEntity> records = metaFieldMapper.pageQuery(page, req);
        page.setRecords(records);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            List<MetaFieldQueryRes> list = page.getRecords().stream().map(MetaFieldQueryServiceImpl::convertRes).collect(Collectors.toList());
            result.setData(list);
        }
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setCode(FeatureResultCodeEnum.SUCCESS.getCode());
        return result;
    }

    @Override
    public Map<String, MetaFieldEntity> getByProjectId(Long projectId) {
        List<MetaFieldEntity> list = metaFieldMapper.getByProjectId(projectId);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().collect(Collectors.toMap(MetaFieldEntity::getResourceKey, e -> e));
        }
        return new HashMap<>();
    }

    @Override
    public MetaFieldEntity getById(Long id) {
        return metaFieldMapper.selectById(id);
    }

    @Override
    public List<MetaFieldEntity> getAllByProjectId(Long projectId) {
        return metaFieldMapper.getByProjectId(projectId);
    }

    private static MetaFieldQueryRes convertRes(MetaFieldEntity e) {
        MetaFieldQueryRes res = new MetaFieldQueryRes();
        res.setId(e.getId());
        res.setResourceKey(e.getResourceKey());
        res.setResourceName(e.getResourceName());
        res.setVersion(e.getVersion());
        res.setProjectId(e.getProjectId());
        res.setLanguage(e.getLanguage());
        res.setScript(e.getScript());
        res.setReturnType(e.getReturnType());
        res.setReturnTypeDesc(ObjectTypeEnum.getDefaultValueByCode(e.getReturnType()));
        res.setDefaultValue(e.getDefaultValue());
        res.setExceptionValue(e.getExceptionValue());
        res.setCategoryTag(e.getCategoryTag());
        res.setCrtUser(e.getCrtUser());
        res.setCrtTime(e.getCrtTime());
        return res;
    }
}
