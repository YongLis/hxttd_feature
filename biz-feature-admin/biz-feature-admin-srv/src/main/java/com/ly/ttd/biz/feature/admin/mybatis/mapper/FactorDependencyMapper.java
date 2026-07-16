package com.ly.ttd.biz.feature.admin.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ly.ttd.biz.feature.admin.mybatis.entity.FactorDependencyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 指标血缘 Mapper
 *
 * @author yong.li
 * @since 2026-06-30
 */
@Mapper
public interface FactorDependencyMapper extends BaseMapper<FactorDependencyEntity> {

    /**
     * 查询血缘
     */
    List<FactorDependencyEntity> queryDependency(@Param("projectId") Long projectId, @Param("parents") List<String> parents);


    /**
     * 查询上游血缘
     */
    List<FactorDependencyEntity> queryUpstreamDependency(@Param("projectId") Long projectId, @Param("childs") List<String> childs);


    void removeDependency(Long projectId, String parent);
}
