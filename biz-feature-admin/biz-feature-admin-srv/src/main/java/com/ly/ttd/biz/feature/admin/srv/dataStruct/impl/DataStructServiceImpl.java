package com.ly.ttd.biz.feature.admin.srv.dataStruct.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.DataStructEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.DataStructMapper;
import com.ly.ttd.feature.admin.api.dataStruct.DataStructService;
import com.ly.ttd.feature.admin.api.dto.DataStructDto;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 数据集 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class DataStructServiceImpl implements DataStructService {
    @Resource
    private DataStructMapper dataStructMapper;

    @Override
    public Long add(DataStructDto dto) throws BizException {
        DataStructEntity entity = new DataStructEntity();
        BeanUtils.copyProperties(dto, entity);
        dataStructMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(DataStructDto dto) throws BizException {
        DataStructEntity entity = new DataStructEntity();
        BeanUtils.copyProperties(dto, entity);
        dataStructMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        dataStructMapper.deleteById(id);
    }

    @Override
    public DataStructDto queryById(Long id) throws BizException {
        DataStructEntity entity = dataStructMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        DataStructDto dto = new DataStructDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
