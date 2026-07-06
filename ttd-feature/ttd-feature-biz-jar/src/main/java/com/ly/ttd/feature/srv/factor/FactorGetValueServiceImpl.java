package com.ly.ttd.feature.srv.factor;

import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.common.ctx.TxnParamContext;
import com.ly.ttd.feature.common.enums.FactorTypeEnum;
import com.ly.ttd.feature.common.model.factor.MetaFactorModel;
import com.ly.ttd.feature.srv.FactorGetValueService;
import com.ly.ttd.feature.srv.factor.velocity.FactorCalculateService;
import com.ly.ttd.feature.srv.fallback.FallBackExecutor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author yong.li
 * @since 2026/4/22 14:56
 */
@Slf4j
@Service
public class FactorGetValueServiceImpl implements FactorGetValueService, FeatureConfigurationAware {

    @Resource
    private FactorCalculateService factorCalculateService;
    private FeatureConfiguration featureConfiguration;

    @Override
    public Object getValue(String factorCode, TxnParamContext ctx) {
        log.info("[FactorGetValue] factor get value start,txnId={}, factorCode={}", ctx.getTxnId(), factorCode);
        MetaFactorModel metaFactorConfig = featureConfiguration.getMetaFactorMap().get(factorCode);
        return FallBackExecutor.getWithTimeout(
                () -> getFactorValue(factorCode, ctx),
                metaFactorConfig.getTimeout(),
                TimeUnit.MILLISECONDS,
                metaFactorConfig.getDefaultValue(),
                metaFactorConfig.getExceptionValue());
    }

    private Object getFactorValue(String factorCode, TxnParamContext ctx) {
        MetaFactorModel metaFactorConfig = featureConfiguration.getMetaFactorMap().get(factorCode);
        if (null == metaFactorConfig) {
            throw new RuntimeException("factor config not found, txnId=" + ctx.getTxnId() + ",factorCode=" + factorCode);
        }

        Object result = null;
        try {
            if (FactorTypeEnum.META.getCode()
                    .equals(metaFactorConfig.getFactorType())) {
                result = factorCalculateService.getNativeFactorValue(factorCode, ctx);
            } else if (FactorTypeEnum.DERIVATIVE.getCode()
                    .equals(metaFactorConfig.getFactorType())) {
                result = factorCalculateService.getDerivativeFactorValue(factorCode, ctx);
            } else {
                result = factorCalculateService.getFeatureFactorValue(factorCode, ctx);
            }
        } catch (Exception e) {
            log.error("get factor value error,factorCode={}, txnId={}", factorCode, ctx.getTxnId(), e);
            return null;
        }
        return result;
    }

    @Override
    public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }
}
