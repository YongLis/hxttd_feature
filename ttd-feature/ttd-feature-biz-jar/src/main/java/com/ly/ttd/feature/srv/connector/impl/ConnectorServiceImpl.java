package com.ly.ttd.feature.srv.connector.impl;

import com.ly.ttd.connector.api.AbstractConnectorRequest;
import com.ly.ttd.connector.api.ConnectorResponse;
import com.ly.ttd.connector.engine.ConnectorEngine;
import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.common.model.connector.ConnectorModel;
import com.ly.ttd.feature.srv.connector.ConnectorService;
import com.ly.ttd.feature.srv.connector.observer.ConnectorTraceSaveObserver;
import com.ly.ttd.feature.srv.connector.op.ConnectorOpFactory;
import com.ly.ttd.feature.srv.connector.req.ConnectorReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author yong.li
 * @since 2026/6/12 16:29
 */
@Slf4j
@Service
public class ConnectorServiceImpl implements ConnectorService, FeatureConfigurationAware {
    private FeatureConfiguration featureConfiguration;

    @Resource
    private ConnectorTraceSaveObserver connectorTraceSaveObserver;

    @Override
    public ConnectorResponse execute(ConnectorReq req) throws FeatureBizException {

        ConnectorModel connectorModel = featureConfiguration.getConnectorMap().get(req.getConnectorKey());
        if (connectorModel == null) {
            log.error("connector not found, connectorKey: {}", req.getConnectorKey());
            throw new FeatureBizException("01", "connector not found");
        }
        // 执行connector
        ConnectorEngine connectorEngine = featureConfiguration.getConnectorEngine();
        AbstractConnectorRequest abstractConnectorRequest = ConnectorOpFactory
                .getConnectorOpService(connectorModel.getConnectorType())
                .buildRequest(req);
        return connectorEngine.executeSync(connectorModel.getResourceKey(),
                abstractConnectorRequest, Arrays.asList(connectorTraceSaveObserver));
    }

    @Override
    public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }
}
