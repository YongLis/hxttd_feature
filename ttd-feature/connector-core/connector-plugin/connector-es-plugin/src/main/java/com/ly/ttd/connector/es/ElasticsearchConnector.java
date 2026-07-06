package com.ly.ttd.connector.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
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

/**
 * @author yong.li
 * @since 2026/4/14 09:55
 */
@Slf4j
public class ElasticsearchConnector extends AbstractConnector<ElasticsearchRequest, ConnectorResponse> implements FeatureConfigurationAware {
    private FeatureConfiguration featureConfiguration;

    public ElasticsearchConnector(String connectorId) {
        super(connectorId);
    }

    @Override
    public String getConnectorType() {
        return ConnectorEnum.ES.getCode();
    }

    @Override
    public ElasticsearchRequest createRequest() {
        return new ElasticsearchRequest(this);
    }

    @Override
    public ConnectorResponse execute(ElasticsearchRequest req) throws ConnectorException {
        StopWatch stopWatch = StopWatch.createStarted();
        ConnectorResponse response = new ConnectorResponse();
        response.setReq(req);
        try {
            ElasticsearchClient client = featureConfiguration.getConnectors().getElasticsearchClientMap()
                    .get(req.getDatasourceName());
            ElasticsearchExecutor executor = new ElasticsearchExecutor(client);
            Object res = executor.execute(req.getEndpoint(), req.getDsl());
            response.setRes(res);
            response.setState(ExecuteStateEnum.SUCCESS.getCode());
        } catch (Exception e) {
            log.error("elasticsearch connector execute error, txnId={}, req={}", req.getTxnId(),
                    JSON.toJSONString(req), e);
            response.setState(ExecuteStateEnum.ERROR.getCode());
            response.setErrorMsg(e.getMessage());
        }
        stopWatch.stop();
        response.setCost(stopWatch.getTime());
        return response;
    }

    @Override
    public ConnectorResponse execute(ElasticsearchRequest req, ConnectorObserver<ElasticsearchRequest, ConnectorResponse> observer) throws ConnectorException {

        StopWatch stopWatch = StopWatch.createStarted();
        ConnectorResponse response = new ConnectorResponse();
        response.setReq(req);
        try {
            ElasticsearchClient client = featureConfiguration.getConnectors().getElasticsearchClientMap()
                    .get(req.getDatasourceName());
            ElasticsearchExecutor executor = new ElasticsearchExecutor(client);
            Object res = executor.execute(req.getEndpoint(), req.getDsl());
            response.setRes(res);
            response.setState(ExecuteStateEnum.SUCCESS.getCode());
            if (null != observer) {
                observer.onComplete(req, response);
            }
        } catch (Exception e) {
            log.error("elasticsearch connector execute error, txnId={}, req={}", req.getTxnId(),
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
