package com.ly.ttd.biz.feature.dem.sweb.service.feature;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.req.FeatureConfigQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.res.FeatureConfigListRes;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.res.FeatureConfigQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.FeatureConfigEntity;

import java.util.List;

/**
 * @author yong.li
 * @since 2026/6/2 22:35
 */
public interface FeatureConfigQueryService {

    PageResult<FeatureConfigQueryRes> pageQuery(FeatureConfigQueryReq req);

    FeatureConfigEntity getByResourceKey(String resourceKey);

    List<FeatureConfigEntity> getByProjectId(Long projectId);

    List<FeatureConfigListRes> getListByProjectId(Long projectId);

    FeatureConfigEntity getById(Long id);

}
