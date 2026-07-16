package com.ly.ttd.biz.feature.admin.srv.feature.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.FeatureDef;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.FeatureDefMapper;
import com.ly.ttd.feature.admin.api.dto.FeatureDefDto;
import com.ly.ttd.feature.admin.api.feature.FeatureDefService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 特征定义 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class FeatureDefServiceImpl implements FeatureDefService {
    @Resource
    private FeatureDefMapper featureDefMapper;

    @Override
    public Long add(FeatureDefDto dto) throws BizException {
        FeatureDef entity = new FeatureDef();
        BeanUtils.copyProperties(dto, entity);
        featureDefMapper.insert(entity);
        return entity.getDefId();
    }

    @Override
    public void update(FeatureDefDto dto) throws BizException {
        FeatureDef entity = new FeatureDef();
        BeanUtils.copyProperties(dto, entity);
        featureDefMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        featureDefMapper.deleteById(id);
    }

    @Override
    public FeatureDefDto queryById(Long id) throws BizException {
        FeatureDef entity = featureDefMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        FeatureDefDto dto = new FeatureDefDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
