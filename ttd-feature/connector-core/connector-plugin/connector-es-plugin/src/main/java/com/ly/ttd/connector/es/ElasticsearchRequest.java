package com.ly.ttd.connector.es;

import com.ly.ttd.connector.ConnectorException;
import com.ly.ttd.connector.api.AbstractConnectorRequest;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/3/27 11:43
 */
@Data
public class ElasticsearchRequest extends AbstractConnectorRequest {

    public static final String END_POINT = "endpoint";
    public static final String DSL = "dsl";
    public static final String DATA_SOURCE_NAME = "datasourceName";
    public static final String PARAM = "param";
    public static final String RETURN_TYPE = "returnType";


    @Override
    protected void validateRequest() throws ConnectorException {

    }

    public ElasticsearchRequest() {
    }

    public ElasticsearchRequest(ElasticsearchConnector connector) {
        super(connector);
    }


    public String getDatasourceName() {
        return getRequestParameter(DATA_SOURCE_NAME);
    }

    public String getEndpoint() {
        return getRequestParameter(END_POINT);
    }

    public String getDsl() {
        return getRequestParameter(DSL);
    }

    public Map<String, Object> getParam() {
        Object res = requestParameters.get(PARAM);
        return null == res ? new HashMap<>() : (Map<String, Object>) res;
    }

    public String getReturnType() {
        return getRequestParameter(RETURN_TYPE);
    }

    @Override
    public String getRequestParameter(String name) {
        return (String) requestParameters.get(name);
    }


}
