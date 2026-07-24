package com.ly.ttd.biz.feature.dem.sweb.service.tableDef.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.TableColumn;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.TableDef;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.TableColumnMapper;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.TableDefMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.TableDefQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.req.TableDefQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.res.TableColumnDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.res.TableDefDetail;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据表定义 查询服务实现
 *
 * @author yong.li
 * @since 2026-07-14
 */
@Slf4j
@Service
public class TableDefQueryServiceImpl implements TableDefQueryService {

    @Resource
    private TableDefMapper tableDefMapper;

    @Resource
    private TableColumnMapper tableColumnMapper;

    @Override
    public PageResult<TableDefDetail> pageQuery(TableDefQueryReq req) {
        PageResult<TableDefDetail> result = new PageResult<>();
        Page<TableDef> page = new Page<>(req.getCurrent(), req.getPageSize());
        List<TableDef> records = tableDefMapper.pageQuery(page, req);
        page.setRecords(records);
        if (CollectionUtils.isNotEmpty(records)) {
            result.setData(records.stream().map(this::entityConvert).collect(Collectors.toList()));
        }
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setCode("0000");
        return result;
    }

    @Override
    public TableDefDetail getByTableName(String tableName) {
        TableDef entity = tableDefMapper.selectByTableName(tableName);
        return entity != null ? entityConvert(entity) : null;
    }

    @Override
    public List<TableDef> getAll() {
        return tableDefMapper.selectAllValid();
    }

    @Override
    public TableDefDetail getById(String id) {
        TableDef entity = tableDefMapper.selectById(id);
        return entity != null ? entityConvert(entity) : null;
    }

    private TableDefDetail entityConvert(TableDef e) {
        TableDefDetail res = new TableDefDetail();
        res.setId(e.getId());
        res.setTableName(e.getTableName());
        res.setDataSource(e.getDataSource());
        res.setCrtUser(e.getCrtUser());
        res.setCrtTime(e.getCrtTime());
        res.setUptUser(e.getUptUser());
        res.setUptTime(e.getUptTime());
        res.setDeleted(e.getDeleted());

        // 查询该表对应的字段列表
        List<TableColumn> columns = tableColumnMapper.selectByTableName(e.getTableName());
        if (CollectionUtils.isNotEmpty(columns)) {
            res.setColumnCount(columns.size());
            res.setColumns(columns.stream().map(this::columnConvert).collect(Collectors.toList()));
        } else {
            res.setColumnCount(0);
        }

        return res;
    }

    private TableColumnDetail columnConvert(TableColumn e) {
        TableColumnDetail res = new TableColumnDetail();
        res.setId(e.getId());
        res.setTableName(e.getTableName());
        res.setColumnName(e.getColumnName());
        res.setColumnType(e.getColumnType());
        res.setNullAble(e.getNullAble());
        res.setFactorCode(e.getFactorCode());
        res.setCrtUser(e.getCrtUser());
        res.setCrtTime(e.getCrtTime());
        return res;
    }
}
