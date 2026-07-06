package com.ly.ttd.feature.srv.vel.monitor.impl;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.common.event.doris.VelHistorySnapshot;
import com.ly.ttd.feature.common.event.doris.VelMasterValueRecord;
import com.ly.ttd.feature.common.event.doris.VelReadSnapshot;
import com.ly.ttd.feature.consts.FeatureTraceTableEnum;
import com.ly.ttd.feature.srv.data.TraceDataSaveService;
import com.ly.ttd.feature.srv.data.TraceMessage;
import com.ly.ttd.feature.srv.vel.monitor.FeatureMonitorSinkService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yong.li
 * @since 2026/4/25 15:50
 */
@Service
@Slf4j
public class FeatureMonitorSinkServiceImpl implements FeatureMonitorSinkService {

    @Resource
    private TraceDataSaveService traceDataSaveService;

    @Override
    public void syncSaveVelEventData(List<VelEventData> dos, String runMode) {
        if (CollectionUtils.isEmpty(dos)) {
            return;
        }
        dos.forEach(dto -> {
            try {
                TraceMessage traceMessage = new TraceMessage(FeatureTraceTableEnum.VEL_EVENT_DATA.getKafkaTopic(), JSON.toJSONString(dto));
                traceDataSaveService.save(traceMessage, runMode);
            } catch (Exception e) {
                log.error("syncSaveVelEventData error ", e);
            }

        });
    }


    @Override
    public void syncSaveVelMasterValueRecord(VelMasterValueRecord record, String runMode) {
        try {
            TraceMessage traceMessage = new TraceMessage(FeatureTraceTableEnum.VEL_MASTER_VALUE_RECORD.getKafkaTopic(), JSON.toJSONString(record));
            traceDataSaveService.save(traceMessage, runMode);
        } catch (Exception e) {
            log.error("syncSaveVelMasterValueRecord error", e);
        }
    }

    @Override
    public void syncSaveVelHistorySnapshot(VelHistorySnapshot dto, String runMode) {
        try {
            TraceMessage traceMessage = new TraceMessage(FeatureTraceTableEnum.VEL_HISTORY_SNAPSHOT.getKafkaTopic(), JSON.toJSONString(dto));
            traceDataSaveService.save(traceMessage, runMode);
        } catch (Exception e) {
            log.error("syncSaveVelHistorySnapshot error", e);
        }
    }


    @Override
    public void syncSaveVelReadSnapshot(VelReadSnapshot dto, String runMode) {
        try {
            TraceMessage traceMessage = new TraceMessage(FeatureTraceTableEnum.VEL_READ_SNAPSHOT.getKafkaTopic(), JSON.toJSONString(dto));
            traceDataSaveService.save(traceMessage, runMode);
        } catch (Exception e) {
            log.error("syncSaveVelReadSnapshot error", e);
        }
    }
}
