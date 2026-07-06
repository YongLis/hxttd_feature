package com.ly.ttd.biz.admin.srv.connector;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.mybatis.entity.ConnectorEntity;
import com.ly.ttd.biz.admin.srv.connector.req.ConnectorQueryReq;
import com.ly.ttd.biz.admin.srv.connector.res.ConnectorQueryRes;

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
