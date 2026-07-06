package com.ly.ttd.feature.srv.factor.velocity.vel.sink;

import com.ly.ttd.feature.common.enums.VelocityTimeModeEnum;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.common.event.dto.VelValueItem;
import com.ly.ttd.feature.common.event.dto.VelWindowData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 按自然日统计
 * <p>
 * 该类继承自AbstractVelDataSunkService，用于按自然日统计维度数据。
 * 主要功能包括：
 * 1. 检查输入参数的有效性
 * 2. 从Redis获取缓存数据
 * 3. 更新缓存数据并返回结果
 * <p>
 * 使用场景：
 * 当需要按自然日统计维度数据时，可以使用该类进行计算。
 * <p>
 * 注意事项：
 * 1. 输入参数dto不能为空，否则将返回null。
 * 2. 缓存数据为空时会创建新的缓存数据。
 * 3. 缓存数据不为空时会追加或更新现有数据。
 *
 * @author yong.li
 * @since 2026/4/24 11:47
 */
@Service
@Slf4j
public class VelDataSunkByCalendarDayService extends AbstractVelDataSunkService {
    @Override
    protected String getTimeMode() {
        return VelocityTimeModeEnum.CURRENT_DAY.getType();
    }

    @Override
    public VelWindowData getVelData(VelEventData dto) {

        if (null == dto) {
            log.error("[VelDataSunkByCalendarDayService] getVelData error, dto is null");
            return null;
        }

        VelWindowData dataDto = new VelWindowData();
        dataDto.setTxnId(dto.getTxnId());
        dataDto.setFeatureCode(dto.getFeatureCode());
        dataDto.setMasterValue(dto.getMasterValue());
        dataDto.setExpireTime(VelocityTimeOutBuilder.getWindowExpireTime(dto));
        // 获取缓存数据
        String redisKey = VelocityRedisKeyBuilder.buildVelItemKey(dto);
        Map<String, List<VelValueItem>> cacheMap = getCacheItemFromRedis(dto.getTxnId(), redisKey);
        dataDto.setBeforeData(cacheMap);
        dataDto.setAfterData(cacheMap);
        return dataDto;
    }

}
