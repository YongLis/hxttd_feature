package com.ly.ttd.feature.engine.loader;

import com.ly.ttd.feature.engine.cache.CacheRefreshService;
import com.ly.ttd.feature.engine.config.TtdFeatureConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author yong.li
 * @since 2026/6/22 16:23
 */
@Component
@Slf4j
public class ResourceDelayLoadTask {
    @Resource
    private CacheRefreshService cacheRefreshService;
    @Resource
    private TtdFeatureConfig ttdFeatureConfig;

    @Scheduled(initialDelay = 60000, fixedRate = 2 * 60000)
    public void load() {
        log.info("system delay load resource start");
        cacheRefreshService.refreshCache(ttdFeatureConfig.getProjectId());
        log.info("system delay load resource end");
    }

}
