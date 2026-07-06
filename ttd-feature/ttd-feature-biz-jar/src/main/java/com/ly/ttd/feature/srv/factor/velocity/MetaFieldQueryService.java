package com.ly.ttd.feature.srv.factor.velocity;

import com.ly.ttd.feature.common.ctx.TxnParamContext;

import java.util.List;
import java.util.Map;

/**
 * 元字段查询服务
 *
 * @author yong.li
 * @since 2026/4/29 12:05
 */
public interface MetaFieldQueryService {

    /**
     * 批量获取元字段值
     *
     * @param metaFieldSet 元字段集合
     * @return Map<String, Object>
     */
    Map<String, Object> getMetaFieldValue(List<String> metaFieldSet, TxnParamContext ctx);
}
