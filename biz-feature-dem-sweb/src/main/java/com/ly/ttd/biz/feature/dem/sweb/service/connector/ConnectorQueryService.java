package com.ly.ttd.biz.feature.dem.sweb.service.connector;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.connector.req.ConnectorQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.connector.res.ConnectorQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.ConnectorEntity;

/**
 * 连接器查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface ConnectorQueryService {

    PageResult<ConnectorQueryRes> pageQuery(ConnectorQueryReq req);

    ConnectorEntity getById(Long id);
}
