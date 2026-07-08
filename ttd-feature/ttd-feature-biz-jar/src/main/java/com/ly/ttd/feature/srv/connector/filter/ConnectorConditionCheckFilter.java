package com.ly.ttd.feature.srv.connector.filter;

import com.ly.ttd.connector.api.AbstractConnectorRequest;
import com.ly.ttd.connector.api.spi.Connector;
import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.common.enums.ScriptType;
import com.ly.ttd.feature.common.model.connector.ConnectorModel;
import com.ly.ttd.language.srv.impl.LanguageExecuteFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yong.li
 * @since 2026/7/7 16:20
 */
public class ConnectorConditionCheckFilter {

    private FeatureConfiguration featureConfiguration;

    public ConnectorConditionCheckFilter(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }

    public boolean checkCondition(AbstractConnectorRequest request) {
        Connector connector = request.getConnector();
        String connectorId = connector.getConnectorId();
        ConnectorModel connectorModel = featureConfiguration.getConnectorMap().get(connectorId);

        if (StringUtils.isEmpty(connectorModel.getCondition())) {
            return true;
        }

        // 执行前置条件
        Object res = LanguageExecuteFactory.getInstance(ScriptType.GROOVY.getCode())
                .execute(connectorModel.getCondition(), request.getRequestParameters());
        return null == res ? false : (Boolean) res;
    }


}
