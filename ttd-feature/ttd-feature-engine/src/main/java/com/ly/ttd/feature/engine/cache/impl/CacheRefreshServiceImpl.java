package com.ly.ttd.feature.engine.cache.impl;

import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.cfg.resource.*;
import com.ly.ttd.feature.common.model.AccessPointModel;
import com.ly.ttd.feature.common.model.factor.DerivativeFactorModel;
import com.ly.ttd.feature.common.model.factor.FactorDependencyModel;
import com.ly.ttd.feature.common.model.factor.FeatureFactorModel;
import com.ly.ttd.feature.common.model.factor.MetaFactorModel;
import com.ly.ttd.feature.common.model.meta.MetaFieldModel;
import com.ly.ttd.feature.common.model.struct.DataStructModel;
import com.ly.ttd.feature.common.model.vel.FeatureConfigModel;
import com.ly.ttd.feature.engine.cache.CacheRefreshService;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yong.li
 * @since 2026/6/22 16:09
 */
@Service
public class CacheRefreshServiceImpl implements CacheRefreshService, FeatureConfigurationAware {
    private FeatureConfiguration featureConfiguration;
    @Resource
    private AccessPointResourceLoader accessPointResourceLoader;
    @Resource
    private MetaFieldResourceLoader metaFieldResourceLoader;
    @Resource
    private FactorResourceLoader factorResourceLoader;
    @Resource
    private FeatureConfigResourceLoader featureConfigResourceLoader;
    @Resource
    private DataStructResourceLoader dataStructResourceLoader;


    @Override
    public void loadAndRefreshAccessPoint(Long projectId) {

        List<AccessPointModel> accessPointModels = accessPointResourceLoader.loadResource(projectId);
        if (CollectionUtils.isNotEmpty(accessPointModels)) {
            accessPointModels.forEach(t -> {
                if (featureConfiguration.getPointMap().containsKey(t.getCode())) {
                    AccessPointModel existed = featureConfiguration.getPointMap().get(t.getCode());
                    if (!t.getVersion().equals(existed.getVersion())) {
                        featureConfiguration.addAccessPoint(t);
                    }
                } else {
                    featureConfiguration.addAccessPoint(t);
                }
            });
        }

    }

    @Override
    public void loadAndRefreshFeatureConfig(Long projectId) {
        List<FeatureConfigModel> configModels = featureConfigResourceLoader.loadConfig(projectId);
        if (CollectionUtils.isNotEmpty(configModels)) {
            configModels.forEach(t -> {
                if (featureConfiguration.getFeatureMap().containsKey(t.getResourceKey())) {
                    FeatureConfigModel existed = featureConfiguration.getFeatureMap().get(t.getResourceKey());
                    if (!t.getVersion().equals(existed.getVersion())) {
                        featureConfiguration.addFeatureConfig(t);
                    }
                } else {
                    featureConfiguration.addFeatureConfig(t);
                }
            });
        }
    }

    @Override
    public void loadAndRefreshMetaField(Long projectId) {
        List<MetaFieldModel> metaFieldModels = metaFieldResourceLoader.loadResource(projectId);
        if (CollectionUtils.isNotEmpty(metaFieldModels)) {
            metaFieldModels.forEach(t -> {
                if (featureConfiguration.getMetaFieldMap().containsKey(t.getResourceKey())) {
                    MetaFieldModel existed = featureConfiguration.getMetaFieldMap().get(t.getResourceKey());
                    if (!t.getVersion().equals(existed.getVersion())) {
                        featureConfiguration.addMetaField(t);
                    }
                } else {
                    featureConfiguration.addMetaField(t);
                }
            });
        }
    }

    @Override
    public void loadAndRefreshFactor(Long projectId) {
        loadMetaFactor(projectId);
        loadDerivativeFactor(projectId);
        loadFeatureFactor(projectId);
    }

    @Override
    public void loadAndRefreshDataStruct(Long projectId) {
        List<DataStructModel> dataStructModels = dataStructResourceLoader.loadDataStruct(projectId);
        if (CollectionUtils.isNotEmpty(dataStructModels)) {
            dataStructModels.forEach(t -> {
                if (featureConfiguration.getDataStructMap().containsKey(t.getResourceKey())) {
                    DataStructModel existed = featureConfiguration.getDataStructMap().get(t.getResourceKey());
                    if (!t.getVersion().equals(existed.getVersion())) {
                        featureConfiguration.addDataStruct(t);
                    }
                } else {
                    featureConfiguration.addDataStruct(t);
                }
            });
        }

    }

    @Override
    public void refreshCache(Long projectId) {
        this.loadAndRefreshAccessPoint(projectId);
        this.loadAndRefreshMetaField(projectId);
        this.loadAndRefreshFeatureConfig(projectId);
        this.loadAndRefreshFactor(projectId);
        this.loadAndRefreshDataStruct(projectId);

        // 构建依赖关系
        this.loadFactorDependency(projectId);
    }


    private void loadMetaFactor(Long projectId) {
        List<MetaFactorModel> metaFactorModels = factorResourceLoader.loadMetaFactor(projectId);
        if (CollectionUtils.isNotEmpty(metaFactorModels)) {
            metaFactorModels.forEach(t -> {
                if (featureConfiguration.getMetaFactorMap().containsKey(t.getResourceKey())) {
                    MetaFactorModel existed = featureConfiguration.getMetaFactorMap().get(t.getResourceKey());
                    if (!t.getVersion().equals(existed.getVersion())) {
                        featureConfiguration.addMetaFactor(t);
                    }
                } else {
                    featureConfiguration.addMetaFactor(t);
                }
            });
        }
    }

    private void loadDerivativeFactor(Long projectId) {
        List<DerivativeFactorModel> factors = factorResourceLoader.loadDerivativeFactor(projectId);
        if (CollectionUtils.isNotEmpty(factors)) {
            factors.forEach(t -> {
                if (featureConfiguration.getDerivativeFactorMap().containsKey(t.getResourceKey())) {
                    DerivativeFactorModel existed = featureConfiguration.getDerivativeFactorMap().get(t.getResourceKey());
                    if (!t.getVersion().equals(existed.getVersion())) {
                        featureConfiguration.addDerivativeFactor(t);
                    }
                } else {
                    featureConfiguration.addDerivativeFactor(t);
                }
            });
        }
    }

    private void loadFeatureFactor(Long projectId) {
        List<FeatureFactorModel> factors = factorResourceLoader.loadFeatureFactor(projectId);
        if (CollectionUtils.isNotEmpty(factors)) {
            factors.forEach(t -> {
                if (featureConfiguration.getFeatureFactorMap().containsKey(t.getResourceKey())) {
                    FeatureFactorModel existed = featureConfiguration.getFeatureFactorMap().get(t.getResourceKey());
                    if (!t.getVersion().equals(existed.getVersion())) {
                        featureConfiguration.addFeatureFactor(t);
                    }
                } else {
                    featureConfiguration.addFeatureFactor(t);
                }
            });
        }
    }


    private void loadFactorDependency(Long projectId) {

        Set<String> derivativeFactorCodes = featureConfiguration.getDerivativeFactorMap()
                .keySet();

        if (CollectionUtils.isEmpty(derivativeFactorCodes)) {
            return;
        }
        List<FactorDependencyModel> dependencyModels = new ArrayList<>();
        for (String parent : derivativeFactorCodes) {
            List<FactorDependencyModel> factorDependencyModels = factorResourceLoader.queryDependency(projectId, parent);
            if (CollectionUtils.isNotEmpty(factorDependencyModels)) {
                dependencyModels.addAll(factorDependencyModels);
            }
        }

        if (CollectionUtils.isNotEmpty(dependencyModels)) {
            Map<String, List<FactorDependencyModel>> dependencyMap = dependencyModels.stream()
                    .collect(Collectors.groupingBy(FactorDependencyModel::getParent));

            for (DerivativeFactorModel factorModel : featureConfiguration.getDerivativeFactorMap().values()) {
                factorModel.buildRefFactorList(dependencyMap);
            }
        }

    }

    @Override
    public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }
}
