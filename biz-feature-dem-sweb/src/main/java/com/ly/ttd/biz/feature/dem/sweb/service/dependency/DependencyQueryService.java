package com.ly.ttd.biz.feature.dem.sweb.service.dependency;


import com.ly.ttd.biz.feature.dem.sweb.service.dependency.req.DependencyQueryReq;
import com.ly.ttd.feature.admin.api.res.DependencyQueryRes;

/**
 * @author yong.li
 * @since 2026/7/1 15:00
 */
public interface DependencyQueryService {

    /**
     * 查询血缘
     */
    DependencyQueryRes queryDependency(DependencyQueryReq req);


}
