package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.TableDef;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    TableDef selectById(@Param("id") Long id);
}
