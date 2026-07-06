package com.ly.ttd.feature.srv.factor.velocity.vel.sink;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.common.event.dto.VelValueItem;
import com.ly.ttd.feature.common.event.dto.VelWindowData;
import com.ly.ttd.nacos.redis.XRedisTemplate;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实时特征数据保存服务
 *
 * @author yong.li
 * @since 2026/4/23 17:02
 */
@Slf4j
@Component
public abstract class AbstractVelDataSunkService {
    @Resource
    protected XRedisTemplate xRedisTemplate;

    /**
     * 实时特征数据保存类型
     */
    protected abstract String getTimeMode();

    /**
     * 获取实时特征数据
     */
    public abstract VelWindowData getVelData(VelEventData dto);


    protected Map<String, List<VelValueItem>> getCacheItemFromRedis(String txnId, String redisKey) {
        String cacheData = xRedisTemplate.get(redisKey);
        if (StringUtils.isNotBlank(cacheData)) {
            log.info(" getCacheItemFromRedis,txnId={}, redisKey: {} ,value={}", txnId, redisKey, cacheData);
            return JSON.parseObject(cacheData, new TypeReference<Map<String, List<VelValueItem>>>() {
            });
        }
        return new HashMap<>();

    }

}
