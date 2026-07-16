package com.ly.ttd.biz.feature.admin.srv.feature.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.FeatureEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.FeatureEntityMapper;
import com.ly.ttd.feature.admin.api.dto.FeatureDto;
import com.ly.ttd.feature.admin.api.feature.FeatureService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 元数据实体 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class FeatureServiceImpl implements FeatureService {
    @Resource
    private FeatureEntityMapper featureEntityMapper;

    @Override
    public Long add(FeatureDto dto) throws BizException {
        FeatureEntity entity = new FeatureEntity();
        BeanUtils.copyProperties(dto, entity);
        featureEntityMapper.insert(entity);
        return entity.getEntityId();
    }

    @Override
    public void update(FeatureDto dto) throws BizException {
        FeatureEntity entity = new FeatureEntity();
        BeanUtils.copyProperties(dto, entity);
        featureEntityMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        featureEntityMapper.deleteById(id);
    }

    @Override
    public FeatureDto queryById(Long id) throws BizException {
        FeatureEntity entity = featureEntityMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        FeatureDto dto = new FeatureDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
