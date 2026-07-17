package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.FactorDependencyEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.FactorDependencyQueryReq;
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
public interface FactorDependencyMapper{

    /**
     * 分页查询血缘关系
     */
    List<FactorDependencyEntity> pageQuery(Page<FactorDependencyEntity> page, @Param("req") FactorDependencyQueryReq req);


    /**
     * 查询血缘
     */
    List<FactorDependencyEntity> queryDependency(@Param("projectId") Long projectId, @Param("parents") List<String> parents);


    /**
     * 查询上游血缘
     */
    List<FactorDependencyEntity> queryUpstreamDependency(@Param("projectId") Long projectId, @Param("childs") List<String> childs);


    void removeDependency(Long projectId, String parent);

    /**
     * 根据ID查询
     */
    FactorDependencyEntity selectById(@Param("id") Long id);

}
