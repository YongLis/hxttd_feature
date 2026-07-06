package com.ly.ttd.feature.sample.resource.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ly.ttd.feature.cfg.resource.FactorResourceLoader;
import com.ly.ttd.feature.common.model.factor.DerivativeFactorModel;
import com.ly.ttd.feature.common.model.factor.FactorDependencyModel;
import com.ly.ttd.feature.common.model.factor.FeatureFactorModel;
import com.ly.ttd.feature.common.model.factor.MetaFactorModel;
import com.ly.ttd.feature.sample.mybatis.entity.FactorDependencyEntity;
import com.ly.ttd.feature.sample.mybatis.entity.FactorEntity;
import com.ly.ttd.feature.sample.mybatis.mapper.FactorDependencyMapper;
import com.ly.ttd.feature.sample.mybatis.mapper.FactorMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 指标资源加载实现
 * <p>
 * 从 MySQL（ttd_factor 表）读取三类指标，按 factorType 分流转为对应的 Model：
 * <ul>
 *   <li>META → {@link MetaFactorModel}</li>
 *   <li>DERIVATIVE → {@link DerivativeFactorModel}</li>
 *   <li>FEATURE → {@link FeatureFactorModel}</li>
 * </ul>
 *
 * @author yong.li
 * @since 2026/6/29
 */
@Slf4j
@Service
public class FactorResourceLoaderImpl implements FactorResourceLoader {

    @Resource
    private FactorMapper factorMapper;
    @Resource
    private FactorDependencyMapper factorDependencyMapper;

    @Override
    public List<MetaFactorModel> loadMetaFactor(Long projectId) {
        List<FactorEntity> entities = selectByFactorType(projectId, "META");
        if (CollectionUtils.isEmpty(entities)) {
            log.info("未加载到元字段因子, projectId={}", projectId);
            return Collections.emptyList();
        }
        List<MetaFactorModel> models = entities.stream().map(this::toMetaFactor).collect(Collectors.toList());
        log.info("加载元字段因子完成, projectId={}, count={}", projectId, models.size());
        return models;
    }

    @Override
    public List<DerivativeFactorModel> loadDerivativeFactor(Long projectId) {
        List<FactorEntity> entities = selectByFactorType(projectId, "DERIVATIVE");
        if (CollectionUtils.isEmpty(entities)) {
            log.info("未加载到衍生因子, projectId={}", projectId);
            return Collections.emptyList();
        }
        List<DerivativeFactorModel> models = entities.stream().map(this::toDerivativeFactor).collect(Collectors.toList());
        log.info("加载衍生因子完成, projectId={}, count={}", projectId, models.size());
        return models;
    }

    @Override
    public List<FeatureFactorModel> loadFeatureFactor(Long projectId) {
        List<FactorEntity> entities = selectByFactorType(projectId, "FEATURE");
        if (CollectionUtils.isEmpty(entities)) {
            log.info("未加载到特征因子, projectId={}", projectId);
            return Collections.emptyList();
        }
        List<FeatureFactorModel> models = entities.stream().map(this::toFeatureFactor).collect(Collectors.toList());
        log.info("加载特征因子完成, projectId={}, count={}", projectId, models.size());
        return models;
    }

    @Override
    public List<FactorDependencyModel> queryDependency(Long projectId, String parent) {
        List<FactorDependencyEntity> result = new ArrayList<>();
        selectFactorDependency(projectId, result, List.of(parent));
        if(CollectionUtils.isEmpty(result)){
            return Collections.emptyList();
        }
        List<FactorDependencyModel> models = result.stream().map(t -> {
        FactorDependencyModel model = new FactorDependencyModel();
            model.setParent(t.getParent());
            model.setChild(t.getChild());
            return model;
        }).collect(Collectors.toList());
        log.info("查询指标依赖血缘完成, projectId={}, count={}", projectId, models.size());
        return models;
    }

    private List<FactorEntity> selectByFactorType(Long projectId, String factorType) {
        return factorMapper.selectList(new LambdaQueryWrapper<FactorEntity>()
                .eq(FactorEntity::getProjectId, projectId)
                .eq(FactorEntity::getFactorType, factorType));
    }



    private void selectFactorDependency(Long projectId,List<FactorDependencyEntity> result, List<String> parents) {
        if(CollectionUtils.isEmpty(parents)){
            return;
        }
        List<FactorDependencyEntity> entities = selectDependency(projectId, parents);
        if(CollectionUtils.isEmpty(entities)){
            return;
        }
        result.addAll(entities);
        List<String> children = entities.stream().map(FactorDependencyEntity::getChild).collect(Collectors.toList());
        selectFactorDependency(projectId, result, children);

    }

    private List<FactorDependencyEntity> selectDependency(Long projectId, List<String> parents) {
        return factorDependencyMapper.selectList(new LambdaQueryWrapper<FactorDependencyEntity>()
                .eq(FactorDependencyEntity::getProjectId, projectId)
                .in(FactorDependencyEntity::getParent, parents)
                .eq(FactorDependencyEntity::getDeleted, false));
    }


    private MetaFactorModel toMetaFactor(FactorEntity entity) {
        MetaFactorModel model = new MetaFactorModel();
        fillBaseFields(model, entity);
        model.setMetaFieldCode(entity.getRefFeatureCode());
        return model;
    }

    private DerivativeFactorModel toDerivativeFactor(FactorEntity entity) {
        DerivativeFactorModel model = new DerivativeFactorModel();
        fillBaseFields(model, entity);
        parseDerivativeResourceJson(entity.getResourceJson(), model);
        return model;
    }

    private FeatureFactorModel toFeatureFactor(FactorEntity entity) {
        FeatureFactorModel model = new FeatureFactorModel();
        fillBaseFields(model, entity);
        model.setRefVelocityCode(entity.getRefFeatureCode());
        return model;
    }

    private void fillBaseFields(com.ly.ttd.feature.common.model.factor.FactorModel model, FactorEntity entity) {
        model.setResourceKey(entity.getResourceKey());
        model.setResourceName(entity.getResourceName());
        model.setVersion(entity.getVersion());
        model.setProjectId(entity.getProjectId());
        model.setFactorType(entity.getFactorType());
        model.setReturnType(entity.getReturnType());
        model.setDefaultValue(entity.getDefaultValue());
        model.setExceptionValue(entity.getExceptionValue());
        model.setTimeout(entity.getTimeout());
    }

    private void parseDerivativeResourceJson(String resourceJson, DerivativeFactorModel model) {
        if (resourceJson == null || resourceJson.isBlank()) {
            return;
        }
        try {
            com.alibaba.fastjson.JSONObject obj = JSON.parseObject(resourceJson);
            if (obj != null) {
                model.setLanguage(obj.getString("language"));
                model.setConditionScript(obj.getString("conditionScript"));
                model.setScript(obj.getString("script"));
                model.setConnectorType(obj.getString("connectorType"));
                model.setConnectorCode(obj.getString("connectorCode"));
            }
        } catch (Exception e) {
            log.warn("解析衍生因子 resourceJson 失败, resourceKey={}", model.getResourceKey(), e);
        }
    }
}
