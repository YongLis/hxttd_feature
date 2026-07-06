package com.ly.ttd.connector.es;

import com.ly.ttd.connector.api.ConnectorResponse;
import com.ly.ttd.connector.api.spi.Connector;
import com.ly.ttd.connector.api.spi.ConnectorProvider;
import com.ly.ttd.feature.common.enums.ConnectorEnum;

/**
 * @author yong.li
 * @since 2026/4/14 15:04
 */
public class ElasticsearchConnectorProvider implements ConnectorProvider<ElasticsearchRequest, ConnectorResponse> {
    @Override
    public String getConnectorType() {
        return ConnectorEnum.ES.getCode();
    }

    @Override
    public Connector<ElasticsearchRequest, ConnectorResponse> createConnector() {
        return new ElasticsearchConnector(getConnectorType());
    }
}
