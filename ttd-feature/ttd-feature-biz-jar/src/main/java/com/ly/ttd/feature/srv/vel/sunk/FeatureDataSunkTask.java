package com.ly.ttd.feature.srv.vel.sunk;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.cfg.ThreadPoolNames;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.common.event.doris.VelHistorySnapshot;
import com.ly.ttd.feature.common.event.doris.VelMasterValueRecord;
import com.ly.ttd.feature.common.event.dto.VelWindowData;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.request.TxnFeatureRequest;
import com.ly.ttd.feature.srv.data.FeatureMiddleDataSaveService;
import com.ly.ttd.feature.srv.data.dto.FeatureMiddleData;
import com.ly.ttd.feature.srv.factor.velocity.vel.sink.VelDataSunkFactory;
import com.ly.ttd.feature.srv.factor.velocity.vel.sink.VelocityRedisKeyBuilder;
import com.ly.ttd.feature.srv.factor.velocity.vel.sink.VelocityTimeOutBuilder;
import com.ly.ttd.nacos.redis.XRedisTemplate;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 实时特征数据计算存储任务
 *
 * @author yong.li
 * @since 2026/4/23 16:28
 */
@Slf4j
@Service
public class FeatureDataSunkTask extends FeatureTask implements FeatureConfigurationAware {
    private FeatureConfiguration featureConfiguration;

    private static String TXM_VEL_HANDLE_KEY = "ttd:fea:handle:%s:%s";
    @Resource
    private XRedisTemplate xRedisTemplate;
    @Resource
    private FeatureMiddleDataSaveService featureMiddleDataSaveService;

    @Override
    public void execute(TxnFeatureRequest request) throws FeatureBizException {

        if (CollectionUtils.isEmpty(request.getEventDataList())) {
            log.warn("FeatureDataSunkTask warn, pointCode: {},txnId: {}, eventDataDos is empty",
                    request.getPointCode(), request.getTxnId());
            throw new FeatureBizException("01", "实时特征事件流数据为空");
        }

        // 保存实时特征事件流数据
        featureMonitorSinkService.syncSaveVelEventData(request.getEventDataList(), request.getRunMode());

        List<CompletableFuture<VelWindowData>> futureList = new ArrayList<>();

        for (VelEventData t : request.getEventDataList()) {
            //实时特征累计幂等判断
            String handledRedisKey = String.format(TXM_VEL_HANDLE_KEY, t.getTxnId(), t.getFeatureCode());
            if (xRedisTemplate.exist(handledRedisKey)) {
                log.warn("velocity data already write, txnId={}, featureCode={}", t.getTxnId(), t.getFeatureCode());
                continue;
            }

            CompletableFuture<VelWindowData> future = CompletableFuture.supplyAsync(() -> {

                try {
                    VelWindowData dataDto = VelDataSunkFactory.getService(t.getTimeMode())
                            .getVelData(t);
                    // 保存缓存
                    String redisKey = VelocityRedisKeyBuilder.buildVelItemKey(t);
                    Integer cacheTime = VelocityTimeOutBuilder.getCacheTime(t);
                    dataDto.setRedisKey(redisKey);

                    FeatureMiddleData middleData = new FeatureMiddleData(redisKey,
                            JSON.toJSONString(dataDto.getAfterData()),
                            Long.valueOf(cacheTime));
                    featureMiddleDataSaveService.saveData(middleData);

                    //存储实时特征主维度值记录
                    saveMasterValueMonitorData(Lists.newArrayList(dataDto), request.getRunMode());
                    //存储实时特征写入快照数据
                    saveVelocityWriteSnapshotData(Lists.newArrayList(dataDto), request.getRunMode());

                    xRedisTemplate.set(handledRedisKey, "1", 300L);
                    return dataDto;
                } catch (Exception e) {
                    log.error("save feature record error, txnId={}, eventData={}", t.getTxnId(),
                            JSON.toJSONString(t),
                            e);
                    return null;
                }
            }, featureConfiguration.getThreadPool(ThreadPoolNames.THREAD_LOG));
            futureList.add(future);
        }
        CompletableFuture
                .allOf(futureList.toArray(new CompletableFuture[0])).join();
    }

    private void saveVelocityWriteSnapshotData(List<VelWindowData> resultList, String runMode) {
        resultList.forEach(t -> {
            // 存储实时特征写入快照数据
            VelHistorySnapshot dto = new VelHistorySnapshot();
            dto.setFeatureCode(t.getFeatureCode());
            dto.setMasterValue(t.getMasterValue());
            dto.setTxnId(t.getTxnId());
            dto.setRedisKey(t.getRedisKey());
            dto.setBeforeData(JSON.toJSONString(t.getBeforeData()));
            dto.setAfterData(JSON.toJSONString(t.getAfterData()));
            dto.setExpireTime(t.getExpireTime());
            dto.setTxnTime(new Date());
            featureMonitorSinkService.syncSaveVelHistorySnapshot(dto, runMode);

        });
    }

    private void saveMasterValueMonitorData(List<VelWindowData> resultList, String runMode) {
        resultList.forEach(t -> {
            VelMasterValueRecord record = new VelMasterValueRecord();
            record.setRedisKey(t.getRedisKey());
            record.setFeatureCode(t.getFeatureCode());
            record.setMasterValue(t.getMasterValue());
            record.setCacheSize(t.getAfterData()
                    .keySet()
                    .size());
            record.setCacheByteSize((long) JSON
                    .toJSONString(t.getAfterData())
                    .getBytes().length);
            record.setExpireTime(t.getExpireTime());
            record.setTxnTime(new Date());
            featureMonitorSinkService.syncSaveVelMasterValueRecord(record, runMode);
        });
    }

    @Override
    public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }
}
