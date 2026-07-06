package com.ly.ttd.feature.srv.connector.op;

import com.ly.ttd.connector.api.AbstractConnectorRequest;
import com.ly.ttd.feature.srv.connector.req.ConnectorReq;

/**
 * @author yong.li
 * @since 2026/6/12 22:42
 */
public abstract class AbstractConnectorOpService implements ConnectorOpService {

    /**
     * 获取连接器类型
     */
    public abstract String getConnectorType();


    /**
     *
     */
    public abstract AbstractConnectorRequest buildRequest(ConnectorReq req);


}
