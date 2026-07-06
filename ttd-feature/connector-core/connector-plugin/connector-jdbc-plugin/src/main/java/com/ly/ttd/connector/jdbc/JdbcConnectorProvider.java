package com.ly.ttd.connector.jdbc;

import com.ly.ttd.connector.api.ConnectorResponse;
import com.ly.ttd.connector.api.spi.Connector;
import com.ly.ttd.connector.api.spi.ConnectorProvider;
import com.ly.ttd.consts.enums.ConnectorEnum;

/**
 * @author yong.li
 * @since 2026/4/14 16:17
 */
public class JdbcConnectorProvider implements ConnectorProvider<JdbcRequest, ConnectorResponse> {
    @Override
    public String getConnectorType() {
        return ConnectorEnum.JDBC.getCode();
    }

    @Override
    public Connector<JdbcRequest, ConnectorResponse> createConnector() {
        return new JdbcConnector(getConnectorType());
    }

}
