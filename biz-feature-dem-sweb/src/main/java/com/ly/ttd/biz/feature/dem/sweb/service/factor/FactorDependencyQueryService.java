package com.ly.ttd.biz.feature.dem.sweb.service.factor;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.FactorDependencyQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.res.FactorDependencyQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.FactorDependencyEntity;

import java.util.List;

/**
 * 指标血缘查询服务
 *
 * @author yong.li
 * @since 2026-06-30
 */
public interface FactorDependencyQueryService {

    /**
     * 分页查询血缘关系
     * <p>UP: 查询当前指标的上游依赖（当前指标依赖哪些指标）
     * <p>DOWN: 查询当前指标的下游依赖（哪些指标依赖当前指标）
     */
    PageResult<FactorDependencyQueryRes> pageQuery(FactorDependencyQueryReq req);


    List<FactorDependencyEntity> queryDependency(Long projectId, String parent);


    List<FactorDependencyEntity> queryUpstreamDependency(Long projectId, String child);


    void removeDependency(Long projectId, String parent);

    void addDependency(Long projectId, String parent, String parentType, List<String> childs, String user);
}
