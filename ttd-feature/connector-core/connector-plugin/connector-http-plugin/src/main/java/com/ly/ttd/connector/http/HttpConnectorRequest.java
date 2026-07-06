package com.ly.ttd.connector.http;

import com.ly.ttd.connector.ConnectorException;
import com.ly.ttd.connector.api.AbstractConnectorRequest;
import lombok.Data;

import java.util.Map;

/**
 * @author yong.li
 * @since 2026/4/14 09:34
 */
@Data
public class HttpConnectorRequest extends AbstractConnectorRequest {

    public static final String DATA_SOURCE_NAME = "dataSourceName";
    public static final String URL = "url";
    public static final String METHOD = "method";
    public static final String HEADER = "header";
    public static final String BODY = "body";


    public HttpConnectorRequest(HttpConnector connector) {
        super(connector);
    }

    @Override
    protected void validateRequest() throws ConnectorException {

    }

    public String getDataSourceName() {
        return (String) getRequestParameter(DATA_SOURCE_NAME);
    }

    public String getUrl() {
        return (String) getRequestParameter(URL);
    }

    public String getMethod() {
        return (String) getRequestParameter(METHOD);
    }

    public Map<String, String> getHeader() {
        return (Map<String, String>) getRequestParameter(HEADER);
    }

    public String getBody() {
        return (String) getRequestParameter(BODY);
    }

}
