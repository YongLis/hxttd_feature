package com.ly.ttd.feature.srv.connector.op;

import com.ly.ttd.connector.api.spi.Connector;
import com.ly.ttd.feature.common.model.connector.ConnectorModel;

/**
 * @author yong.li
 * @since 2026/6/12 21:48
 */
public interface ConnectorOpService {


    /**
     * 执行连接器操作
     */
    Connector<?, ?> buildConnector(ConnectorModel connectorModel);

}
