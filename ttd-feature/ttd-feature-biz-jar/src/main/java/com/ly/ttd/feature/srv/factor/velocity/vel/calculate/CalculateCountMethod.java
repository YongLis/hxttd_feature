package com.ly.ttd.feature.srv.factor.velocity.vel.calculate;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.feature.common.enums.VelocityCaluateTypeEnum;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.common.event.doris.VelReadEvent;
import com.ly.ttd.feature.common.event.dto.VelCalculateResult;
import com.ly.ttd.feature.common.event.dto.VelValueItem;
import com.ly.ttd.feature.common.event.dto.VelWindowData;
import com.ly.ttd.feature.srv.factor.velocity.vel.sink.VelDataSunkFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 去重计算从维度个数
 * <p>
 * 该类继承自AbstractVelCalculateMethod，用于计算去重后的维度个数。
 * 主要功能包括：
 * 1. 检查输入参数的有效性
 * 2. 获取并过滤数据
 * 3. 计算去重后的维度个数
 * <p>
 * 使用场景：
 * 当需要统计某个维度下不同值的数量时，可以使用该类进行计算。
 * <p>
 * 注意事项：
 * 1. 仅支持单个主维度的计算，如果主维度个数大于1，将返回错误。
 * 2. 输入参数dto和masterValues不能为空，否则将返回错误。
 *
 * @author yong.li
 * @since 2026/4/24 14:22
 */
@Slf4j
@Service
public class CalculateCountMethod extends AbstractVelCalculateMethod {
    @Override
    protected String getCalculateMethod() {
        return VelocityCaluateTypeEnum.COUNT.getCode();
    }

    @Override
    public VelCalculateResult doCalculate(VelReadEvent dto) {

        if (null == dto || CollectionUtils.isEmpty(dto.getMasterValues())) {
            log.warn(
                    "[VelCalculateMethod] velocity factor config error,count not support master is empty");
            return new VelCalculateResult(BigDecimal.ZERO, "");
        }
        // 主维度个数大于1，不支持计数
        if (dto.getMasterValues()
                .size() > 1) {
            log.error(
                    "[VelCalculateMethod] velocity factor config error,count not support master more than one，definitionKey={}",
                    dto.getFactorCode());
            return new VelCalculateResult(BigDecimal.ZERO, "");
        }

        VelEventData eventDataDto = new VelEventData();
        BeanUtils.copyProperties(dto, eventDataDto);
        eventDataDto.setMasterValue(dto.getMasterValues()
                .get(0));

        VelWindowData dataDto = VelDataSunkFactory.getService(dto.getTimeMode())
                .getVelData(eventDataDto);
        if (null == dataDto) {
            log.warn(
                    "[VelCalculateMethod] velocity factor config error,count not support master more than one，definitionKey={}",
                    dto.getFactorCode());
            return new VelCalculateResult(BigDecimal.ZERO, "");
        }
        Map<String, List<VelValueItem>> dataMap = dataDto.getAfterData();

        filterData(dto, dataDto.getAfterData());

        BigDecimal result = BigDecimal.ZERO;
        if (dataMap != null) {
            result = BigDecimal.valueOf(dataMap.keySet()
                    .size());
        }
        return new VelCalculateResult(result, JSON.toJSONString(dataMap));
    }
}
