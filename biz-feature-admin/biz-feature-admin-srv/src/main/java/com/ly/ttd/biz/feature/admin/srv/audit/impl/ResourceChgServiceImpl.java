package com.ly.ttd.biz.feature.admin.srv.audit.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.ResourceChgEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.ResourceChgMapper;
import com.ly.ttd.feature.admin.api.audit.ResourceChgService;
import com.ly.ttd.feature.admin.api.dto.ResourceChgDto;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 资源变更历史 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class ResourceChgServiceImpl implements ResourceChgService {
    @Resource
    private ResourceChgMapper resourceChgMapper;

    @Override
    public Long add(ResourceChgDto dto) throws BizException {
        ResourceChgEntity entity = new ResourceChgEntity();
        BeanUtils.copyProperties(dto, entity);
        resourceChgMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(ResourceChgDto dto) throws BizException {
        ResourceChgEntity entity = new ResourceChgEntity();
        BeanUtils.copyProperties(dto, entity);
        resourceChgMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        resourceChgMapper.deleteById(id);
    }

    @Override
    public ResourceChgDto queryById(Long id) throws BizException {
        ResourceChgEntity entity = resourceChgMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        ResourceChgDto dto = new ResourceChgDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
