package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.FeatureEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yong
 * @description 针对表【ttd_feature_entity(元数据实体表)】的数据库操作Mapper
 * @createDate 2026-07-08 13:47:19
 * @Entity com.ly.ttd.biz.admin.mybatis.entity.FeatureEntity
 */
@Mapper
public interface FeatureEntityMapper{

    /**
     * 根据ID查询
     */
    FeatureEntity selectById(@Param("id") Long id);
    /**
     * 查询全部
     */
    List<FeatureEntity> selectAll();

}
