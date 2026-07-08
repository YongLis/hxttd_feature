package com.ly.ttd.feature.engine.impl;

import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.cfg.ThreadPoolNames;
import com.ly.ttd.feature.common.ctx.TxnParamContext;
import com.ly.ttd.feature.common.enums.RunModeEnum;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.common.model.AccessPointModel;
import com.ly.ttd.feature.common.model.struct.DataStructModel;
import com.ly.ttd.feature.common.model.struct.FieldModel;
import com.ly.ttd.feature.engine.FeatureEngineService;
import com.ly.ttd.feature.engine.config.TtdFeatureConfig;
import com.ly.ttd.feature.engine.req.ModelQueryReq;
import com.ly.ttd.feature.engine.req.SingleQueryReq;
import com.ly.ttd.feature.engine.req.TxnFeatureReq;
import com.ly.ttd.feature.engine.res.ExecuteResult;
import com.ly.ttd.feature.request.TxnFeatureRequest;
import com.ly.ttd.feature.srv.FactorGetValueService;
import com.ly.ttd.feature.srv.TxnFeatureService;
import com.ly.ttd.feature.srv.factor.velocity.VelocityAdminService;
import com.ly.ttd.feature.srv.vel.compile.FieldCodeValue;
import com.ly.ttd.utils.Md5Util;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 特征查询服务
 *
 * @author yong.li
 * @since 2026/6/12 14:31
 */
@Slf4j
@Service
public class FeatureEngineServiceImpl implements FeatureEngineService, FeatureConfigurationAware {

    private FeatureConfiguration featureConfiguration;

    @Resource
    private FactorGetValueService factorGetValueService;
    @Resource
    private TxnFeatureService txnFeatureService;
    @Resource
    private VelocityAdminService velocityAdminService;
    @Resource
    private TtdFeatureConfig ttdFeatureConfig;

    @Override
    public void asyncWrite(@Validated TxnFeatureReq req) throws FeatureBizException {
        CompletableFuture.runAsync(() -> {
            try {
                TxnParamContext ctx = new TxnParamContext();
                ctx.setProjectId(ttdFeatureConfig.getProjectId());
                ctx.setTxnId(req.getTxnId());
                ctx.setPointCode(req.getPointCode());
                ctx.setBizCode(req.getBizCode());
                ctx.setTxnTime(req.getTxnTime());
                ctx.setTraceId(req.getTraceId());
                ctx.setReq(req.getReq());

                ctx.setRunMode(req.getRunMode());
                ctx.setTraceId(req.getTraceId());

                List<VelEventData> eventDataDtos = velocityAdminService.filterVelAndBuildEventData(ctx);
                TxnFeatureRequest velocityRequest = new TxnFeatureRequest();
                velocityRequest.setPointCode(req.getPointCode());
                velocityRequest.setTxnId(req.getTxnId());
                velocityRequest.setBizCode(req.getBizCode());
                velocityRequest.setTxnTime(req.getTxnTime());
                velocityRequest.setEventDataList(eventDataDtos);
                txnFeatureService.write(velocityRequest);
            } catch (Exception e) {
                log.error("feature write error, txnId={}", req.getTxnId());
            }
        }, featureConfiguration.getThreadPool(ThreadPoolNames.THREAD_FEATURE_WRITE));
    }


    private void validParam(TxnFeatureReq req) throws FeatureBizException {
        String pointCode = req.getPointCode();
        if (StringUtils.isEmpty(req.getRunMode())) {
            req.setRunMode(RunModeEnum.TEST.getCode());
        }

        if (StringUtils.isEmpty(req.getTraceId())) {
            req.setTraceId(Md5Util.MD5(UUID.randomUUID().toString()));
        }

        AccessPointModel accessPointModel = featureConfiguration.getPointMap().get(pointCode);
        if (null == accessPointModel) {
            throw new FeatureBizException("01", "接入点不存在");
        }
    }

    @Override
    public ExecuteResult<Object> getSingleValue(SingleQueryReq req) {
        try {
            validParam(req);
            Object value = factorGetValueService.getValue(req.getFactorCode(), buildTxnParamContent(req));
            return ExecuteResult.success(value);
        } catch (FeatureBizException e) {
            log.error("查询指指标失败", e);
            return ExecuteResult.fail(e.getMessage());
        }

    }

    private TxnParamContext buildTxnParamContent(TxnFeatureReq req) throws FeatureBizException {
        AccessPointModel point = featureConfiguration.getPointMap().get(req.getPointCode());
        if (null == point) {
            throw new FeatureBizException("01", "接入点不存在");
        }
        TxnParamContext ctx = new TxnParamContext();
        ctx.setProjectId(point.getProjectId());
        ctx.setTxnId(req.getTxnId());
        ctx.setPointCode(req.getPointCode());
        ctx.setBizCode(req.getBizCode());
        ctx.setTxnTime(req.getTxnTime());
        ctx.setReq(req.getReq());
        ctx.setRunMode(req.getRunMode());
        return ctx;
    }

    @Override
    public ExecuteResult<Map<String, Object>> getModelValue(ModelQueryReq req) {

        try {
            validParam(req);
            if (StringUtils.isEmpty(req.getModelCode())) {
                throw new FeatureBizException("01", "modelCode不允许为空");
            }
            AccessPointModel point = featureConfiguration.getPointMap().get(req.getPointCode());
            if (null == point) {
                throw new FeatureBizException("01", "接入点不存在");
            }

            if (!point.getSubModels().contains(req.getModelCode())) {
                throw new FeatureBizException("01", "模型不存在");
            }

            DataStructModel dataStructModel = featureConfiguration.getDataStructMap().get(req.getModelCode());
            if (null == dataStructModel) {
                throw new FeatureBizException("01", "模型不存在");
            }


            TxnParamContext ctx = buildTxnParamContent(req);

            List<CompletableFuture<FieldCodeValue>> list = new ArrayList<>();
            dataStructModel.getFieldModels()
                    .forEach(t -> {
                        list.add(getFactorValue(t.getFactorCode(), ctx));
                    });

            Map<String, String> fieldCodeMap = dataStructModel.getFieldModels()
                    .stream().collect(Collectors.toMap(FieldModel::getFactorCode, FieldModel::getFieldCode));
            CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();
            Map<String, Object> valueMap = new HashMap<>();
            list.forEach(t -> {
                FieldCodeValue fieldCodeValue = t.join();
                String fieldCode = fieldCodeMap.get(fieldCodeValue.getCode());
                valueMap.put(fieldCode, fieldCodeValue.getValue());
            });

            return ExecuteResult.success(valueMap);
        } catch (FeatureBizException e) {
            return ExecuteResult.failReturnEmpty(e.getMessage());
        } catch (Exception e) {
            return ExecuteResult.failReturnEmpty("查询模型指标失败");
        }

    }

    private CompletableFuture<FieldCodeValue> getFactorValue(String factorCode, TxnParamContext ctx) {
        return CompletableFuture.supplyAsync(() -> parseFactorValueDto(factorCode, ctx),
                featureConfiguration.getThreadPool(ThreadPoolNames.THREAD_FACTOR_GET_VALUE)
        );
    }


    private FieldCodeValue parseFactorValueDto(String factorCode, TxnParamContext ctx) {
        FieldCodeValue fieldCodeValue = new FieldCodeValue();
        fieldCodeValue.setCode(factorCode);
        fieldCodeValue.setValue(factorGetValueService.getValue(factorCode, ctx));
        return fieldCodeValue;
    }

    @Override
    public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }
}
