package com.ly.ttd.connector.api.spi;

import com.ly.ttd.connector.api.AbstractConnectorRequest;
import com.ly.ttd.connector.api.ConnectorResponse;

/**
 * @author yong.li
 * @since 2026/4/14 14:21
 */
public interface ConnectorObserver<K extends AbstractConnectorRequest, V extends ConnectorResponse> {


    /**
     * 完成时触发
     */
    void onComplete(K req, V res);

    /**
     * 异常时触发
     */
    void onException(K req, V res);


    /**
     * 取消时触发
     */
    void onCancel(K req, V res);


}
