package com.ly.ttd.biz.feature.admin.srv.pipe.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.TableColumn;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.TableColumnMapper;
import com.ly.ttd.feature.admin.api.dto.TableColumnDto;
import com.ly.ttd.feature.admin.api.pipe.TableColumnService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 数据表列 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class TableColumnServiceImpl implements TableColumnService {
    @Resource
    private TableColumnMapper tableColumnMapper;

    @Override
    public String add(TableColumnDto dto) throws BizException {
        TableColumn entity = new TableColumn();
        BeanUtils.copyProperties(dto, entity);
        tableColumnMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(TableColumnDto dto) throws BizException {
        TableColumn entity = new TableColumn();
        BeanUtils.copyProperties(dto, entity);
        tableColumnMapper.updateById(entity);
    }

    @Override
    public void delete(String id, String opUser) throws BizException {
        tableColumnMapper.deleteById(id);
    }

    @Override
    public TableColumnDto queryById(String id) throws BizException {
        TableColumn entity = tableColumnMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        TableColumnDto dto = new TableColumnDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
