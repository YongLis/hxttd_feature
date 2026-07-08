package com.ly.ttd.feature.srv.factor.velocity.impl;

import com.ly.ttd.connector.api.ConnectorResponse;
import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.common.ctx.TxnParamContext;
import com.ly.ttd.feature.common.enums.ExecuteStateEnum;
import com.ly.ttd.feature.common.enums.FactorTypeEnum;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.common.language.ScriptVariable;
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
import com.ly.ttd.language.srv.ScriptLanguageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
     * @throws FeatureBizException 当因子未找到或计算失败时抛出异常
     *                             <p>
     *                             方法逻辑：
     *                             1. 根据factorCode从缓存中获取因子配置
     *                             2. 如果配置不存在，抛出"factor not found"异常
     *                             3. 尝试从Redis缓存中获取因子值
     *                             4. 如果缓存命中，直接返回反序列化后的值
     *                             5. 如果缓存未命中，则异步执行字段服务获取值
     *                             6. 如果异步执行失败，则根据配置进行异常处理
     *                             7. 将最终结果存入Redis缓存
     */
    @Override
    public Object getNativeFactorValue(String factorCode, TxnParamContext ctx) throws FeatureBizException {
        return doGetNativeFactorValue(factorCode, ctx);
    }

    private Object doGetNativeFactorValue(String factorCode, TxnParamContext ctx) throws FeatureBizException {
        MetaFactorModel configDto = featureConfiguration.getMetaFactorMap().get(factorCode);
        if (null == configDto) {
            throw new FeatureBizException("01", "factor not found");
        }
        Object cacheVal = ctx.get(factorCode);
        if (null != cacheVal) {
            return cacheVal;
        }
        Object res = accessMetaFieldCalculate.loadValue(configDto.getMetaFieldCode(), ctx);
        ctx.put(factorCode, res);
        return res;
    }

    private Object doGetDerivativeFactorValue(String factorCode, TxnParamContext ctx) throws FeatureBizException {
        long startTime = System.currentTimeMillis();
        DerivativeFactorModel configDto = featureConfiguration.getDerivativeFactorMap().get(factorCode);
        if (null == configDto) {
            throw new FeatureBizException("01", "factor not found");
        }
        Object cacheVal = ctx.get(factorCode);
        if (null != cacheVal) {
            return cacheVal;
        }

        // 循环依赖检测
        if (!ctx.getComputingSet().add(factorCode)) {
            throw new FeatureBizException("01", "factor is cycle reference: " + factorCode);
        }

        // 拓扑层级并行解析依赖
        if (CollectionUtils.isNotEmpty(configDto.getRefFactorList())) {
            for (List<String> refs : configDto.getRefFactorList()) {
                List<CompletableFuture<Void>> futures = new ArrayList<>();
                for (String refCode : refs) {
                    if (!ctx.getComputingSet().contains(refCode)) {
                        futures.add(resolveFactorAsync(refCode, ctx));
                    }
                }
                if (!futures.isEmpty()) {
                    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                }
            }
        }

        // 超时降级执行自身计算
        Object result = FallBackExecutor.getWithTimeout(
                () -> computeDerivedFactorValue(configDto, ctx),
                null,
                configDto.getTimeout(),
                TimeUnit.MILLISECONDS,
                configDto.getDefaultValue(),
                configDto.getExceptionValue());
        ctx.put(factorCode, result);
        log.info("[FactorCalculate] derived factor get value，factorCode：{}，result：{}, cost={}ms", factorCode, result,
                System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * 执行衍生因子自身计算（条件脚本 + 连接器 + 计算脚本）
     */
    private Object computeDerivedFactorValue(DerivativeFactorModel configDto, TxnParamContext ctx) {
        Map<String, Object> factorScriptMap = ctx.getParamByFields(configDto.getFactorCodes());
        if (!executeConditionScript(factorScriptMap, configDto, ctx)) {
            return ValueConvertor.convert(configDto.getReturnType(), configDto.getDefaultValue());
        }
        if (StringUtils.isNoneEmpty(configDto.getConnectorCode())) {
            Map<String, Object> connectorMap = connectCalculate(configDto.getConnectorCode(), ctx);
            factorScriptMap.putAll(connectorMap);
        }
        return executeCalculateScript(factorScriptMap, configDto, ctx);
    }

    private String getFactorType(String factorCode) {
        if (featureConfiguration.getMetaFactorMap().containsKey(factorCode)) {
            return FactorTypeEnum.META.getCode();
        }
        if (featureConfiguration.getDerivativeFactorMap().containsKey(factorCode)) {
            return FactorTypeEnum.DERIVATIVE.getCode();
        }
        if (featureConfiguration.getFeatureFactorMap().containsKey(factorCode)) {
            return FactorTypeEnum.FEATURE.getCode();
        }


        return null;
    }

    private Object doGetFeatureFactorValue(String factorCode, TxnParamContext ctx) throws FeatureBizException {
        FeatureFactorModel configDto = featureConfiguration.getFeatureFactorMap().get(factorCode);
        if (null == configDto) {
            throw new FeatureBizException("01", "factor not found");
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

    /**
     * 异步解析依赖因子，使用 ForkJoinPool 避免业务线程池死锁
     */
    private CompletableFuture<Void> resolveFactorAsync(String factorCode, TxnParamContext ctx) {
        return CompletableFuture.runAsync(() -> {
            String factorType = getFactorType(factorCode);
            if (FactorTypeEnum.META.getCode().equals(factorType)) {
                doGetNativeFactorValue(factorCode, ctx);
            } else if (FactorTypeEnum.DERIVATIVE.getCode().equals(factorType)) {
                doGetDerivativeFactorValue(factorCode, ctx);
            } else if (FactorTypeEnum.FEATURE.getCode().equals(factorType)) {
                doGetFeatureFactorValue(factorCode, ctx);
            }
        });
    }


    /**
     * 获取衍生因子值
     *
     * @param factorCode 因子定义键
     * @param ctx        支付RCS参数上下文
     * @return 衍生因子值
     * @throws FeatureBizException 当因子未找到或计算失败时抛出异常
     *                             <p>
     *                             方法逻辑：
     *                             1. 根据factorCode从缓存中获取因子配置
     *                             2. 如果配置不存在，抛出"factor not found"异常
     *                             3. 尝试从Redis缓存中获取因子值
     *                             4. 如果缓存命中，直接返回反序列化后的值
     *                             5. 如果缓存未命中，则计算依赖因子值
     *                             6. 将最终结果存入Redis缓存
     *                             7. 记录计算耗时日志
     */
    @Override
    public Object getDerivativeFactorValue(String factorCode, TxnParamContext ctx) throws FeatureBizException {
        return doGetDerivativeFactorValue(factorCode, ctx);
    }

    /**
     * 执行前置脚本
     */
    private boolean executeConditionScript(Map<String, Object> factorScriptMap, DerivativeFactorModel configDto, TxnParamContext ctx) {
        if (StringUtils.isEmpty(configDto.getConditionScript())) {
            return true;
        }

        ScriptVariable scriptVariable = new ScriptVariable();
        scriptVariable.setResourceKey(configDto.getResourceKey());
        scriptVariable.setLang(configDto.getLanguage());
        scriptVariable.setScript(configDto.getConditionScript());
        scriptVariable.setParams(factorScriptMap);
        Object res = scriptLanguageService.execute(scriptVariable);
        return null == res ? false : (Boolean) res;
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
        return scriptLanguageService.execute(scriptVariable);
    }


    /**
     * 连接计算
     */
    private Map<String, Object> connectCalculate(String connectorId, TxnParamContext ctx) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> connectorMap = new HashMap<>();

        ConnectorResponse response = connectorService.execute(new ConnectorReq(connectorId, ctx));
        if (null != response) {
            if (ExecuteStateEnum.ERROR.equals(response.getState())) {
                throw new RuntimeException("connector execute error");
            }
            connectorMap.put(connectorId, response.getRes());
        } else {
            connectorMap.put(connectorId, new HashMap<>());
        }
        res.put("Connector", connectorMap);

        return res;

    }

    /**
     * 获取速度因子值
     *
     * @param factorCode 因子定义键
     * @param ctx        支付RCS参数上下文
     * @return 速度因子值
     * @throws FeatureBizException 当因子未找到或计算失败时抛出异常
     *                             <p>
     *                             方法逻辑：
     *                             1. 根据definitionKey从缓存中获取因子配置
     *                             2. 如果配置不存在，抛出"factor not found"异常
     *                             3. 尝试从Redis缓存中获取因子值
     *                             4. 如果缓存命中，直接返回反序列化后的值
     *                             5. 如果缓存未命中，则异步执行速度管理服务获取值
     *                             6. 如果异步执行失败，则根据配置进行异常处理
     *                             7. 将最终结果存入Redis缓存
     */
    @Override
    public Object getFeatureFactorValue(String factorCode, TxnParamContext ctx) throws FeatureBizException {
        return doGetFeatureFactorValue(factorCode, ctx);
    }


    @Override
    public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }
}
