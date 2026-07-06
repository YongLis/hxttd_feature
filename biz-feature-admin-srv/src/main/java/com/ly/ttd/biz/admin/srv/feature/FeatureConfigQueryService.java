package com.ly.ttd.biz.admin.srv.feature;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.mybatis.entity.FeatureConfigEntity;
import com.ly.ttd.biz.admin.srv.feature.req.FeatureConfigQueryReq;
import com.ly.ttd.biz.admin.srv.feature.res.FeatureConfigListRes;
import com.ly.ttd.biz.admin.srv.feature.res.FeatureConfigQueryRes;

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
