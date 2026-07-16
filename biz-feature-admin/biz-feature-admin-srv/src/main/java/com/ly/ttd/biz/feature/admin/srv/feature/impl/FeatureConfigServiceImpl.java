package com.ly.ttd.biz.feature.admin.srv.feature.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.FeatureConfigEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.FeatureConfigMapper;
import com.ly.ttd.feature.admin.api.dto.FeatureConfigDto;
import com.ly.ttd.feature.admin.api.feature.FeatureConfigService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 特征配置 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class FeatureConfigServiceImpl implements FeatureConfigService {
    @Resource
    private FeatureConfigMapper featureConfigMapper;

    @Override
    public Long add(FeatureConfigDto dto) throws BizException {
        FeatureConfigEntity entity = new FeatureConfigEntity();
        BeanUtils.copyProperties(dto, entity);
        featureConfigMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(FeatureConfigDto dto) throws BizException {
        FeatureConfigEntity entity = new FeatureConfigEntity();
        BeanUtils.copyProperties(dto, entity);
        featureConfigMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        featureConfigMapper.deleteById(id);
    }

    @Override
    public FeatureConfigDto queryById(Long id) throws BizException {
        FeatureConfigEntity entity = featureConfigMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        FeatureConfigDto dto = new FeatureConfigDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
