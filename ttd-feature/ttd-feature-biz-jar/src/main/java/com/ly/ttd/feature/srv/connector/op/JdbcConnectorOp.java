package com.ly.ttd.feature.srv.connector.op;

import com.ly.ttd.connector.api.AbstractConnectorRequest;
import com.ly.ttd.connector.api.spi.Connector;
import com.ly.ttd.connector.jdbc.JdbcRequest;
import com.ly.ttd.consts.enums.ConnectorEnum;
import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.common.model.DataFieldModel;
import com.ly.ttd.feature.common.model.connector.ConnectorModel;
import com.ly.ttd.feature.common.model.connector.JdbcConnectorModel;
import com.ly.ttd.feature.srv.connector.req.ConnectorReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yong.li
 * @since 2026/6/12 22:44
 */
@Slf4j
@Service
public class JdbcConnectorOp extends AbstractConnectorOpService implements FeatureConfigurationAware {

    private FeatureConfiguration featureConfiguration;

    @Override
    public String getConnectorType() {
        return ConnectorEnum.JDBC.getCode();
    }

    @Override
    public AbstractConnectorRequest buildRequest(ConnectorReq req) {
        JdbcConnectorModel connectorDef = (JdbcConnectorModel) featureConfiguration.getConnectorMap().get(req.getConnectorKey());

        List<String> factorCodes = connectorDef.getFields().stream().map(DataFieldModel::getFieldCode).collect(Collectors.toList());

        JdbcRequest request = new JdbcRequest();
        request.setRequestTimeout(Duration.ofMillis(connectorDef.getTimeout()));

        request.setRequestParameter(JdbcRequest.SQL, connectorDef.getSql());
        request.setRequestParameter(JdbcRequest.DATA_SOURCE_NAME, connectorDef.getDataSourceName());
        request.setRequestParameter(JdbcRequest.PARAM, req.getCtx().getParamByFields(factorCodes));
        request.setRequestParameter(JdbcRequest.RETURN_TYPE, connectorDef.getReturnType());

        request.setConnector(featureConfiguration.getConnectorEngine().getConnector(req.getConnectorKey()));

        request.setTxnId(req.getCtx().getTxnId());
        return request;
    }

    @Override
    public Connector<?, ?> buildConnector(ConnectorModel connectorModel) {
        JdbcConnectorModel defModel = (JdbcConnectorModel) connectorModel;


        return null;
    }

    @Override
    public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }
}
