package com.ly.ttd.biz.feature.admin.srv.access.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.AccessPointParamEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.AccessPointParamMapper;
import com.ly.ttd.feature.admin.api.access.AccessPointParamService;
import com.ly.ttd.feature.admin.api.dto.AccessPointParamDto;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 接入点参数 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class AccessPointParamServiceImpl implements AccessPointParamService {
    @Resource
    private AccessPointParamMapper accessPointParamMapper;

    @Override
    public Long add(AccessPointParamDto dto) throws BizException {
        AccessPointParamEntity entity = new AccessPointParamEntity();
        BeanUtils.copyProperties(dto, entity);
        accessPointParamMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(AccessPointParamDto dto) throws BizException {
        AccessPointParamEntity entity = new AccessPointParamEntity();
        BeanUtils.copyProperties(dto, entity);
        accessPointParamMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        accessPointParamMapper.deleteById(id);
    }

    @Override
    public AccessPointParamDto queryById(Long id) throws BizException {
        AccessPointParamEntity entity = accessPointParamMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        AccessPointParamDto dto = new AccessPointParamDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
