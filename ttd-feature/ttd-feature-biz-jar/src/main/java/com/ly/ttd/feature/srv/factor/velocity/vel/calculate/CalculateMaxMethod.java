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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多个主维度取计算结果的最大值
 * <p>
 * 该类继承自AbstractVelCalculateMethod，用于计算多个主维度下的最大值。
 * 主要功能包括：
 * 1. 检查输入参数的有效性
 * 2. 获取并过滤数据
 * 3. 计算多个主维度的最大值
 * <p>
 * 使用场景：
 * 当需要从多个主维度中获取最大值时，可以使用该类进行计算。
 * <p>
 * 注意事项：
 * 1. 输入参数dto和masterValues不能为空，否则将返回错误。
 * 2. 支持多个主维度的计算，返回所有主维度中的最大值。
 *
 * @author yong.li
 * @since 2026/4/24 14:22
 */
@Slf4j
@Service
public class CalculateMaxMethod extends AbstractVelCalculateMethod {
    @Override
    protected String getCalculateMethod() {
        return VelocityCaluateTypeEnum.MAX.getCode();
    }

    @Override
    public VelCalculateResult doCalculate(VelReadEvent dto) {

        if (null == dto) {
            return new VelCalculateResult(BigDecimal.ZERO, "");
        }

        if (CollectionUtils.isEmpty(dto.getMasterValues())) {
            log.warn(
                    "[VelCalculateMethod] velocity factor config error,max not support master is empty，definitionKey={}, txnId={}, velocityCode={}",
                    dto.getFactorCode(), dto.getTxnId(), dto.getFeatureCode());
            return new VelCalculateResult(BigDecimal.ZERO, "");
        }

        Map<String, VelCalculateResult> calculateResultMap = new HashMap<>();
        List<BigDecimal> resultList = new ArrayList<>();
        for (int i = 0; i < dto.getMasterValues().size(); i++) {
            String master = dto.getMasterValues().get(i);

            VelEventData eventDataDto = new VelEventData();
            BeanUtils.copyProperties(dto, eventDataDto);
            eventDataDto.setMasterValue(master);
            if (CollectionUtils.isNotEmpty(dto.getSlaveValues())) {
                if (dto.getSlaveValues().size() == 1) {
                    String slave = dto.getSlaveValues().get(0);
                    eventDataDto.setSlaveValue(slave);
                } else if (dto.getSlaveValues().size() > 1 && dto.getMasterValues().size() == dto.getSlaveValues().size()) {
                    String slave = dto.getSlaveValues().get(i);
                    eventDataDto.setSlaveValue(slave);
                } else {
                    log.warn("[VelCalculateMethod] velocity factor cal max error for master slave size not equal, dto={}", JSON.toJSONString(dto));
                }

            }

            VelWindowData dataDto = VelDataSunkFactory.getService(dto.getTimeMode())
                    .getVelData(eventDataDto);
            Map<String, List<VelValueItem>> dataMap = dataDto.getAfterData();
            if (StringUtils.isNotBlank(eventDataDto.getSlaveValue())) {
                filterDataByMasterIndex(dto, eventDataDto.getSlaveValue(), dataDto.getAfterData());
            }

            BigDecimal result = BigDecimal.ZERO;
            if (dataMap != null) {
                result = BigDecimal.valueOf(dataMap.keySet()
                        .size());
            }
            resultList.add(result);
            calculateResultMap.put(master, new VelCalculateResult(result, JSON.toJSONString(dataMap)));
        }

        // 取最大值
        BigDecimal max = resultList.stream()
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        log.info("[VelCalculateMethod] calculate max result, factorCode={}, max={}, resultMap={}",
                dto.getFactorCode(), max, JSON.toJSONString(calculateResultMap));
        return new VelCalculateResult(max, JSON.toJSONString(calculateResultMap));
    }
}
