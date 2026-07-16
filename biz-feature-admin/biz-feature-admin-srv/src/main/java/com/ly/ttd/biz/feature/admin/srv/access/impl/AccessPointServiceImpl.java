package com.ly.ttd.biz.feature.admin.srv.access.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.AccessPointEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.AccessPointMapper;
import com.ly.ttd.biz.feature.admin.srv.resource.ResourceOpFactory;
import com.ly.ttd.feature.admin.api.access.AccessPointService;
import com.ly.ttd.feature.admin.api.dto.AccessPointDto;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 接入点 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class AccessPointServiceImpl implements AccessPointService {
    @Resource
    private AccessPointMapper accessPointMapper;

    @Override
    public void add(AccessPointDto dto) throws BizException {
        ResourceOpFactory.getService(FeatureResourceType.POINT.getType())
                .add(dto);
    }

    @Override
    public void update(AccessPointDto dto) throws BizException {
        ResourceOpFactory.getService(FeatureResourceType.POINT.getType())
                .update(dto);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        ResourceOpFactory.getService(FeatureResourceType.POINT.getType())
                .delete(id, opUser);
    }

    @Override
    public AccessPointDto queryById(Long id) throws BizException {
        AccessPointEntity entity = accessPointMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        AccessPointDto dto = new AccessPointDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
