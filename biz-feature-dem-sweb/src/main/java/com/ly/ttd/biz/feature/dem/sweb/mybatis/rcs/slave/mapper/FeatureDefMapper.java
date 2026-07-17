package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.FeatureDef;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yong
 * @description 针对表【ttd_feature_def(特征定义表)】的数据库操作Mapper
 * @createDate 2026-07-08 13:47:46
 * @Entity com.ly.ttd.biz.admin.mybatis.entity.FeatureDef
 */
@Mapper
public interface FeatureDefMapper{

    /**
     * 根据ID查询
     */
    FeatureDef selectById(@Param("id") Long id);
    /**
     * 查询全部
     */
    List<FeatureDef> selectAll();
    
    /**
     * 根据 def_id 查询总数
     */
    long selectByFeatureName(@Param("featureName") String featureName);
}
