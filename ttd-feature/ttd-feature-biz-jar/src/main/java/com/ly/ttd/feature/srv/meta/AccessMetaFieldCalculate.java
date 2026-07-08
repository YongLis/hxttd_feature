package com.ly.ttd.feature.srv.meta;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.cfg.ThreadPoolNames;
import com.ly.ttd.feature.common.ctx.TxnParamContext;
import com.ly.ttd.feature.common.enums.ScriptType;
import com.ly.ttd.feature.common.language.ScriptVariable;
import com.ly.ttd.feature.common.model.meta.MetaFieldModel;
import com.ly.ttd.feature.srv.fallback.FallBackExecutor;
import com.ly.ttd.feature.srv.fallback.ValueConvertor;
import com.ly.ttd.feature.srv.vel.compile.FieldCodeValue;
import com.ly.ttd.language.srv.ScriptLanguageService;
import jakarta.annotation.Resource;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author yong.li
 * @since 2026/6/10 12:46
 */
@Slf4j
@Service
public class AccessMetaFieldCalculate implements FeatureConfigurationAware {
    private FeatureConfiguration featureConfiguration;

    @Resource
    private ScriptLanguageService scriptLanguageService;

    /**
     * 获取所有字段值，并发执行, 基本报文字段预加载
     */

    public Object loadValue(String fieldKey, TxnParamContext ctx) {
        try {
            return toLoadValue(fieldKey, ctx);
        } catch (Exception e) {
            // 不会走到
            return null;
        }
    }

    private Object toLoadValue(String fieldKey, TxnParamContext ctx) {
        Object object = ctx.get(fieldKey);
        if (null != object) {
            return object;
        } else {
            Object res = doLoadValue(fieldKey, ctx);
            ctx.put(fieldKey, res);
            ctx.getComputingSet().add(fieldKey);
            return res;
        }
    }

    public void batchLoadValue(TxnParamContext ctx) {
        triggerPointCodeMetaField(ctx.getPointCode(), ctx);
    }

    private Object doLoadValue(String fieldKey, TxnParamContext ctx) {
        if (StringUtil.isBlank(fieldKey) || StringUtil.isBlank(ctx.getTxnId())) {
            log.error("meta getValue fieldKey or txnId is null paramContext={}", JSON.toJSONString(ctx));
            return null;
        }
        MetaFieldModel field = featureConfiguration.getMetaFieldMap().get(fieldKey);
        if (field == null) {
            log.error("meta getValue metaFieldConfigDto is null,txnId={},fieldKey={},pointCode={}",
                    ctx.getTxnId(), fieldKey, ctx.getPointCode());
            return null;
        }

        ScriptVariable variable = new ScriptVariable();
        variable.setResourceKey(fieldKey);
        variable.setLang(ScriptType.GROOVY.getCode());
        variable.setScript(field.getScript());
        variable.setParams(ctx.getReq());

        return FallBackExecutor.getWithTimeout(() -> scriptLanguageService.execute(variable),
                featureConfiguration.getThreadPool(ThreadPoolNames.THREAD_META_FIELD),
                field.getTimeout(), TimeUnit.MILLISECONDS,
                ValueConvertor.convert(field.getReturnType(), field.getDefaultValue()),
                ValueConvertor.convert(field.getReturnType(), field.getExceptionValue()));
    }

    /**
     * 异步全量触发通接入点下的元字段
     */

    private void triggerPointCodeMetaField(String pointCode, TxnParamContext ctx) {

        List<CompletableFuture<FieldCodeValue>> futures = new ArrayList<>();

        List<String> pointCodeFields = featureConfiguration.getPointCodeMetaFieldMap().get(pointCode);

        if (pointCodeFields == null || pointCodeFields.isEmpty()) {
            return;
        }
        pointCodeFields
                .stream().filter(t -> !ctx.getComputingSet().contains(t))
                .forEach(x -> {
                    futures.add(triggerMetaField(x, ctx));
                });

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .join();
        futures.stream().forEach(t -> {
            FieldCodeValue dto = t.join();
            ctx.getComputingSet().add(dto.getCode());
            ctx.put(dto.getCode(), dto.getValue());
        });
    }

    private CompletableFuture<FieldCodeValue> triggerMetaField(String fieldKey, TxnParamContext ctx) {
        return CompletableFuture.supplyAsync(() -> {
            FieldCodeValue fieldDto = new FieldCodeValue();
            fieldDto.setCode(fieldKey);
            fieldDto.setValue(loadValue(fieldKey, ctx));
            return fieldDto;
        }, featureConfiguration.getThreadPool(ThreadPoolNames.THREAD_META_FIELD));
    }

    @Override
    public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }
}
