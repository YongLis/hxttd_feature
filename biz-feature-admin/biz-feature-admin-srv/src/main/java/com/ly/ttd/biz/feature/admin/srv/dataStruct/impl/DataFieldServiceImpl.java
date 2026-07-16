package com.ly.ttd.biz.feature.admin.srv.dataStruct.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.DataFieldEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.DataStructFieldMapper;
import com.ly.ttd.feature.admin.api.dataStruct.DataFieldService;
import com.ly.ttd.feature.admin.api.dto.DataFieldDto;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 字段 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class DataFieldServiceImpl implements DataFieldService {
    @Resource
    private DataStructFieldMapper dataStructFieldMapper;

    @Override
    public Long add(DataFieldDto dto) throws BizException {
        DataFieldEntity entity = new DataFieldEntity();
        BeanUtils.copyProperties(dto, entity);
        dataStructFieldMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(DataFieldDto dto) throws BizException {
        DataFieldEntity entity = new DataFieldEntity();
        BeanUtils.copyProperties(dto, entity);
        dataStructFieldMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        dataStructFieldMapper.deleteById(id);
    }

    @Override
    public DataFieldDto queryById(Long id) throws BizException {
        DataFieldEntity entity = dataStructFieldMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        DataFieldDto dto = new DataFieldDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
