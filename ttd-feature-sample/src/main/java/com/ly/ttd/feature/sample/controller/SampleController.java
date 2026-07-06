package com.ly.ttd.feature.sample.controller;

import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.engine.cache.CacheRefreshService;
import com.ly.ttd.feature.engine.config.TtdFeatureConfig;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 示例 Controller — 演示如何获取引擎运行时状态及手动触发缓存刷新
 *
 * @author yong.li
 * @since 2026/6/29
 */
@RestController
@RequestMapping("/sample")
public class SampleController implements FeatureConfigurationAware {
    private FeatureConfiguration featureConfiguration;
    @Resource
    private CacheRefreshService cacheRefreshService;

    @Resource
    private TtdFeatureConfig ttdFeatureConfig;

    /**
     * 查看引擎缓存状态
     */
    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of(
                "projectId", ttdFeatureConfig.getProjectId(),
                "accessPoints", featureConfiguration.getPointMap().size(),
                "featureConfigs", featureConfiguration.getFeatureMap().size(),
                "metaFields", featureConfiguration.getMetaFieldMap().size(),
                "metaFactors", featureConfiguration.getMetaFactorMap().size(),
                "derivativeFactors", featureConfiguration.getDerivativeFactorMap().size(),
                "featureFactors", featureConfiguration.getFeatureFactorMap().size(),
                "dataStructs", featureConfiguration.getDataStructMap().size()
        );
    }

    /**
     * 手动触发缓存刷新
     */
    @PostMapping("/refresh")
    public String refresh() {
        cacheRefreshService.refreshCache(ttdFeatureConfig.getProjectId());
        return "cache refresh triggered";
    }

    @Override
    public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }
}
