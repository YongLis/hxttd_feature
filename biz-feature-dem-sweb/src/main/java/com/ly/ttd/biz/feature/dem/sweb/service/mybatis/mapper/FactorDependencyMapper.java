package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.FactorDependencyQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.FactorDependencyEntity;
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
     * 分页查询血缘关系
     */
    IPage<FactorDependencyEntity> pageQuery(Page<FactorDependencyEntity> page, @Param("req") FactorDependencyQueryReq req);


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
