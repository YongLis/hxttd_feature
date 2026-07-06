package com.ly.ttd.feature.srv.factor.velocity.vel.sink;

import com.ly.ttd.feature.common.enums.VelocityTimeModeEnum;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.common.event.dto.VelValueItem;
import com.ly.ttd.feature.common.event.dto.VelWindowData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于TTL（Time To Live）的统计数据存储服务
 * <p>
 * 该类继承自AbstractVelDataSunkService，用于处理基于TTL的统计数据存储。
 * 主要功能包括：
 * 1. 检查输入参数的有效性
 * 2. 从Redis获取缓存数据
 * 3. 根据TTL过滤过期数据
 * 4. 更新缓存数据并返回结果
 * <p>
 * 使用场景：
 * 当需要基于TTL存储和更新统计数据时，可以使用该类进行计算。
 * <p>
 * 注意事项：
 * 1. 输入参数dto不能为空，否则将返回null。
 * 2. 缓存数据为空时会创建新的缓存数据。
 * 3. 缓存数据不为空时会根据TTL过滤过期数据并更新现有数据。
 *
 * @author yong.li
 * @since 2026/4/23 17:05
 */
@Service
@Slf4j
public class VelDataSunkByTTLService extends AbstractVelDataSunkService {


    @Override
    protected String getTimeMode() {
        return VelocityTimeModeEnum.TTL.getType();
    }

    @Override
    public VelWindowData getVelData(VelEventData dto) {

        if (null == dto) {
            log.error("[VelDataSunkByTTLService] getVelData error, dto is null");
            return null;
        }
        VelWindowData dataDto = new VelWindowData();
        dataDto.setFeatureCode(dto.getFeatureCode());
        dataDto.setMasterValue(dto.getMasterValue());
        dataDto.setTxnId(dto.getTxnId());
        dataDto.setExpireTime(VelocityTimeOutBuilder.getWindowExpireTime(dto));
        String redisKey = VelocityRedisKeyBuilder.buildVelItemKey(dto);

        Map<String, List<VelValueItem>> cacheItemMap = getCacheItemFromRedis(dto.getTxnId(), redisKey);
        if (!cacheItemMap.isEmpty()) {
            long windowMinTime = VelocityTimeOutBuilder.getWindowMinTime(dto);

            // 过滤缓存Map中Item时间戳小于当前时间周期
            cacheItemMap.entrySet().removeIf(entry -> entry.getValue().stream().anyMatch(item -> item.getTs() < windowMinTime));
            // 移除缓存Map中Value为空的key
            cacheItemMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());

            dataDto.setBeforeData(cacheItemMap);
            dataDto.setAfterData(cacheItemMap);

        } else {
            dataDto.setBeforeData(new HashMap<>());
            dataDto.setAfterData(new HashMap<>());
        }

        return dataDto;
    }
}
