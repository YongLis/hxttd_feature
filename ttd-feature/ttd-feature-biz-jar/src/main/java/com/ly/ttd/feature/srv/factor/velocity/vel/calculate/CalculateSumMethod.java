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
 * 求和计算
 * <p>
 * 该类继承自AbstractVelCalculateMethod，用于计算多个主维度下的求和值。
 * 主要功能包括：
 * 1. 检查输入参数的有效性
 * 2. 获取并过滤数据
 * 3. 计算多个主维度的求和值
 * <p>
 * 使用场景：
 * 当需要从多个主维度中获取求和值时，可以使用该类进行计算。
 * <p>
 * 注意事项：
 * 1. 输入参数dto和masterValues不能为空，否则将返回错误。
 * 2. 仅支持单个主维度的计算，如果主维度个数大于1，将返回错误。
 *
 * @author yong.li
 * @since 2026/4/24 14:22
 */
@Slf4j
@Service
public class CalculateSumMethod extends AbstractVelCalculateMethod {
	@Override
	protected String getCalculateMethod() {
		return VelocityCaluateTypeEnum.SUM.getCode();
	}

	@Override
	public VelCalculateResult doCalculate(VelReadEvent dto) {
		if (null == dto || CollectionUtils.isEmpty(dto.getMasterValues())) {
			return new VelCalculateResult(BigDecimal.ZERO, "");
		}
		// 主维度个数大于1，不支持求和
		if (dto.getMasterValues()
				.size() > 1) {
			log.error(
					"[VelCalculateMethod] velocity factor config error,sum not support master more than one，txnId={}, factorCode={}",
					dto.getTxnId(), dto.getFactorCode());
			return new VelCalculateResult(BigDecimal.ZERO, "");
		}

		VelEventData eventDataDto = new VelEventData();
		BeanUtils.copyProperties(dto, eventDataDto);

		if (CollectionUtils.isEmpty(dto.getSlaveValues())) {
			return new VelCalculateResult(BigDecimal.ZERO, "");
		}
		eventDataDto.setSlaveValue(dto.getSlaveValues().get(0));
		eventDataDto.setMasterValue(dto.getMasterValues()
				.get(0));
		VelWindowData dataDto = VelDataSunkFactory.getService(dto.getTimeMode())
				.getVelData(eventDataDto);
		if (null == dataDto) {
			return new VelCalculateResult(BigDecimal.ZERO, "");
		}
		Map<String, List<VelValueItem>> dataMap = dataDto.getAfterData();
		filterData(dto, dataDto.getAfterData());

		BigDecimal result = BigDecimal.ZERO;
		if (dataMap != null) {
			result = dataMap.values()
					.stream()
					.flatMap(List::stream)
					.map(VelValueItem::getV)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		}
		return new VelCalculateResult(result, JSON.toJSONString(dataMap));
	}
}
