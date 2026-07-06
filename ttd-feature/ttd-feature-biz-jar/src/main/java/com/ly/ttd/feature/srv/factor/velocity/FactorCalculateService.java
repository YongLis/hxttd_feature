package com.ly.ttd.feature.srv.factor.velocity;

import com.ly.ttd.consts.exception.BizException;
import com.ly.ttd.feature.common.ctx.TxnParamContext;

/**
 * 因子计算服务
 *
 * @author yong.li
 * @since 2026/4/25 09:43
 */
public interface FactorCalculateService {
    /**
     * 获取元字段指标值
     */
    Object getNativeFactorValue(String definitionKey, TxnParamContext ctx) throws BizException;


    /**
     * 获取衍生指标值
     */
    Object getDerivativeFactorValue(String definitionKey, TxnParamContext ctx) throws BizException;

    /**
     * 获取实时特征指标值
     */
    Object getFeatureFactorValue(String definitionKey, TxnParamContext ctx) throws BizException;
}
