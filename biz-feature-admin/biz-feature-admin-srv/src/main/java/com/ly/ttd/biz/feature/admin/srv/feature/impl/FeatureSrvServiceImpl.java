package com.ly.ttd.biz.feature.admin.srv.feature.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.FeatureService;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.FeatureServiceMapper;
import com.ly.ttd.feature.admin.api.dto.FeatureServiceDto;
import com.ly.ttd.feature.admin.api.feature.FeatureSrvService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 特征服务 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class FeatureSrvServiceImpl implements FeatureSrvService {
    @Resource
    private FeatureServiceMapper featureServiceMapper;

    @Override
    public Long add(FeatureServiceDto dto) throws BizException {
        FeatureService entity = new FeatureService();
        BeanUtils.copyProperties(dto, entity);
        featureServiceMapper.insert(entity);
        return entity.getServiceId();
    }

    @Override
    public void update(FeatureServiceDto dto) throws BizException {
        FeatureService entity = new FeatureService();
        BeanUtils.copyProperties(dto, entity);
        featureServiceMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        featureServiceMapper.deleteById(id);
    }

    @Override
    public FeatureServiceDto queryById(Long id) throws BizException {
        FeatureService entity = featureServiceMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        FeatureServiceDto dto = new FeatureServiceDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
