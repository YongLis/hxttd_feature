package com.ly.ttd.feature.engine.loader;

import com.ly.ttd.feature.engine.cache.CacheRefreshService;
import com.ly.ttd.feature.engine.config.TtdFeatureConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 资源加载触发器（由 {@link FeatureConfigInit#afterSingletonsInstantiated()} 驱动）
 * <p>
 * 不再实现 {@code InitializingBean}，改为由 FeatureConfigInit 在
 * {@code afterSingletonsInstantiated()} 中按序调用，确保 FeatureConfiguration
 * 已通过 FeatureConfigurationAware 回调注入到 CacheRefreshService 之后再触发加载。
 *
 * @author yong.li
 * @since 2026/6/22 14:57
 */
@Slf4j
@Component
public class ResourceLoader {

    @Resource
    private CacheRefreshService cacheRefreshService;
    @Resource
    private TtdFeatureConfig ttdFeatureConfig;

    public void triggerLoad() {
        log.info("system auto load resource start");
        cacheRefreshService.refreshCache(ttdFeatureConfig.getProjectId());
        log.info("system auto load resource end");
    }
}
