package com.ly.ttd.biz.feature.dem.sweb.service.access;


import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.access.req.AccessPointQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.access.res.AccessPointDocRes;
import com.ly.ttd.biz.feature.dem.sweb.service.access.res.AccessPointQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.AccessPointEntity;

import java.util.List;

/**
 * @author yong.li
 * @since 2026/6/23 14:48
 */
public interface AccessPointQueryService {

    PageResult<AccessPointQueryRes> pageQuery(AccessPointQueryReq req);


    List<AccessPointEntity> list(Long projectId);

    /**
     * 获取接入点详情（含数据源/参数/响应模型）
     */
    AccessPointQueryRes getDetail(Long id);

    AccessPointDocRes getPointDoc(Long id);


}
