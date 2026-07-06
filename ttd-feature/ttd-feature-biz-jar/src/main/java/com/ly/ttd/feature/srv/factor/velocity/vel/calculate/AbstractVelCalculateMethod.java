package com.ly.ttd.feature.srv.factor.velocity.vel.calculate;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.feature.common.event.doris.VelReadEvent;
import com.ly.ttd.feature.common.event.doris.VelReadSnapshot;
import com.ly.ttd.feature.common.event.dto.VelCalculateResult;
import com.ly.ttd.feature.common.event.dto.VelValueItem;
import com.ly.ttd.feature.consts.FeatureTraceTableEnum;
import com.ly.ttd.feature.srv.data.TraceDataSaveService;
import com.ly.ttd.feature.srv.data.TraceMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 实时特征计算方法
 *
 * @author yong.li
 * @since 2026/4/23 16:44
 */
@Component
@Slf4j
public abstract class AbstractVelCalculateMethod {
	@Resource
	private TraceDataSaveService traceDataSaveService;

	/**
	 * 获取计算方法
	 */
	protected abstract String getCalculateMethod();

	/**
	 * 过滤实时特征结果并计算
	 */
	protected abstract VelCalculateResult doCalculate(VelReadEvent dto);

	/**
	 * 实时特征查询
	 */
	public VelCalculateResult readVel(VelReadEvent dto, String runMode) {
		VelCalculateResult result = doCalculate(dto);
		VelReadSnapshot snapshotDto = new VelReadSnapshot();
		snapshotDto.setFactorCode(dto.getFactorCode());
		snapshotDto.setFactorName(dto.getFactorName());
		snapshotDto.setFactorCode(dto.getFactorCode());
		snapshotDto.setMasterValue(dto.getMasterValues());
		snapshotDto.setTxnId(dto.getTxnId());
		VelReadSnapshot.ReadSnapshotDetail detail = new VelReadSnapshot.ReadSnapshotDetail(dto, result.getSampleSnapshot());
		snapshotDto.setData(JSON.toJSONString(detail));
		snapshotDto.setValue(result.getResult());
		snapshotDto.setExpireTime(DateUtils.addMonths(new Date(), 6));
		snapshotDto.setQueryTime(new Date());
		snapshotDto.setPointCode(dto.getPointCode());


		TraceMessage traceMessage = new TraceMessage(FeatureTraceTableEnum.VEL_READ_SNAPSHOT.getKafkaTopic()
				, JSON.toJSONString(snapshotDto));
		traceDataSaveService.save(traceMessage, runMode);

		return result;
	}

	/**
	 * （计数、求和专用）
	 * 计算时数据过滤，不计入当前的因子，排除当前交易
	 * 实时特征累计场景：
	 * 一对一
	 * 一对多
	 * 多对一
	 *
	 * @param dto
	 * @param dataMap <从维度值， 关联交易列表>
	 */
	protected void filterData(VelReadEvent dto, Map<String, List<VelValueItem>> dataMap) {
		if (MapUtils.isEmpty(dataMap)) {
			dataMap = new HashMap<>();
		}

		// 不计入当前的因子
		if ("0".equals(dto.getVelCurFlag())) {
			// 针对事前累计不计当前记录的因子，事中或事后查询时，需要排除当前交易
			if (CollectionUtils.isNotEmpty(dto.getSlaveValues())) {
				for (String slaveValue : dto.getSlaveValues()) {
					List<VelValueItem> velValueItems = dataMap.get(slaveValue);
					if (CollectionUtils.isNotEmpty(velValueItems)) { // 包含了当前交易的从维度
						velValueItems.removeIf(item -> item.getTxnId()
								.equals(dto.getTxnId()));
						if (CollectionUtils.isEmpty(velValueItems)) {
							dataMap.remove(slaveValue);
						} else {
							dataMap.put(slaveValue, velValueItems);
						}
					}
				}
			}
		} else {
			// 不排除当前交易
			// 针对事前累计计当前记录的因子，事中或事后查询时，需要防止重复计入
			if (CollectionUtils.isNotEmpty(dto.getSlaveValues())) {
				for (String slaveValue : dto.getSlaveValues()) {
					List<VelValueItem> velValueItems = dataMap.get(slaveValue);
					if (CollectionUtils.isNotEmpty(velValueItems)) {
						// 缓存包含当前从维度记录
						// 检查是否存在当前交易的记录，如果不存在，则计入
						boolean exist = velValueItems.stream()
								.anyMatch(t -> t.getTxnId()
										.equals(dto.getTxnId()));
						if (!exist) {
							VelValueItem velValueItem = new VelValueItem(dto.getTxnId(), dto.getTxnTime()
									.getTime(), dto.getVelValue());
							velValueItems.add(velValueItem);
							dataMap.put(slaveValue, velValueItems);
						}
					} else {
						// 缓存不存在当前从维度记录
						VelValueItem velValueItem = new VelValueItem(dto.getTxnId(), dto.getTxnTime()
								.getTime(), dto.getVelValue());
						List<VelValueItem> velValueItemList = new ArrayList<>();
						velValueItemList.add(velValueItem);
						dataMap.put(slaveValue, velValueItemList);
					}
				}
			}
		}
	}

	/**
	 * 最大值、最小值、平均值专用
	 * 计算时数据过滤，不计入当前的因子，排除当前交易
	 * 实时特征累计场景：
	 * 多对多按下标累计
	 *
	 * @param dto
	 * @param slaveValue 下标对应从维度值
	 * @param dataMap    <从维度值， 关联交易列表>
	 */
	protected void filterDataByMasterIndex(VelReadEvent dto, String slaveValue,
										   Map<String, List<VelValueItem>> dataMap) {
		// 不计入当前的因子
		if ("0".equals(dto.getVelCurFlag())) {
			// 针对事前累计不计当前记录的因子，事中或事后查询时，需要排除当前交易
			List<VelValueItem> velValueItems = dataMap.get(slaveValue);
			if (CollectionUtils.isNotEmpty(velValueItems)) { // 包含了当前交易的从维度
				velValueItems.removeIf(item -> item.getTxnId()
						.equals(dto.getTxnId()));
				if (CollectionUtils.isEmpty(velValueItems)) {
					dataMap.remove(slaveValue);
				} else {
					dataMap.put(slaveValue, velValueItems);
				}
			}

		} else {
			// 不排除当前交易
			// 针对事前累计计当前记录的因子，事中或事后查询时，需要防止重复计入
			List<VelValueItem> velValueItems = dataMap.get(slaveValue);
			if (CollectionUtils.isNotEmpty(velValueItems)) {
				// 缓存包含当前从维度记录
				// 检查是否存在当前交易的记录，如果不存在，则计入
				boolean exist = velValueItems.stream()
						.anyMatch(t -> t.getTxnId()
								.equals(dto.getTxnId()));
				if (!exist) {
					VelValueItem velValueItem = new VelValueItem(dto.getTxnId(), dto.getTxnTime()
							.getTime(), dto.getVelValue());
					velValueItems.add(velValueItem);
					dataMap.put(slaveValue, velValueItems);
				}
			} else {
				// 缓存不存在当前从维度记录
				VelValueItem velValueItem = new VelValueItem(dto.getTxnId(), dto.getTxnTime()
						.getTime(), dto.getVelValue());
				List<VelValueItem> velValueItemList = new ArrayList<>();
				velValueItemList.add(velValueItem);
				dataMap.put(slaveValue, velValueItemList);
			}
		}

	}

}
