package com.ly.ttd.feature.srv.factor.velocity;

import com.ly.ttd.feature.common.ctx.TxnParamContext;
import com.ly.ttd.feature.common.event.doris.VelEventData;

import java.math.BigDecimal;
import java.util.List;

/**
 * 实时特征操作服务
 *
 * @author yong.li
 * @since 2026/4/24 17:16
 */
public interface VelocityAdminService {

    /**
     * 实时特征读取
     */
    BigDecimal readVel(String definitionKey, TxnParamContext ctx);

    /**
     * 实时特征前置筛选与构建实时特征事件流
     *
     * @param ctx 上下文参数
     * @return 实时特征事件流
     */
    List<VelEventData> filterVelAndBuildEventData(TxnParamContext ctx);
}
