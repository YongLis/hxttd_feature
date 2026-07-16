package com.ly.ttd.biz.feature.admin.srv.dict.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.DictEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.DictMapper;
import com.ly.ttd.feature.admin.api.dict.DictService;
import com.ly.ttd.feature.admin.api.dto.DictDto;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 字典 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class DictServiceImpl implements DictService {
    @Resource
    private DictMapper dictMapper;

    @Override
    public Long add(DictDto dto) throws BizException {
        DictEntity entity = new DictEntity();
        BeanUtils.copyProperties(dto, entity);
        dictMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(DictDto dto) throws BizException {
        DictEntity entity = new DictEntity();
        BeanUtils.copyProperties(dto, entity);
        dictMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        dictMapper.deleteById(id);
    }

    @Override
    public DictDto queryById(Long id) throws BizException {
        DictEntity entity = dictMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        DictDto dto = new DictDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public DictDto getDictCode(String systemCode, String dictCode) {
        return dictMapper.getDictCode(systemCode, dictCode);
    }
}
