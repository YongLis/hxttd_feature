package com.ly.ttd.feature.srv.data.impl;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.feature.common.event.hbase.HbaseData;
import com.ly.ttd.feature.srv.data.FeatureMiddleDataSaveService;
import com.ly.ttd.feature.srv.data.HbaseDataSaveService;
import com.ly.ttd.feature.srv.data.dto.FeatureMiddleData;
import com.ly.ttd.nacos.redis.XRedisTemplate;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yong.li
 * @since 2026/7/4 21:50
 */
@Service
@Slf4j
public class FeatureMiddleDataSaveServiceImpl implements FeatureMiddleDataSaveService {
    @Resource
    private XRedisTemplate xRedisTemplate;
    @Resource
    private HbaseDataSaveService hbaseDataSaveService;

    @Override
    public void saveData(FeatureMiddleData dto) {
        try {
            xRedisTemplate.set(dto.getUniqueKey(), dto.getValue(), dto.getExpireTime());

            HbaseData hbaseData = new HbaseData();
            hbaseData.setRowKey(dto.getUniqueKey());
            hbaseData.setFamily("a");
            hbaseData.setQualifier("fea");
            hbaseData.setValue(dto.getValue());

            hbaseDataSaveService.write("feature_mid", hbaseData);
        } catch (Exception e) {
            log.error("FeatureMiddleDataSave error, dto={}", JSON.toJSONString(dto), e);
        }
    }
}
