package com.ly.ttd.biz.feature.admin.srv.metaField.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.MetaFieldEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.MetaFieldMapper;
import com.ly.ttd.feature.admin.api.dto.MetaFieldDto;
import com.ly.ttd.feature.admin.api.metaField.MetaFieldService;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 元字段 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class MetaFieldServiceImpl implements MetaFieldService {
    @Resource
    private MetaFieldMapper metaFieldMapper;

    @Override
    public Long add(MetaFieldDto dto) throws BizException {
        MetaFieldEntity entity = new MetaFieldEntity();
        BeanUtils.copyProperties(dto, entity);
        metaFieldMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(MetaFieldDto dto) throws BizException {
        MetaFieldEntity entity = new MetaFieldEntity();
        BeanUtils.copyProperties(dto, entity);
        metaFieldMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        metaFieldMapper.deleteById(id);
    }

    @Override
    public MetaFieldDto queryById(Long id) throws BizException {
        MetaFieldEntity entity = metaFieldMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        MetaFieldDto dto = new MetaFieldDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public Map<String, MetaFieldDto> getByProjectId(Long projectId) {
        List<MetaFieldEntity> list = metaFieldMapper.getByProjectId(projectId);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().map(t -> {
                MetaFieldDto dto = new MetaFieldDto();
                BeanUtils.copyProperties(t, dto);
                return dto;
            }).collect(Collectors.toMap(MetaFieldDto::getResourceKey, V -> V));
        }
        return new HashMap<>();
    }
}
