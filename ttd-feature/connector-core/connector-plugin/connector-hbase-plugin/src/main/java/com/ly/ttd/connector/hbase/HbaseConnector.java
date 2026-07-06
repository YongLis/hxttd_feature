package com.ly.ttd.connector.hbase;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.connector.ConnectorException;
import com.ly.ttd.connector.api.AbstractConnector;
import com.ly.ttd.connector.api.ConnectorResponse;
import com.ly.ttd.connector.api.spi.ConnectorObserver;
import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.common.enums.ConnectorEnum;
import com.ly.ttd.feature.common.enums.ExecuteStateEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.hadoop.hbase.client.Connection;

/**
 * @author yong.li
 * @since 2026/4/14 09:55
 */
@Slf4j
public class HbaseConnector extends AbstractConnector<HbaseRequest, ConnectorResponse> implements FeatureConfigurationAware {

    private FeatureConfiguration featureConfiguration;

    public HbaseConnector(String connectorId) {
        super(connectorId);
    }

    @Override
    public String getConnectorType() {
        return ConnectorEnum.HBASE.getCode();
    }

    @Override
    public HbaseRequest createRequest() {
        return new HbaseRequest(this);
    }

    @Override
    public ConnectorResponse execute(HbaseRequest req) throws ConnectorException {
        StopWatch stopWatch = StopWatch.createStarted();
        ConnectorResponse response = new ConnectorResponse();
        response.setReq(req);
        try {
            Connection connection = featureConfiguration.getConnectors()
                    .getHbaseClient();
            HbaseExecutor executor = new HbaseExecutor(connection);

            Object res = executor.query(
                    req.getTable(),
                    req.getRowKey(),
                    req.getFamily(),
                    req.getQualifier());
            response.setRes(res);
            response.setState(ExecuteStateEnum.SUCCESS.getCode());
        } catch (Exception e) {
            log.error("hbase connector execute error, txnId={}, req={}", req.getTxnId(),
                    JSON.toJSONString(req), e);
            response.setState(ExecuteStateEnum.ERROR.getCode());
            response.setErrorMsg(e.getMessage());
        }
        stopWatch.stop();
        response.setCost(stopWatch.getTime());
        return response;
    }

    @Override
    public ConnectorResponse execute(HbaseRequest req, ConnectorObserver<HbaseRequest, ConnectorResponse> observer) throws ConnectorException {
        StopWatch stopWatch = StopWatch.createStarted();
        ConnectorResponse response = new ConnectorResponse();
        response.setReq(req);
        try {
            Connection connection = featureConfiguration.getConnectors()
                    .getHbaseClient();
            HbaseExecutor executor = new HbaseExecutor(connection);

            Object res = executor.query(
                    req.getTable(),
                    req.getRowKey(),
                    req.getFamily(),
                    req.getQualifier());
            response.setRes(res);
            response.setState(ExecuteStateEnum.SUCCESS.getCode());
            if (null != observer) {
                observer.onComplete(req, response);
            }

        } catch (Exception e) {
            log.error("hbase connector execute error, txnId={}, req={}", req.getTxnId(),
                    JSON.toJSONString(req), e);
            response.setState(ExecuteStateEnum.ERROR.getCode());
            response.setErrorMsg(e.getMessage());
            if (null != observer) {
                observer.onException(req, response);
            }
        }
        stopWatch.stop();
        response.setCost(stopWatch.getTime());
        return response;
    }

    @Override
    public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }
}
