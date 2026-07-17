package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.DataFieldEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据集字段 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface DataStructFieldMapper {

    /**
     * 根据ID查询
     */
    DataFieldEntity selectById(@Param("id") Long id);
    /**
     * 查询全部
     */
    List<DataFieldEntity> selectAll();
    
    /**
     * 根据 data_struct_code 查询总数
     */
    long selectCountByKey(@Param("key") String key);
}
