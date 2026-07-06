package com.ly.ttd.feature.srv.factor.velocity.impl;

import com.ly.ttd.api.language.ScriptVariable;
import com.ly.ttd.connector.api.ConnectorResponse;
import com.ly.ttd.consts.enums.ExecuteState;
import com.ly.ttd.consts.exception.BizException;
import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.cfg.ThreadPoolNames;
import com.ly.ttd.feature.common.ctx.TxnParamContext;
import com.ly.ttd.feature.common.model.factor.DerivativeFactorModel;
import com.ly.ttd.feature.common.model.factor.FeatureFactorModel;
import com.ly.ttd.feature.common.model.factor.MetaFactorModel;
import com.ly.ttd.feature.srv.connector.ConnectorService;
import com.ly.ttd.feature.srv.connector.req.ConnectorReq;
import com.ly.ttd.feature.srv.factor.velocity.FactorCalculateService;
import com.ly.ttd.feature.srv.factor.velocity.VelocityAdminService;
import com.ly.ttd.feature.srv.fallback.FallBackExecutor;
import com.ly.ttd.feature.srv.fallback.ValueConvertor;
import com.ly.ttd.feature.srv.meta.AccessMetaFieldCalculate;
import com.ly.ttd.feature.srv.vel.compile.FieldCodeValue;
import com.ly.ttd.language.srv.ScriptLanguageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author yong.li
 * @since 2026/4/25 09:44
 */
@Service
@Slf4j
public class FactorCalculateServiceImpl implements FactorCalculateService, FeatureConfigurationAware {
    public static final int DEFAULT_TIMEOUT = 60;
    public static final String TXN_FACTOR_CACHE = "pay:rcs:factor:cache:%s:%s:%s";

    @Resource
    private AccessMetaFieldCalculate accessMetaFieldCalculate;
    @Resource
    private VelocityAdminService velocityAdminService;
    @Resource
    private ConnectorService connectorService;
    @Resource
    private ScriptLanguageService scriptLanguageService;

    private FeatureConfiguration featureConfiguration;

    /**
     * 获取原生因子值
     *
     * @param factorCode 因子定义键
     * @param ctx        支付RCS参数上下文
     * @return 因子值
     * @throws BizException 当因子未找到或计算失败时抛出异常
     *                      <p>
     *                      方法逻辑：
     *                      1. 根据factorCode从缓存中获取因子配置
     *                      2. 如果配置不存在，抛出"factor not found"异常
     *                      3. 尝试从Redis缓存中获取因子值
     *                      4. 如果缓存命中，直接返回反序列化后的值
     *                      5. 如果缓存未命中，则异步执行字段服务获取值
     *                      6. 如果异步执行失败，则根据配置进行异常处理
     *                      7. 将最终结果存入Redis缓存
     */
    @Override
    public Object getNativeFactorValue(String factorCode, TxnParamContext ctx) throws BizException {
        MetaFactorModel configDto = featureConfiguration.getMetaFactorMap().get(factorCode);
        if (null == configDto) {
            throw new BizException("01", "factor not found");
        }
        Object cacheVal = ctx.get(factorCode);
        if (null != cacheVal) {
            return cacheVal;
        }


        Object res = accessMetaFieldCalculate.loadValue(configDto.getMetaFieldCode(), ctx);
        if (null != res) {
            ctx.put(factorCode, res);
        }
        return res;
    }

    /**
     * 获取衍生因子值
     *
     * @param factorCode 因子定义键
     * @param ctx        支付RCS参数上下文
     * @return 衍生因子值
     * @throws BizException 当因子未找到或计算失败时抛出异常
     *                      <p>
     *                      方法逻辑：
     *                      1. 根据factorCode从缓存中获取因子配置
     *                      2. 如果配置不存在，抛出"factor not found"异常
     *                      3. 尝试从Redis缓存中获取因子值
     *                      4. 如果缓存命中，直接返回反序列化后的值
     *                      5. 如果缓存未命中，则计算依赖因子值
     *                      6. 将最终结果存入Redis缓存
     *                      7. 记录计算耗时日志
     */
    @Override
    public Object getDerivativeFactorValue(String factorCode, TxnParamContext ctx) throws BizException {

        long startTime = System.currentTimeMillis();
        DerivativeFactorModel configDto = featureConfiguration.getDerivativeFactorMap().get(factorCode);
        if (null == configDto) {
            throw new BizException("01", "factor not found");
        }
        Object cacheVal = ctx.get(factorCode);
        if (null != cacheVal) {
            return cacheVal;
        }

        // 获取依赖因子值
        calculateDerivedFactorValue(factorCode, ctx);

        Object res = ctx.get(factorCode);

        if (null != res) {
            ctx.put(factorCode, res);
        }
        log.info("[FactorCalculate] derived factor get value，factorCode：{}，result：{}, cost={}ms", factorCode, res,
                System.currentTimeMillis() - startTime);
        return res;
    }

    private void calculateDerivedFactorValue(String parentFactorCode, TxnParamContext ctx) throws BizException {

        if (ctx.existKey(parentFactorCode)) {
            return;
        }

        DerivativeFactorModel configDto = featureConfiguration.getDerivativeFactorMap().get(parentFactorCode);
        for (List<String> refs : configDto.getRefFactorList()) {
            List<CompletableFuture<FieldCodeValue>> completableFutures = new ArrayList<>();
            for (String refCode : refs) {
                if (!ctx.existKey(refCode)) {
                    completableFutures.add(buildFactorValueDto(refCode, ctx));
                }
            }
            CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
            completableFutures.stream().forEach(t -> {
                FieldCodeValue valueDto = t.join();
                ctx.put(valueDto.getCode(), valueDto.getValue());
            });
        }
        // 计算当前因子值
        Object currentValue = FallBackExecutor.getWithTimeout(() -> getDerivativeFactorValue(parentFactorCode, configDto, ctx),
                configDto.getTimeout(),
                TimeUnit.MILLISECONDS,
                configDto.getDefaultValue(),
                configDto.getExceptionValue());
        ctx.put(parentFactorCode, currentValue);
    }

    private Object getDerivativeFactorValue(String parentFactorCode, DerivativeFactorModel configDto, TxnParamContext ctx) throws BizException {
        Map<String, Object> factorScriptMap = ctx.getParamByFields(configDto.getFactorCodes());
        if (!executeConditionScript(factorScriptMap, configDto, ctx)) {
            //  前置条件不成功，返回默认值
            Object defaultValue = ValueConvertor.convert(configDto.getReturnType(), configDto.getDefaultValue());
            ctx.put(parentFactorCode, defaultValue);
            return defaultValue;
        }

        if (StringUtils.isNoneEmpty(configDto.getConnectorCode())) {
            Map<String, Object> connectorMap = connectCalculate(configDto.getConnectorCode(), ctx);
            factorScriptMap.putAll(connectorMap);
        }
        return executeCalculateScript(factorScriptMap, configDto, ctx);
    }

    private DerivativeFactorModel getDerivativeFactorModel(String factorCode) {
        return featureConfiguration.getDerivativeFactorMap().get(factorCode);
    }

    /**
     * 执行前置脚本
     */
    private boolean executeConditionScript(Map<String, Object> factorScriptMap, DerivativeFactorModel configDto, TxnParamContext ctx) {
        ScriptVariable scriptVariable = new ScriptVariable();
        scriptVariable.setResourceKey(configDto.getResourceKey());
        scriptVariable.setLang(configDto.getLanguage());
        scriptVariable.setScript(configDto.getConditionScript());
        scriptVariable.setParams(factorScriptMap);
        scriptVariable.setBizOrderNo(ctx.getTxnId());
        scriptVariable.setTxnTime(ctx.getTxnTime());
        return (Boolean) scriptLanguageService.execute(scriptVariable);
    }

    /**
     * 执行计算脚本
     */
    private Object executeCalculateScript(Map<String, Object> factorScriptMap, DerivativeFactorModel configDto, TxnParamContext ctx) {
        ScriptVariable scriptVariable = new ScriptVariable();
        scriptVariable.setResourceKey(configDto.getResourceKey());
        scriptVariable.setLang(configDto.getLanguage());
        scriptVariable.setScript(configDto.getScript());
        scriptVariable.setParams(factorScriptMap);
        scriptVariable.setBizOrderNo(ctx.getTxnId());
        scriptVariable.setTxnTime(ctx.getTxnTime());
        return scriptLanguageService.execute(scriptVariable);
    }


    /**
     * 连接计算
     */
    private Map<String, Object> connectCalculate(String connectorId, TxnParamContext ctx) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> connectorMap = new HashMap<>();

        ConnectorResponse response = connectorService.execute(new ConnectorReq(connectorId, ctx));
        if (ExecuteState.FAIL.equals(response.getState())) {
            throw new RuntimeException("connector execute error");
        }
        connectorMap.put(connectorId, response.getRes());
        res.put("Connector", connectorMap);
        return res;

    }

    private CompletableFuture<FieldCodeValue> buildFactorValueDto(String factorCode, TxnParamContext ctx) {
        return CompletableFuture.supplyAsync(() -> {
            FieldCodeValue fieldCodeValue = new FieldCodeValue();
            fieldCodeValue.setCode(factorCode);
            if (null != featureConfiguration.getMetaFactorMap().get(factorCode)) {
                fieldCodeValue.setValue(getNativeFactorValue(factorCode, ctx));
            } else if (null != featureConfiguration.getDerivativeFactorMap().get(factorCode)) {
                fieldCodeValue.setValue(getDerivativeFactorValue(factorCode, ctx));
            } else {
                fieldCodeValue.setValue(getFeatureFactorValue(factorCode, ctx));
            }
            return fieldCodeValue;
        }, featureConfiguration.getThreadPool(ThreadPoolNames.THREAD_FACTOR_GET_VALUE));
    }

    /**
     * 获取速度因子值
     *
     * @param factorCode 因子定义键
     * @param ctx        支付RCS参数上下文
     * @return 速度因子值
     * @throws BizException 当因子未找到或计算失败时抛出异常
     *                      <p>
     *                      方法逻辑：
     *                      1. 根据definitionKey从缓存中获取因子配置
     *                      2. 如果配置不存在，抛出"factor not found"异常
     *                      3. 尝试从Redis缓存中获取因子值
     *                      4. 如果缓存命中，直接返回反序列化后的值
     *                      5. 如果缓存未命中，则异步执行速度管理服务获取值
     *                      6. 如果异步执行失败，则根据配置进行异常处理
     *                      7. 将最终结果存入Redis缓存
     */
    @Override
    public Object getFeatureFactorValue(String factorCode, TxnParamContext ctx) throws BizException {
        FeatureFactorModel configDto = featureConfiguration.getFeatureFactorMap().get(factorCode);
        if (null == configDto) {
            throw new BizException("01", "factor not found");
        }
        Object cacheVal = ctx.get(factorCode);
        if (null != cacheVal) {
            return cacheVal;
        }

        // 计算衍生因子值、超时、异常处理
        Object res = velocityAdminService.readVel(factorCode, ctx);
        if (null != res) {
            ctx.put(factorCode, res);
        }
        return res;
    }


    @Override
    public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }
}
