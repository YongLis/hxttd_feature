package com.ly.ttd.feature.srv.factor.velocity.handler;

import com.ly.ttd.feature.common.ctx.TxnParamContext;

/**
 * @author yong.li
 * @since 2026/4/25 10:14
 */
public interface FactorLocalGetValueService {
    /**
     * 获取值
     */
    Object getValue(String definitionKey, TxnParamContext ctx);

}
