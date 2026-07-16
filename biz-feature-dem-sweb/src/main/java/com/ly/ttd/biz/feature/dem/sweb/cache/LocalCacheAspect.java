package com.ly.ttd.biz.feature.dem.sweb.cache;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存切面
 * 拦截带有@LocalCache注解的方法，实现缓存逻辑
 *
 * @author yong.li
 * @since 2026/4/30 14:30
 */
@Component
@Aspect
@Slf4j
public class LocalCacheAspect {

    /**
     * 创建Caffeine缓存实例
     * 最大容量1000，过期时间10分钟
     */
    private final Cache<String, Object> cache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .recordStats()
            .removalListener((key, value, cause) -> {
                log.info("Cache removed key: {}, cause: {}", key, cause);
            })
            .build();

    @Around("@annotation(com.ttd.biz.srv.cache.LocalCache)")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 生成缓存key：方法签名 + 参数的JSON hash值
        String methodName = proceedingJoinPoint.getSignature().toShortString();
        Object[] args = proceedingJoinPoint.getArgs();
        String argsJson = JSON.toJSONString(args);
        int argsHash = Arrays.hashCode(args);
        String cacheKey = methodName + ":" + argsJson + ":" + argsHash;

        log.info("Checking cache for key: {}", cacheKey);

        try {
            // 1. 先从缓存获取数据
            Object cachedResult = cache.getIfPresent(cacheKey);
            if (cachedResult != null) {
                log.info("Cache hit for key: {}", cacheKey);
                return cachedResult;
            }

            log.info("Cache miss for key: {}, executing method...", cacheKey);

            // 2. 缓存不存在，执行方法查询数据库
            Object result = proceedingJoinPoint.proceed();

            // 3. 将查询结果存入缓存
            if (result != null) {
                cache.put(cacheKey, result);
                log.info("Cached result for key: {}", cacheKey);
            }

            return result;
        } catch (Throwable t) {
            log.error("Error executing method for cache key: {}, error: ", cacheKey, t);
            throw t;
        }
    }
}
