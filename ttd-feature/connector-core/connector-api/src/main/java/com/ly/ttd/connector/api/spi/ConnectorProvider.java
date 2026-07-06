package com.ly.ttd.connector.api.spi;

import com.ly.ttd.connector.api.AbstractConnectorRequest;
import com.ly.ttd.connector.api.ConnectorResponse;

/**
 * @author yong.li
 * @since 2026/4/14 13:55
 */
public interface ConnectorProvider<K extends AbstractConnectorRequest, V extends ConnectorResponse> {

    /**
     * 连接器类型
     */
    public String getConnectorType();

    /**
     * 获取连接器
     */
    public Connector<K, V> createConnector();

}
