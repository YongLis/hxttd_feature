package com.ly.ttd.feature.srv.connector.observer;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.connector.api.AbstractConnectorRequest;
import com.ly.ttd.connector.api.ConnectorResponse;
import com.ly.ttd.connector.api.spi.ConnectorObserver;
import com.ly.ttd.feature.common.enums.CallStateEnum;
import com.ly.ttd.feature.common.event.doris.ConnectorTrace;
import com.ly.ttd.feature.consts.FeatureTraceTableEnum;
import com.ly.ttd.feature.srv.data.TraceDataSaveService;
import com.ly.ttd.feature.srv.data.TraceMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author yong.li
 * @since 2026/6/15 14:07
 */
@Service
@Slf4j
public class ConnectorTraceSaveObserver implements ConnectorObserver {
    @Resource
    private TraceDataSaveService traceDataSaveService;

    @Override
    public void onComplete(AbstractConnectorRequest req, ConnectorResponse res) {
        String connectorId = req.getConnector().getConnectorId();
        ConnectorTrace trace = new ConnectorTrace();
        trace.setTxnId(req.getTxnId());
        trace.setConnectorId(connectorId);
        trace.setConnectorType(req.getConnector().getConnectorType());

        trace.setCost(res.getCost());
        trace.setCallState(CallStateEnum.SUCCESS.getCode());
        trace.setRequestJson(JSON.toJSONString(req));
        trace.setResponseJson(JSON.toJSONString(res));
        trace.setCallTime(new Date());
//        trace.setErrorMsg();

        TraceMessage traceMessage = new TraceMessage(FeatureTraceTableEnum.CONNECTOR_TRACE.getKafkaTopic(),
                JSON.toJSONString(trace));

        traceDataSaveService.save(traceMessage, req.getRunMode());

    }

    @Override
    public void onException(AbstractConnectorRequest req, ConnectorResponse res) {
        String connectorId = req.getConnector().getConnectorId();
        ConnectorTrace trace = new ConnectorTrace();
        trace.setTxnId(req.getTxnId());
        trace.setConnectorId(connectorId);
        trace.setConnectorType(req.getConnector().getConnectorType());

        trace.setCost(res.getCost());
        trace.setCallState(CallStateEnum.FAIL.getCode());
        trace.setRequestJson(JSON.toJSONString(req));
        trace.setResponseJson(JSON.toJSONString(res));
        trace.setCallTime(new Date());
        trace.setErrorMsg(res.getErrorMsg());

        TraceMessage traceMessage = new TraceMessage(FeatureTraceTableEnum.CONNECTOR_TRACE.getKafkaTopic(),
                JSON.toJSONString(trace));
        traceDataSaveService.save(traceMessage, req.getRunMode());

    }

    @Override
    public void onCancel(AbstractConnectorRequest req, ConnectorResponse res) {
        String connectorId = req.getConnector().getConnectorId();
        ConnectorTrace trace = new ConnectorTrace();
        trace.setTxnId(req.getTxnId());
        trace.setConnectorId(connectorId);
        trace.setConnectorType(req.getConnector().getConnectorType());

        trace.setCost(res.getCost());
        trace.setCallState(CallStateEnum.CANCEL.getCode());
        trace.setRequestJson(JSON.toJSONString(req));
//        trace.setResponseJson(JSON.toJSONString(res));
        trace.setCallTime(new Date());
//        trace.setErrorMsg();

        TraceMessage traceMessage = new TraceMessage(FeatureTraceTableEnum.CONNECTOR_TRACE.getKafkaTopic(),
                JSON.toJSONString(trace));
        traceDataSaveService.save(traceMessage, req.getRunMode());
    }
}
