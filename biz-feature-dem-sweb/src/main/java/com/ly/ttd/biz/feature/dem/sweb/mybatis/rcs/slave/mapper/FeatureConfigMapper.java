package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.FeatureConfigEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.req.FeatureConfigQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.res.FeatureConfigListRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 特征配置 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface FeatureConfigMapper{


    /**
     * 分页查询
     */
    List<FeatureConfigEntity> pageQuery(Page<FeatureConfigEntity> page, FeatureConfigQueryReq req);


    FeatureConfigEntity getByResourceKey(String resourceKey);

    List<FeatureConfigEntity> selectByProjectId(Long projectId);


    FeatureConfigEntity selectByFeatureCode(String featureCode);

    List<FeatureConfigListRes> getListByProjectId(Long projectId);

    /**
     * 根据ID查询
     */
    FeatureConfigEntity selectById(@Param("id") Long id);

    
    /**
     * 根据 resource_key 查询总数
     */
    long selectCountByKey(@Param("key") String key);
}
