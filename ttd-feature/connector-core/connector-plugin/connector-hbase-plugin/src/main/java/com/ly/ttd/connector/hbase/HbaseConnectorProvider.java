package com.ly.ttd.connector.hbase;

import com.ly.ttd.connector.api.ConnectorResponse;
import com.ly.ttd.connector.api.spi.Connector;
import com.ly.ttd.connector.api.spi.ConnectorProvider;
import com.ly.ttd.feature.common.enums.ConnectorEnum;

/**
 * @author yong.li
 * @since 2026/4/14 15:04
 */
public class HbaseConnectorProvider implements ConnectorProvider<HbaseRequest, ConnectorResponse> {
    @Override
    public String getConnectorType() {
        return ConnectorEnum.HBASE.getCode();
    }

    @Override
    public Connector<HbaseRequest, ConnectorResponse> createConnector() {
        return new HbaseConnector(getConnectorType());
    }


}
