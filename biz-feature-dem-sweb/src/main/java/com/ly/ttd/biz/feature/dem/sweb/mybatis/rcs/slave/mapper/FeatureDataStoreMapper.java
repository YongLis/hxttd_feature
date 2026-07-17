package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.FeatureDataStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yong
 * @description 针对表【ttd_feature_data_store(特征数据表)】的数据库操作Mapper
 * @createDate 2026-07-08 13:47:46
 * @Entity com.ly.ttd.biz.admin.mybatis.entity.FeatureDataStore
 */
@Mapper
public interface FeatureDataStoreMapper{

    /**
     * 根据ID查询
     */
    FeatureDataStore selectById(@Param("id") Long id);
    /**
     * 查询全部
     */
    List<FeatureDataStore> selectAll();
    
    /**
     * 根据 entity_key 查询总数
     */
    long selectCountByKey(@Param("key") String key);
}
