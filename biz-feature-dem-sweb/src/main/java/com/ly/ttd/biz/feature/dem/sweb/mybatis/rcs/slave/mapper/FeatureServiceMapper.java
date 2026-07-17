package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.FeatureService;

/**
 * @author yong
 * @description 针对表【ttd_feature_service(特征服务表)】的数据库操作Mapper
 * @createDate 2026-07-08 13:48:17
 * @Entity com.ly.ttd.biz.admin.mybatis.entity.FeatureService
 */
@Mapper
public interface FeatureServiceMapper{

    /**
     * 根据ID查询
     */
    FeatureService selectById(@Param("id") Long id);

}
