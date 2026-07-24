package com.ly.ttd.feature.srv.factor.velocity.impl;

import com.ly.ttd.feature.common.ctx.TxnParamContext;
import com.ly.ttd.feature.srv.factor.velocity.MetaFieldQueryService;
import com.ly.ttd.feature.srv.meta.AccessMetaFieldCalculate;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/4/29 12:06
 */
@Service
@Slf4j
public class MetaFieldQueryServiceImpl implements MetaFieldQueryService {
    @Resource
    private AccessMetaFieldCalculate accessMetaFieldCalculate;


    @Override
    public Map<String, Object> getMetaFieldValue(List<String> metaFields, TxnParamContext ctx) {
        if (CollectionUtils.isEmpty(metaFields)) {
            return Map.of();
        }
        Map<String, Object> result = new HashMap<>();
        metaFields.forEach(t -> result.put(t, accessMetaFieldCalculate.loadValue(t, ctx)));
        return result;
    }
}
