package com.ly.ttd.connector.jdbc;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.connector.ConnectorException;
import com.ly.ttd.connector.api.AbstractConnector;
import com.ly.ttd.connector.api.ConnectorResponse;
import com.ly.ttd.connector.api.spi.ConnectorObserver;
import com.ly.ttd.consts.exception.BizException;
import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.common.enums.ConnectorEnum;
import com.ly.ttd.feature.common.enums.ExecuteStateEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


/**
 * @author yong.li
 * @since 2026/4/14 15:20
 */
@Slf4j
public class JdbcConnector extends AbstractConnector<JdbcRequest, ConnectorResponse> implements FeatureConfigurationAware {

    private FeatureConfiguration featureConfiguration;

    public JdbcConnector(String connectorId) {
        super(connectorId);
    }

    @Override
    public String getConnectorType() {
        return ConnectorEnum.JDBC.getCode();
    }

    @Override
    public JdbcRequest createRequest() {
        return new JdbcRequest(this);
    }

    @Override
    public ConnectorResponse execute(JdbcRequest req) throws BizException {
        StopWatch stopWatch = StopWatch.createStarted();
        ConnectorResponse response = new ConnectorResponse();
        response.setReq(req);
        try {
            NamedParameterJdbcTemplate jdbcTemplate = featureConfiguration.getConnectors()
                    .getJdbcTemplateMap().get(req.getDataSourceName());
            JdbcExecutor executor = new JdbcExecutor(jdbcTemplate);
            Object res = executor.execute(req.getSql(), req.getParam(), req.getReturnType());
            response.setRes(res);
            response.setState(ExecuteStateEnum.SUCCESS.getCode());
        } catch (Exception e) {
            log.error("jdbc connector execute error, txnId={}, req={}", req.getTxnId(),
                    JSON.toJSONString(req), e);
            response.setState(ExecuteStateEnum.ERROR.getCode());
            response.setErrorMsg(e.getMessage());
        }
        stopWatch.stop();
        response.setCost(stopWatch.getTime());
        return response;
    }

    @Override
    public ConnectorResponse execute(JdbcRequest req, ConnectorObserver<JdbcRequest, ConnectorResponse> observer) throws ConnectorException {
        StopWatch stopWatch = StopWatch.createStarted();
        ConnectorResponse response = new ConnectorResponse();
        response.setReq(req);
        try {
            NamedParameterJdbcTemplate jdbcTemplate = featureConfiguration.getConnectors()
                    .getJdbcTemplateMap().get(req.getDataSourceName());
            JdbcExecutor executor = new JdbcExecutor(jdbcTemplate);
            Object res = executor.execute(req.getSql(), req.getParam(), req.getReturnType());
            response.setRes(res);
            response.setState(ExecuteStateEnum.SUCCESS.getCode());
            if (null != observer) {
                observer.onComplete(req, response);
            }
        } catch (Exception e) {
            log.error("jdbc connector execute error, txnId={}, req={}", req.getTxnId(),
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
