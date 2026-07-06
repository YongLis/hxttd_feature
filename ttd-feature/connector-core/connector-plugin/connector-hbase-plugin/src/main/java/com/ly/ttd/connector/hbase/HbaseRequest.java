package com.ly.ttd.connector.hbase;

import com.ly.ttd.connector.ConnectorException;
import com.ly.ttd.connector.api.AbstractConnectorRequest;
import lombok.Data;

/**
 * @author yong.li
 * @since 2026/3/27 11:50
 */
@Data
public class HbaseRequest extends AbstractConnectorRequest {

    //    public static final String CLUSTER_NAME = "clusterName";
    public static final String TABLE = "table";
    public static final String ROW_KEY = "rowKey";
    public static final String FAMILY = "family";
    public static final String QUALIFIER = "qualifier";
    public static final String OP_TYPE = "opType";

    public HbaseRequest() {
    }

    public HbaseRequest(HbaseConnector connector) {
        super(connector);
    }

    @Override
    protected void validateRequest() throws ConnectorException {

    }

    public String getTable() {
        return (String) getRequestParameter(TABLE);
    }

    public String getRowKey() {
        return (String) getRequestParameter(ROW_KEY);
    }

    public String getFamily() {
        return (String) getRequestParameter(FAMILY);
    }

    public String getQualifier() {
        return (String) getRequestParameter(QUALIFIER);
    }
}
