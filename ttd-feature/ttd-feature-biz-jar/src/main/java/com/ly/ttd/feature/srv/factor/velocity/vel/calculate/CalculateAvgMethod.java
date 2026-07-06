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
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计算平均数
 *
 * @author yong.li
 * @since 2026/4/24 14:22
 */
@Slf4j
@Component
public class CalculateAvgMethod extends AbstractVelCalculateMethod {
	@Override
	protected String getCalculateMethod() {
		return VelocityCaluateTypeEnum.AVG.getCode();
	}

	@Override
	public VelCalculateResult doCalculate(VelReadEvent dto) {

		if (null == dto || CollectionUtils.isEmpty(dto.getMasterValues())) {
			log.warn("CalculateAvgMethod doCalculate error, dto:{}", JSON.toJSONString(dto));
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
					log.warn("[VelCalculateMethod] velocity factor cal avg error for master slave size not equal, dto={}", JSON.toJSONString(dto));
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

		if (CollectionUtils.isEmpty(resultList)) {
			log.error("CalculateAvgMethod doCalculate error, resultList is empty, dto:{}", JSON.toJSONString(dto));
			return new VelCalculateResult(BigDecimal.ZERO, "");
		}
		BigDecimal avg = resultList.stream()
				.reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(resultList.size()), 2, RoundingMode.HALF_UP);
		return new VelCalculateResult(avg, JSON.toJSONString(calculateResultMap));
	}
}
