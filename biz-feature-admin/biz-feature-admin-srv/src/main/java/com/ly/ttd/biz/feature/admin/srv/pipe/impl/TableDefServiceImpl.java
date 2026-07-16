package com.ly.ttd.biz.feature.admin.srv.pipe.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.TableColumn;
import com.ly.ttd.biz.feature.admin.mybatis.entity.TableDef;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.TableColumnMapper;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.TableDefMapper;
import com.ly.ttd.feature.admin.api.dto.TableDefDto;
import com.ly.ttd.feature.admin.api.pipe.TableDefService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据表定义 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class TableDefServiceImpl implements TableDefService {
    @Resource
    private TableDefMapper tableDefMapper;
    @Resource
    private TableColumnMapper tableColumnMapper;

    @Override
    @Transactional
    public void add(TableDefDto dto) throws BizException {

        TableDef entity = new TableDef();
        BeanUtils.copyProperties(dto, entity);
        tableDefMapper.insert(entity);
        Long tableId = entity.getId();
        if (CollectionUtils.isNotEmpty(dto.getColumns())) {
            List<TableColumn> columnList = dto.getColumns()
                    .stream()
                    .map(t -> {
                        TableColumn column = new TableColumn();
                        column.setTableName(t.getTableName());
                        column.setColumnName(t.getColumnName());
                        column.setColumnType(t.getColumnType());
                        column.setEmpty(t.getEmpty());
                        column.setFeatureCode(t.getFeatureCode());
                        column.setCrtUser(t.getCrtUser());
                        column.setUptUser(t.getUptUser());
//                        column.setCrtTime();
//                        column.setUptTime();
                        column.setDeleted(false);
                        return column;
                    }).toList();
            tableColumnMapper.insert(columnList);
        }

    }

    @Override
    public void update(TableDefDto dto) throws BizException {
        TableDef entity = new TableDef();
        BeanUtils.copyProperties(dto, entity);
        tableDefMapper.updateById(entity);
    }

    @Override
    public void delete(String id, String opUser) throws BizException {
        tableDefMapper.deleteById(id);
    }

    @Override
    public TableDefDto queryById(String id) throws BizException {
        TableDef entity = tableDefMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        TableDefDto dto = new TableDefDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
