package com.ly.ttd.feature.srv.data.impl;

import com.ly.ttd.feature.common.enums.RunModeEnum;
import com.ly.ttd.feature.srv.data.TraceDataSaveService;
import com.ly.ttd.feature.srv.data.TraceMessage;
import com.ly.ttd.feature.srv.kafka.KafkaMessageSendService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yong.li
 * @since 2026/6/15 14:36
 */
@Service
@Slf4j
public class TraceDataSaveServiceImpl implements TraceDataSaveService {
    @Resource
    private KafkaMessageSendService kafkaMessageSendService;

    @Override
    public void save(TraceMessage traceMessage, String runMode) {
        log.info("save trace message, messageId: {}, message: {}", traceMessage.getMessageId(), traceMessage.getMessage());
        if (RunModeEnum.PROD.getCode().equals(runMode)) {
            kafkaMessageSendService.sendMessage(traceMessage.getTopic(), traceMessage.getMessageId(), traceMessage.getMessage());
        } else {
            log.info("runMode: {} is not prod, ignore save trace message, messageId={}", runMode, traceMessage.getMessageId());
        }
    }
}
