package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.TableColumn;

import java.util.List;

/**
 * @author yong
 * @description 针对表【ttd_table_column(数据表列定义)】的数据库操作Mapper
 * @createDate 2026-07-08 13:48:53
 * @Entity com.ly.ttd.biz.admin.mybatis.entity.TableColumn
 */
@Mapper
public interface TableColumnMapper {

    /**
     * 根据ID查询
     */
    TableColumn selectById(@Param("id") Long id);
    /**
     * 查询全部
     */
    List<TableColumn> selectAll(String tableName);
}
