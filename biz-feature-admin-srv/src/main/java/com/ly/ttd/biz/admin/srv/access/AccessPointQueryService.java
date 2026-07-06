package com.ly.ttd.biz.admin.srv.access;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.mybatis.entity.AccessPointEntity;
import com.ly.ttd.biz.admin.srv.access.req.AccessPointQueryReq;
import com.ly.ttd.biz.admin.srv.access.res.AccessPointDocRes;
import com.ly.ttd.biz.admin.srv.access.res.AccessPointQueryRes;

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
