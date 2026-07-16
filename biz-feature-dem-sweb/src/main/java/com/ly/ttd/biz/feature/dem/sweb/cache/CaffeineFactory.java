package com.ly.ttd.biz.feature.dem.sweb.cache;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yong.li
 * @since 2026/4/2 15:46
 */
@Slf4j
public class CaffeineFactory {
    public static Cache<String, Object> getInstance(ThreadPoolExecutor executor, long duration, long maxSize) {
        log.info("create caffeine cache instance, executor: {}, duration: {}, maxSize: {}", executor, duration, maxSize);
        Cache<String, Object> caffeine = Caffeine.newBuilder()
                .executor(executor)
                .maximumSize(maxSize)
                .expireAfterWrite(duration, TimeUnit.MINUTES)
                .recordStats()
                .removalListener((k, v, c) -> {
                    log.info(" cache remove key: {}, {}, {}", k, JSON.toJSONString(v), JSON.toJSONString(c));
                })
                .build();
        return caffeine;
    }


}
