package com.ly.ttd.biz.feature.admin.srv.feature.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.FeatureView;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.FeatureViewMapper;
import com.ly.ttd.feature.admin.api.dto.FeatureViewDto;
import com.ly.ttd.feature.admin.api.feature.FeatureViewService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 特征视图 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class FeatureViewServiceImpl implements FeatureViewService {
    @Resource
    private FeatureViewMapper featureViewMapper;

    @Override
    public Long add(FeatureViewDto dto) throws BizException {
        FeatureView entity = new FeatureView();
        BeanUtils.copyProperties(dto, entity);
        featureViewMapper.insert(entity);
        return entity.getViewId();
    }

    @Override
    public void update(FeatureViewDto dto) throws BizException {
        FeatureView entity = new FeatureView();
        BeanUtils.copyProperties(dto, entity);
        featureViewMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        featureViewMapper.deleteById(id);
    }

    @Override
    public FeatureViewDto queryById(Long id) throws BizException {
        FeatureView entity = featureViewMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        FeatureViewDto dto = new FeatureViewDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
