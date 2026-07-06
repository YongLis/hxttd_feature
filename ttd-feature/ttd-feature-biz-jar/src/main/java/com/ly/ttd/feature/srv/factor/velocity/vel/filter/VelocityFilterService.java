package com.ly.ttd.feature.srv.factor.velocity.vel.filter;

import com.ly.ttd.feature.common.ctx.TxnParamContext;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.common.model.vel.FeatureConfigModel;
import com.ly.ttd.feature.srv.factor.velocity.dto.FeatureScriptResult;

import java.util.List;
import java.util.Map;

/**
 * 实时特征过滤服务
 *
 * @author yong.li
 * @since 2026/4/29 16:37
 */
public interface VelocityFilterService {

    /**
     * 过滤实时特征前置条件
     */
    List<VelEventData> buildVelEventData(TxnParamContext ctx);

    List<VelEventData> buildVelEventData(TxnParamContext ctx, FeatureConfigModel velocityDto,
                                         Map<String, Object> metaValues);


    /**
     * 获取前置脚本执行结果
     */
    FeatureScriptResult executeScript(TxnParamContext ctx, FeatureConfigModel configModel, Map<String, Object> params);


}
