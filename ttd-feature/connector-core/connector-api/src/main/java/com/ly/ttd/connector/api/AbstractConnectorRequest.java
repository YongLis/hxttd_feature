package com.ly.ttd.connector.api;

import com.ly.ttd.connector.ConnectorException;
import com.ly.ttd.connector.api.spi.Connector;
import com.ly.ttd.connector.api.spi.ConnectorObserver;
import com.ly.ttd.connector.api.spi.ConnectorRequest;
import lombok.Data;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author yong.li
 * @since 2026/3/27 11:34
 */

@Data
public abstract class AbstractConnectorRequest<R extends ConnectorResponse> implements ConnectorRequest<R> {

    protected String txnId;

    protected Connector connector;

    protected String runMode;

    protected Map<String, Object> requestParameters = new LinkedHashMap<>();

    protected Duration requestTimeout;


    public AbstractConnectorRequest() {

    }


    public AbstractConnectorRequest(Connector connector) {
        this.connector = connector;
    }

    public R execute() {
        validateRequest();
        return (R) connector.execute(this);
    }

    protected abstract void validateRequest() throws ConnectorException;

    public void setRequestParameters(Map<String, Object> params) {
        for (Map.Entry<String, Object> param : params.entrySet()) {
            setRequestParameter(param.getKey(), param.getValue());
        }
    }

    public void execute(ConnectorObserver<?, R> observer) {
        validateRequest();
        connector.execute(this, observer);
    }

    @Override
    public void executeAsync() {
        validateRequest();
        connector.executeAsync(this);
    }

    @Override
    public CompletableFuture<R> executeFuture() {
        validateRequest();
        return connector.executeAsyncWithReturn(this);
    }

    @Override
    public Duration getRequestTimeout() {
        return requestTimeout;
    }

    @Override
    public void setRequestTimeout(Duration requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public void setRequestParameter(String name, Object value) {
        requestParameters.put(name, value);
    }

    public Map<String, Object> getRequestParameters() {
        return requestParameters;
    }

    public <V> V getRequestParameter(String name) {
        return (V) requestParameters.get(name);
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }
}
