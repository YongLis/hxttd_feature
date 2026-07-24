package com.ly.ttd.biz.feature.dem.sweb.service.tableDef;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.TableDef;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.req.TableDefQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.res.TableDefDetail;

import java.util.List;

/**
 * 数据表定义 查询服务
 *
 * @author yong.li
 * @since 2026-07-14
 */
public interface TableDefQueryService {

    /**
     * 分页查询
     */
    PageResult<TableDefDetail> pageQuery(TableDefQueryReq req);

    /**
     * 根据表名查询
     */
    TableDefDetail getByTableName(String tableName);

    /**
     * 根据ID查询
     */
    TableDefDetail getById(String id);

    /**
     * 查询所有有效的表定义（下拉列表用）
     */
    List<TableDef> getAll();
}
