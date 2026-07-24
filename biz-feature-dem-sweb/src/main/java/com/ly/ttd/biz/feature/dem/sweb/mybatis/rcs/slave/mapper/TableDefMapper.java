package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.TableDef;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.req.TableDefQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yong
 * @description 针对表【ttd_table_def(数据表定义)】的数据库操作Mapper
 * @createDate 2026-07-08 13:48:53
 * @Entity com.ly.ttd.biz.admin.mybatis.entity.TableDef
 */

@Mapper
public interface TableDefMapper {

    /**
     * 根据ID查询
     */
    TableDef selectById(@Param("id") String id);

    /**
     * 分页查询
     */
    List<TableDef> pageQuery(Page<TableDef> page, @Param("req") TableDefQueryReq req);

    /**
     * 根据表名查询
     */
    TableDef selectByTableName(@Param("tableName") String tableName);

    /**
     * 查询所有有效的表定义（下拉列表用）
     */
    List<TableDef> selectAllValid();
}
