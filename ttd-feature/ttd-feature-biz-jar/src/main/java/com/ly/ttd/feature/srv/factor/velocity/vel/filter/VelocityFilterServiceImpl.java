package com.ly.ttd.feature.srv.factor.velocity.vel.filter;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.cfg.ThreadPoolNames;
import com.ly.ttd.feature.common.consts.TxnConsts;
import com.ly.ttd.feature.common.ctx.TxnParamContext;
import com.ly.ttd.feature.common.enums.ScriptType;
import com.ly.ttd.feature.common.enums.VelocityValueTypeEnum;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.common.language.ScriptVariable;
import com.ly.ttd.feature.common.model.vel.FeatureConfigModel;
import com.ly.ttd.feature.srv.factor.velocity.MetaFieldQueryService;
import com.ly.ttd.feature.srv.factor.velocity.dto.FeatureScriptResult;
import com.ly.ttd.feature.srv.factor.velocity.vel.sink.VelocityTimeOutBuilder;
import com.ly.ttd.feature.srv.vel.compile.FeatureConfigExecuteContext;
import com.ly.ttd.feature.srv.vel.compile.jexl3.LocalMapContext;
import com.ly.ttd.language.srv.ScriptLanguageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author yong.li
 * @since 2026/4/29 16:55
 */
@Service
@Slf4j
public class VelocityFilterServiceImpl implements VelocityFilterService, FeatureConfigurationAware {
    private FeatureConfiguration featureConfiguration;
    @Resource
    private MetaFieldQueryService metaFieldQueryService;

    @Resource
    private ScriptLanguageService scriptLanguageService;

    @Override
    public List<VelEventData> buildVelEventData(TxnParamContext context, FeatureConfigModel configModel,
                                                Map<String, Object> metaValues) {

        // 额外追加公共参数
        metaValues.put(TxnConsts.TXN_ID, context.getTxnId());
        metaValues.put(TxnConsts.POINT_CODE, context.getPointCode());
        LocalMapContext jexlContext = new LocalMapContext(metaValues);
        FeatureConfigExecuteContext executeContext = new FeatureConfigExecuteContext();
        executeContext.setOption("read");
        executeContext.setEndpointCode(context.getPointCode());
        executeContext.setTxnId(context.getTxnId());
        executeContext.setTxnTime(null == context.getTxnTime() ? new Date() : context.getTxnTime());
        executeContext.setConfigModel(configModel);
        executeContext.setContext(jexlContext);
        executeContext.setParams(metaValues);
        return filterData(executeContext);

    }

    @Override
    public FeatureScriptResult executeScript(TxnParamContext ctx, FeatureConfigModel configModel, Map<String, Object> params) {
        ScriptVariable condVariable = buildFeatureVariable(ctx, configModel.getConditionScript(), params);
        ScriptVariable masterVariable = buildFeatureVariable(ctx, configModel.getMainDimScript(), params);
        ScriptVariable slaveVariable = buildFeatureVariable(ctx, configModel.getSlaveDimScript(), params);

        FeatureScriptResult result = new FeatureScriptResult();
        result.setCondition((Boolean) scriptLanguageService.execute(condVariable));
        Object masterValue = scriptLanguageService.execute(masterVariable);
        Object slaveValue = scriptLanguageService.execute(slaveVariable);

        result.setMaster(formatReturnValue(masterValue));
        result.setSlave(formatReturnValue(slaveValue));

        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(configModel.getValueType())) {
            ScriptVariable valVariable = buildFeatureVariable(ctx, configModel.getValueScript(), params);
            result.setValue((new BigDecimal(scriptLanguageService.execute(valVariable).toString())));
        } else {
            result.setValue(new BigDecimal(configModel.getFixValue()));
        }
        return result;
    }

    private List<String> formatReturnValue(Object value) {
        if (null == value) {
            return Collections.emptyList();
        }
        if (value instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) value;
            return collection.stream().map(Object::toString).collect(Collectors.toList());
        }
        return Collections.singletonList(value.toString());
    }

    private ScriptVariable buildFeatureVariable(TxnParamContext ctx, String script, Map<String, Object> params) {
        ScriptVariable variable = new ScriptVariable();
        variable.setLang(ScriptType.JEXL.getCode());
        variable.setScript(script);
        variable.setParams(params);
        return variable;
    }


    @Override
    public List<VelEventData> buildVelEventData(TxnParamContext context) {
        long start = System.currentTimeMillis();
        String txnId = context.getTxnId();
        String pointCode = context.getPointCode();

        log.info(" FilterVelocityConfigTask start, pointCode: {}, txnId: {}", pointCode, txnId);

        // 获取项目下所有的实时特征配置
        List<FeatureConfigModel> dtoList = featureConfiguration.getProjectFeatureMap().get(context.getProjectId());
        if (dtoList == null || dtoList.isEmpty()) {
            log.info(" FilterVelocityConfigTask filter config success, pointCode: {}, txnId: {}",
                    pointCode, txnId);
            return new ArrayList<>();
        }

        List<FeatureConfigExecuteContext> contextList = new ArrayList<>();
        List<CompletableFuture<List<VelEventData>>> futureList = new ArrayList<>();

        Map<String, String> factorVelocityMap = getFactorVelocityCode();
        for (FeatureConfigModel dto : dtoList) {
            String definitionKey = factorVelocityMap.get(dto.getFeatureCode());
            Map<String, Object> tmpMap = metaFieldQueryService.getMetaFieldValue(dto.getMetaFields(), context);
            if (tmpMap == null || tmpMap.isEmpty()) {
                log.warn(" get meta field fail, pointCode: {}, txnId: {},  featureCode: {}, definitionKey={}",
                        context.getPointCode(), context.getTxnId(), dto.getFeatureCode(), definitionKey);
                continue;
            }

            // 额外追加公共参数
            tmpMap.put(TxnConsts.TXN_ID, context.getTxnId());
            tmpMap.put(TxnConsts.POINT_CODE, context.getPointCode());
            // jexl函数调用，不可删除
            LocalMapContext jexlContext = new LocalMapContext(tmpMap);
            FeatureConfigExecuteContext executeContext = new FeatureConfigExecuteContext();
            executeContext.setEndpointCode(context.getPointCode());
            executeContext.setTxnId(context.getTxnId());
            executeContext.setTxnTime(null == context.getTxnTime() ? new Date() : context.getTxnTime());
            executeContext.setConfigModel(featureConfiguration.getFeatureMap().get(dto.getFeatureCode()));
            executeContext.setContext(jexlContext);
            executeContext.setParams(tmpMap);
            contextList.add(executeContext);
        }

        if (CollectionUtils.isEmpty(contextList)) {
            log.warn(" FilterVelocityConfigTask filter config fail, pointCode: {}, txnId: {}",
                    context.getPointCode(), context.getTxnId());
            return new ArrayList<>();
        }

        for (FeatureConfigExecuteContext ctx : contextList) {
            CompletableFuture<List<VelEventData>> future = CompletableFuture.supplyAsync(() -> {
                // 异步执行实时特征前置条件
                return filterVelConditionAndBuildEventData(ctx);
            }, featureConfiguration.getThreadPool(ThreadPoolNames.THREAD_FEATURE_FEATURES));
            futureList.add(future);
        }

        // 获取执行结果
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]))
                .join();
        List<VelEventData> resultList = new ArrayList<>();
        futureList.forEach(t -> {
            try {
                List<VelEventData> tmpList = t.get();
                if (CollectionUtils.isNotEmpty(tmpList)) {
                    for (VelEventData dto : tmpList) {
                        String definitionKey = factorVelocityMap.get(dto.getFeatureCode());
                        if (StringUtils.isBlank(dto.getMasterValue()) || StringUtils.isBlank(dto.getSlaveValue())) {
                            log.warn("master value  or slave value is null, txnId: {}, featureCode: {}, masterValue: {}, slaveValue: {}, definitionKey={}",
                                    context.getTxnId(), dto.getFeatureCode(), dto.getMasterValue(),
                                    dto.getSlaveValue(), definitionKey);
                            continue;
                        }
                        resultList.add(dto);
                    }
                }
            } catch (Exception e) {
                log.error(" FilterVelocityConfigTask error, pointCode: {}, txnId: {}",
                        context.getPointCode(), context.getTxnId(), e);
            }
        });

        log.info(" FilterVelocityConfigTask end, pointCode: {}, txnId: {}, cost: {}",
                context.getPointCode(), context.getTxnId(), System.currentTimeMillis() - start);

        return resultList;
    }

    private Map<String, String> getFactorVelocityCode() {
        Map<String, String> map = new HashMap<>();
        featureConfiguration.getFeatureFactorMap()
                .forEach((k, v) -> map.put(v.getRefVelocityCode(), k));

        return map;
    }

    private List<VelEventData> filterVelConditionAndBuildEventData(FeatureConfigExecuteContext executor) {
        List<VelEventData> velEventDataList = new ArrayList<>();
        List<VelEventData> filterDatas = filterData(executor);
        if (CollectionUtils.isNotEmpty(filterDatas)) {
            velEventDataList.addAll(filterDatas);
        }
        return velEventDataList;
    }

    private List<VelEventData> filterData(FeatureConfigExecuteContext ctx) {
        List<VelEventData> velEventDataList = new ArrayList<>();
        try {
            ScriptVariable condVariable = buildScriptVariable(ctx, ctx.getConfigModel().getConditionScript());
            Boolean bool = (Boolean) scriptLanguageService.execute(condVariable);

            if (Boolean.FALSE.equals(bool)) {
                return velEventDataList;
            }

            Object masterVal = scriptLanguageService.execute(buildScriptVariable(ctx, ctx.getConfigModel().getMainDimScript()));
            Object slaveVal = scriptLanguageService.execute(buildScriptVariable(ctx, ctx.getConfigModel().getSlaveDimScript()));

            List<String> masterValues = new ArrayList<>();
            List<String> slaveValues = new ArrayList<>();
            if (masterVal instanceof Collection) {
                masterValues.addAll((Collection<String>) masterVal);
            } else {
                masterValues.add((String) masterVal);
            }

            if (slaveVal instanceof Collection) {
                slaveValues.addAll((Collection<? extends String>) slaveVal);
            } else {
                slaveValues.add((String) slaveVal);
            }
            if (CollectionUtils.isNotEmpty(masterValues) && CollectionUtils.isNotEmpty(slaveValues)) {
                BigDecimal value = new BigDecimal(ctx.getConfigModel().getFixValue());
                if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(ctx.getConfigModel().getValueType())) {
                    value = (BigDecimal) scriptLanguageService.execute(buildScriptVariable(ctx, ctx.getConfigModel().getValueScript()));
                }

                if (masterValues.size() == 1) {
                    for (String slaveValue : slaveValues) {
                        VelEventData dto = createVelEventDataDto(ctx, slaveValue, masterValues.get(0),
                                value);
                        velEventDataList.add(dto);
                    }
                } else if (masterValues.size() > 1 && slaveValues.size() == 1) {
                    // 多对一
                    for (String masterValue : masterValues) {
                        VelEventData dto = createVelEventDataDto(ctx, slaveValues.get(0), masterValue,
                                value);
                        velEventDataList.add(dto);
                    }
                } else if (masterValues.size() > 1 && slaveValues.size() > 1
                        && masterValues.size() == slaveValues.size()) {

                    // 主维度列表，从维度列表，按下标累计
                    for (int i = 0; i < masterValues.size(); i++) {
                        velEventDataList.add(
                                createVelEventDataDto(ctx, slaveValues.get(i), masterValues.get(i), value));
                    }
                } else {
                    log.warn(
                            " master slave dims value illegal, txnId: {}, velocityCode: {}, masterValue: {}, slaveValue: {}",
                            ctx.getTxnId(), ctx.getConfigModel()
                                    .getFeatureCode(),
                            JSON.toJSONString(masterValues),
                            JSON.toJSONString(slaveValues));
                }
            }
        } catch (Exception e) {
            log.error("parse velocity config value error, pointCode= {}, txnId= {}, velocityCode={}, velocityValue={}",
                    ctx.getEndpointCode(), ctx.getTxnId(), ctx.getConfigModel()
                            .getFeatureCode(),
                    JSON.toJSONString(ctx.getContext()),
                    e);
        }

        return velEventDataList;
    }

    private ScriptVariable buildScriptVariable(FeatureConfigExecuteContext ctx, String script) {
        ScriptVariable variable = new ScriptVariable();
        variable.setLang(ScriptType.JEXL.getCode());
        variable.setScript(script);
        variable.setParams(ctx.getParams());
        return variable;
    }

    private VelEventData createVelEventDataDto(FeatureConfigExecuteContext ctx, String slaveValue,
                                               String masterValue, BigDecimal value) {
        VelEventData dto = new VelEventData();
        dto.setPointCode(ctx.getEndpointCode());
        dto.setTxnId(ctx.getTxnId());
        dto.setTxnTime(ctx.getTxnTime());
        dto.setFeatureCode(ctx.getConfigModel().getFeatureCode());
        dto.setMasterValue(masterValue);
        dto.setSlaveValue(slaveValue);
        dto.setVelValue(value);
        dto.setAggregateMode(ctx.getConfigModel().getAggregateMode());
        dto.setTimeMode(ctx.getConfigModel()
                .getTimeMode());
        dto.setTimeUnit(ctx.getConfigModel()
                .getTimeUnit());
        dto.setTimeWindow(ctx.getConfigModel()
                .getTimeWindow());
        dto.setExpireTime(VelocityTimeOutBuilder.getWindowExpireTime(dto));
        return dto;
    }

    @Override
    public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }
}
