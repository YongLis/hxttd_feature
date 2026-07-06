package com.ly.ttd.feature.sample.resource.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ly.ttd.feature.cfg.resource.FeatureConfigResourceLoader;
import com.ly.ttd.feature.common.model.vel.FeatureConfigModel;
import com.ly.ttd.feature.sample.mybatis.entity.FeatureConfigEntity;
import com.ly.ttd.feature.sample.mybatis.mapper.FeatureConfigMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 特征配置资源加载实现
 * <p>
 * 从 MySQL（ttd_feature_config 表）读取特征配置，转换为引擎用的 {@link FeatureConfigModel}。
 * metaFields 字段从 JSON 字符串反序列化为 {@code List<String>}。
 *
 * @author yong.li
 * @since 2026/6/29
 */
@Slf4j
@Service
public class FeatureConfigResourceLoaderImpl implements FeatureConfigResourceLoader {

    @Resource
    private FeatureConfigMapper featureConfigMapper;

    @Override
    public List<FeatureConfigModel> loadConfig(Long projectId) {
        List<FeatureConfigEntity> entities = featureConfigMapper.selectList(
                new LambdaQueryWrapper<FeatureConfigEntity>()
                        .eq(FeatureConfigEntity::getProjectId, projectId));
        if (CollectionUtils.isEmpty(entities)) {
            log.info("未加载到特征配置资源, projectId={}", projectId);
            return Collections.emptyList();
        }
        List<FeatureConfigModel> models = entities.stream().map(this::convert).collect(Collectors.toList());
        log.info("加载特征配置资源完成, projectId={}, count={}", projectId, models.size());
        return models;
    }

    private FeatureConfigModel convert(FeatureConfigEntity entity) {
        FeatureConfigModel model = new FeatureConfigModel();
        model.setResourceKey(entity.getResourceKey());
        model.setResourceName(entity.getResourceName());
        model.setVersion(entity.getVersion());
        model.setProjectId(entity.getProjectId());
        model.setFeatureCode(entity.getFeatureCode());
        model.setDefaultValue(entity.getDefaultValue());
        model.setExceptionValue(entity.getExceptionValue());
        model.setTimeout(entity.getTimeout());
        model.setMainDimension(entity.getMainDimension());
        model.setSlaveDimension(entity.getSlaveDimension());
        model.setLanguage(entity.getLanguage());
        model.setConditionScript(entity.getConditionScript());
        model.setMainDimScript(entity.getMainDimScript());
        model.setSlaveDimScript(entity.getSlaveDimScript());
        model.setReturnType(entity.getReturnType());
        model.setValueType(entity.getValueType());
        model.setValueScript(entity.getValueScript());
        model.setFixValue(entity.getFixValue());
        model.setAggregateMode(entity.getAggregateMode());
        model.setTimeMode(entity.getTimeMode());
        model.setTimeUnit(entity.getTimeUnit());
        model.setTimeWindow(entity.getTimeWindow());
        if (StringUtils.isNotBlank(entity.getMetaFields())) {
            model.setMetaFields(Arrays.asList(entity.getMetaFields().split(",")));
        }
        return model;
    }
}
