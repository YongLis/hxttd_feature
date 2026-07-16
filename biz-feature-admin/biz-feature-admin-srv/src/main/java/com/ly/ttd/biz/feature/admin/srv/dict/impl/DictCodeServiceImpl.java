package com.ly.ttd.biz.feature.admin.srv.dict.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.DictCodeEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.DictCodeMapper;
import com.ly.ttd.feature.admin.api.dict.DictCodeService;
import com.ly.ttd.feature.admin.api.dto.DictCodeDto;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典项 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class DictCodeServiceImpl implements DictCodeService {
    @Resource
    private DictCodeMapper dictCodeMapper;


    @Override
    public Long add(DictCodeDto dto) throws BizException {
        DictCodeEntity entity = new DictCodeEntity();
        BeanUtils.copyProperties(dto, entity);
        dictCodeMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(DictCodeDto dto) throws BizException {
        DictCodeEntity entity = new DictCodeEntity();
        BeanUtils.copyProperties(dto, entity);
        dictCodeMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        dictCodeMapper.deleteById(id);
    }

    @Override
    public DictCodeDto queryById(Long id) throws BizException {
        DictCodeEntity entity = dictCodeMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        DictCodeDto dto = new DictCodeDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public void addDictCode(Long id, String pointCode, String name) {
        DictCodeEntity entity = new DictCodeEntity();
        entity.setDictId(id);
        entity.setDictKey(pointCode);
        entity.setDictValue(name);
        entity.setCrtUser("S");
        entity.setDeleted(false);
        dictCodeMapper.insert(entity);
    }

    @Override
    public List<DictCodeDto> getByDictId(Long dictId) {
        QueryWrapper<DictCodeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_id", dictId);
        queryWrapper.eq("deleted", false);
        List<DictCodeEntity> entities = dictCodeMapper.selectList(queryWrapper);
        return entities.stream().map(entity -> {
            DictCodeDto dto = new DictCodeDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
