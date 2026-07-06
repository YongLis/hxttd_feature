package com.ly.ttd.feature.srv;

import com.ly.ttd.feature.common.ctx.TxnParamContext;

/**
 * 指标取值逻辑
 *
 * @author yong.li
 * @since 2026/4/22 14:51
 */
public interface FactorGetValueService {

    /**
     * 单个取值
     *
     * @param factorCode 指标编码
     * @param ctx        上下文参数
     * @return 因子值
     */
    Object getValue(String factorCode, TxnParamContext ctx);


}
