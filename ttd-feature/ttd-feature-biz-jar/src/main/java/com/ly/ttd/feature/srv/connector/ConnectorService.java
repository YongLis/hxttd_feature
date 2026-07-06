package com.ly.ttd.feature.srv.connector;

import com.ly.ttd.connector.api.ConnectorResponse;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.srv.connector.req.ConnectorReq;

/**
 * @author yong.li
 * @since 2026/5/23 20:23
 */
public interface ConnectorService {

    ConnectorResponse execute(ConnectorReq req) throws FeatureBizException;


}
