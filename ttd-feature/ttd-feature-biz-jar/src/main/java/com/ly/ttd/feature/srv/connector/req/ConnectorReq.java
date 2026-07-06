package com.ly.ttd.feature.srv.connector.req;

import com.ly.ttd.feature.common.ctx.TxnParamContext;
import lombok.Data;

/**
 * @author yong.li
 * @since 2026/6/12 16:21
 */
@Data
public class ConnectorReq {

    private String connectorKey;

    private TxnParamContext ctx;

    public ConnectorReq(String connectorKey, TxnParamContext ctx) {
        this.connectorKey = connectorKey;
        this.ctx = ctx;
    }
}
