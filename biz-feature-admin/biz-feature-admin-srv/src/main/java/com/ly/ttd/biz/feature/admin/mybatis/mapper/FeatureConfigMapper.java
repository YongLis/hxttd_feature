package com.ly.ttd.biz.feature.admin.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ly.ttd.biz.feature.admin.mybatis.entity.FeatureConfigEntity;
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


    FeatureConfigEntity getByResourceKey(String resourceKey);

    List<FeatureConfigEntity> selectByProjectId(Long projectId);


    FeatureConfigEntity selectByFeatureCode(String featureCode);

}
