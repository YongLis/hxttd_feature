package com.ly.ttd.feature.srv.factor.velocity.impl;

import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.common.ctx.TxnParamContext;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.common.event.doris.VelReadEvent;
import com.ly.ttd.feature.common.event.dto.VelCalculateResult;
import com.ly.ttd.feature.common.model.factor.FeatureFactorModel;
import com.ly.ttd.feature.common.model.vel.FeatureConfigModel;
import com.ly.ttd.feature.srv.factor.velocity.MetaFieldQueryService;
import com.ly.ttd.feature.srv.factor.velocity.VelocityAdminService;
import com.ly.ttd.feature.srv.factor.velocity.dto.FeatureScriptResult;
import com.ly.ttd.feature.srv.factor.velocity.vel.calculate.VelCalculateMethodFactory;
import com.ly.ttd.feature.srv.factor.velocity.vel.filter.VelocityFilterService;
import com.ly.ttd.language.srv.ScriptLanguageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/4/24 17:56
 */
@Slf4j
@Service
public class VelocityAdminServiceImpl implements VelocityAdminService, FeatureConfigurationAware {
	private FeatureConfiguration featureConfiguration;
	private static BigDecimal DEFAULT_VEL_VALUE = BigDecimal.valueOf(-999999);

	@Resource
	private MetaFieldQueryService metaFieldQueryService;

	@Resource
	private VelocityFilterService velocityFilterService;
	@Resource
	private ScriptLanguageService scriptLanguageService;

	@Override
	public BigDecimal readVel(String factorCode, TxnParamContext ctx) {
		long start = System.currentTimeMillis();
		try {
			FeatureFactorModel configDto = featureConfiguration.getFeatureFactorMap().get(factorCode);
			if (null == configDto) {
				log.error("readVel factorCode={} not found config", factorCode);
				return DEFAULT_VEL_VALUE;
			}
			FeatureConfigModel velocityConfigDto = featureConfiguration.getFeatureMap().get(configDto.getRefVelocityCode());
			if (null == velocityConfigDto) {
				log.error("readVel factorCode={} not found velocityConfig", factorCode);
				return DEFAULT_VEL_VALUE;
			}

			// 读取实时特征相关的元字段及数值
			Map<String, Object> param = ctx.buildScriptParams();

			Map<String, Object> metaFieldMap = metaFieldQueryService
					.getMetaFieldValue(velocityConfigDto.getMetaFields(), ctx);
			if (metaFieldMap == null || metaFieldMap.isEmpty()) {
				log.warn("readVel factorCode={} metaFieldMap is empty", factorCode);
				return DEFAULT_VEL_VALUE;
			}
			param.putAll(metaFieldMap);

			FeatureScriptResult scriptResult = velocityFilterService.executeScript(ctx, velocityConfigDto, param);

			// 拼接实时特征计算事件数据
			VelReadEvent dto = new VelReadEvent();
			dto.setFactorName(configDto.getResourceName());
			dto.setFactorCode(factorCode);
			dto.setVelCurFlag(scriptResult.getCondition() ? "1" : "0");
			dto.setPointCode(ctx.getPointCode());
			dto.setTxnId(ctx.getTxnId());
			dto.setTxnTime(ctx.getTxnTime());
			dto.setFeatureCode(configDto.getRefVelocityCode());
			dto.setMasterValues(scriptResult.getMaster());
			dto.setSlaveValues(scriptResult.getSlave());
			dto.setVelValue(scriptResult.getValue());
			dto.setAggregateMode(velocityConfigDto.getAggregateMode());
			dto.setTimeMode(velocityConfigDto.getTimeMode());
			dto.setTimeUnit(velocityConfigDto.getTimeUnit());
			dto.setTimeWindow(velocityConfigDto.getTimeWindow());

			VelCalculateResult result = VelCalculateMethodFactory.getMethod(velocityConfigDto.getAggregateMode())
					.readVel(dto, ctx.getRunMode());
			return result.getResult();
		} catch (Exception e) {
			log.error("readVel error, txnId={},factorCode={}, error={}", ctx.getTxnId(), factorCode,
					e.getMessage(), e);
		}
		return DEFAULT_VEL_VALUE;
	}

	@Override
	public List<VelEventData> filterVelAndBuildEventData(TxnParamContext ctx) {
		log.info("filterVelAndBuildEventData start, txnId={}", ctx.getTxnId());
		try {
			return velocityFilterService.buildVelEventData(ctx);
		} catch (Exception e) {
			log.error("filterVelAndBuildEventData error, txnId={}, error={}", ctx.getTxnId(), e.getMessage(), e);
			return new ArrayList<>();
		}
	}

	@Override
	public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
		this.featureConfiguration = featureConfiguration;
	}
}
