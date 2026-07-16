package com.ly.ttd.biz.feature.admin.srv.feature.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.FeatureDataStore;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.FeatureDataStoreMapper;
import com.ly.ttd.feature.admin.api.dto.FeatureDataStoreDto;
import com.ly.ttd.feature.admin.api.feature.FeatureDataStoreService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 特征数据存储 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class FeatureDataStoreServiceImpl implements FeatureDataStoreService {
    @Resource
    private FeatureDataStoreMapper featureDataStoreMapper;

    @Override
    public Long add(FeatureDataStoreDto dto) throws BizException {
        FeatureDataStore entity = new FeatureDataStore();
        BeanUtils.copyProperties(dto, entity);
        featureDataStoreMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(FeatureDataStoreDto dto) throws BizException {
        FeatureDataStore entity = new FeatureDataStore();
        BeanUtils.copyProperties(dto, entity);
        featureDataStoreMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        featureDataStoreMapper.deleteById(id);
    }

    @Override
    public FeatureDataStoreDto queryById(Long id) throws BizException {
        FeatureDataStore entity = featureDataStoreMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        FeatureDataStoreDto dto = new FeatureDataStoreDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
