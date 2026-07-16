package com.ly.ttd.biz.feature.admin.srv.metaField.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.MetaFieldTestCaseEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.MetaFieldTestCaseMapper;
import com.ly.ttd.feature.admin.api.dto.MetaFieldTestCaseDto;
import com.ly.ttd.feature.admin.api.metaField.MetaFieldTestCaseService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 元字段测试用例 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class MetaFieldTestCaseServiceImpl implements MetaFieldTestCaseService {
    @Resource
    private MetaFieldTestCaseMapper metaFieldTestCaseMapper;

    @Override
    public Long add(MetaFieldTestCaseDto dto) throws BizException {
        MetaFieldTestCaseEntity entity = new MetaFieldTestCaseEntity();
        BeanUtils.copyProperties(dto, entity);
        metaFieldTestCaseMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(MetaFieldTestCaseDto dto) throws BizException {
        MetaFieldTestCaseEntity entity = new MetaFieldTestCaseEntity();
        BeanUtils.copyProperties(dto, entity);
        metaFieldTestCaseMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        metaFieldTestCaseMapper.deleteById(id);
    }

    @Override
    public MetaFieldTestCaseDto queryById(Long id) throws BizException {
        MetaFieldTestCaseEntity entity = metaFieldTestCaseMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        MetaFieldTestCaseDto dto = new MetaFieldTestCaseDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
