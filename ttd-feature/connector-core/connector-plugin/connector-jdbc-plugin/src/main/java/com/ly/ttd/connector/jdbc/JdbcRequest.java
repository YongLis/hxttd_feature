package com.ly.ttd.connector.jdbc;

import com.ly.ttd.connector.ConnectorException;
import com.ly.ttd.connector.api.AbstractConnectorRequest;
import com.ly.ttd.utils.TextUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/3/27 11:45
 */
@Data
public class JdbcRequest extends AbstractConnectorRequest {
    public static final Long DEFAULT_REQUEST_TIMEOUT = 3000L;

    public static final String DATA_SOURCE_NAME = "dataSourceName";
    public static final String SQL = "sql";
    public static final String PARAM = "param";
    public static final String RETURN_TYPE = "returnType";

    public JdbcRequest() {
    }

    public JdbcRequest(JdbcConnector connector) {
        super(connector);
    }


    @Override
    protected void validateRequest() throws ConnectorException {
        String dataSourceName = getDataSourceName();
        if (StringUtils.isEmpty(dataSourceName)) {
            throw new ConnectorException(TextUtils.fmtMessage("JDBC {} required", DATA_SOURCE_NAME));
        }

        String sql = getSql();
        if (StringUtils.isEmpty(sql)) {
            throw new ConnectorException(TextUtils.fmtMessage("JDBC {} required", SQL));
        }
        Map<String, Object> param = getParam();
        if (param == null) {
            throw new ConnectorException(TextUtils.fmtMessage("JDBC {} required", PARAM));
        }
        String returnType = getReturnType();
        if (StringUtils.isEmpty(returnType)) {
            throw new ConnectorException(TextUtils.fmtMessage("JDBC {} required", RETURN_TYPE));
        }

        Duration requestTimeout = getRequestTimeout();
        if (requestTimeout == null) {
            setRequestTimeout(Duration.ofMillis(DEFAULT_REQUEST_TIMEOUT));
        }

    }


    public String getSql() {
        return getRequestParameter(SQL);
    }

    public String getReturnType() {
        return getRequestParameter(RETURN_TYPE);
    }

    public String getDataSourceName() {
        return getRequestParameter(DATA_SOURCE_NAME);
    }

    @Override
    public String getRequestParameter(String name) {
        return (String) requestParameters.get(name);
    }

    public Map<String, Object> getParam() {
        return (Map<String, Object>) requestParameters.get(PARAM);
    }

}
