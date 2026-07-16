package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.req.FeatureConfigQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.res.FeatureConfigListRes;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.FeatureConfigEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 特征配置 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface FeatureConfigMapper extends BaseMapper<FeatureConfigEntity> {


    /**
     * 分页查询
     */
    IPage<FeatureConfigEntity> pageQuery(Page<FeatureConfigEntity> page, FeatureConfigQueryReq req);


    FeatureConfigEntity getByResourceKey(String resourceKey);

    List<FeatureConfigEntity> selectByProjectId(Long projectId);


    FeatureConfigEntity selectByFeatureCode(String featureCode);

    List<FeatureConfigListRes> getListByProjectId(Long projectId);
}
