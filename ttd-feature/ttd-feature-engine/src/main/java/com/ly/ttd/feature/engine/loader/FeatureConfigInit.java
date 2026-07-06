package com.ly.ttd.feature.engine.loader;

import com.ly.ttd.connector.engine.cache.ConnectorCacheConfig;
import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.cfg.ThreadPoolNames;
import com.ly.ttd.feature.cfg.connector.JdbcDataSourceParser;
import com.ly.ttd.feature.engine.cache.CacheRefreshService;
import com.ly.ttd.feature.engine.config.TtdFeatureConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.dynamictp.core.DtpRegistry;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 特征引擎初始化配置
 * <p>
 * 线程池由 dynamic-tp 管理（配置见 application.properties），
 * 通过 {@link DtpRegistry#getDtpExecutor(String)} 获取。
 * 运行时可通过 Nacos 动态调整线程池参数，无需重启。
 * <p>
 * 实现 {@link SmartInitializingSingleton}，所有初始化动作（Aware 注入、多数据源解析、
 * 线程池注册、引擎启动、缓存加载）统一在 {@link #afterSingletonsInstantiated()} 中按序执行，
 * 确保依赖方（如 {@code CacheRefreshServiceImpl}）的 {@code FeatureConfiguration}
 * 在首次使用前已被注入。
 *
 * @author yong.li
 * @since 2026/6/29 10:50
 */
@Configuration
@Slf4j
public class FeatureConfigInit implements SmartInitializingSingleton {

    @Resource
    private TtdFeatureConfig ttdFeatureConfig;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private CacheRefreshService cacheRefreshService;

    private FeatureConfiguration featureConfiguration;

    @Resource
    private JdbcDataSourceParser jdbcDataSourceParser;

    // ==================== FeatureConfiguration Bean ====================

    @Bean
    public FeatureConfiguration featureConfiguration() {
        log.info("init featureConfiguration");
        ConnectorCacheConfig cacheConfig = ConnectorCacheConfig.builder()
                .enabled(ttdFeatureConfig.isConnectorCacheEnabled())
                .defaultTtl(ttdFeatureConfig.getConnectorCacheTtl())
                .maxCacheSize(ttdFeatureConfig.getConnectorCacheMaxSize())
                .keyPrefix(ttdFeatureConfig.getConnectorCacheKeyPrefix())
                .build();

        FeatureConfiguration fc = new FeatureConfiguration(cacheConfig);
        this.featureConfiguration = fc;
        log.info("FeatureConfiguration Bean 创建完成, 等待 afterSingletonsInstantiated 统一初始化");
        return fc;
    }

    @Override
    public void afterSingletonsInstantiated() {
        // Step 1: 向所有 FeatureConfigurationAware Bean 注入配置实例
        // 此时所有单例 Bean（含 @Service）已全部创建，getBeansOfType 保证全覆盖
        Map<String, FeatureConfigurationAware> awareBeans =
                applicationContext.getBeansOfType(FeatureConfigurationAware.class);
        awareBeans.values().forEach(aware -> {
            log.info("inject FeatureConfiguration to {}", aware.getClass().getSimpleName());
            aware.setFeatureConfiguration(featureConfiguration);
        });
        log.info("已向 {} 个 FeatureConfigurationAware Bean 注入配置", awareBeans.size());

        // Step 2: 解析多数据源配置（在 Connectors 初始化之前完成注册）
        jdbcDataSourceParser.parseJdbcConfig();

        // Step 3: 注册 dynamic-tp 线程池
        registerThreadPools();

        // Step 4: 启动连接器引擎（依赖数据源已注册 & THREAD_CONNECTOR 线程池）
        featureConfiguration.startInit();

        // Step 5: 加载缓存资源（依赖 FeatureConfiguration 已注入到 CacheRefreshService）
        log.info("system auto load resource start");
        cacheRefreshService.refreshCache(ttdFeatureConfig.getProjectId());
        log.info("system auto load resource end");

        log.info("FeatureConfiguration 初始化完成（Aware注入 → 多数据源解析 → 线程池注册 → 引擎启动 → 缓存加载）");
    }

    private void registerThreadPools() {
        featureConfiguration.registerThreadPool(ThreadPoolNames.THREAD_META_FIELD,
                DtpRegistry.getDtpExecutor(ThreadPoolNames.THREAD_META_FIELD));
        featureConfiguration.registerThreadPool(ThreadPoolNames.THREAD_METRIC,
                DtpRegistry.getDtpExecutor(ThreadPoolNames.THREAD_METRIC));
        featureConfiguration.registerThreadPool(ThreadPoolNames.THREAD_FEATURE_WRITE,
                DtpRegistry.getDtpExecutor(ThreadPoolNames.THREAD_FEATURE_WRITE));
        featureConfiguration.registerThreadPool(ThreadPoolNames.THREAD_FEATURE_FEATURES,
                DtpRegistry.getDtpExecutor(ThreadPoolNames.THREAD_FEATURE_FEATURES));
        featureConfiguration.registerThreadPool(ThreadPoolNames.THREAD_LOG,
                DtpRegistry.getDtpExecutor(ThreadPoolNames.THREAD_LOG));
        featureConfiguration.registerThreadPool(ThreadPoolNames.THREAD_FACTOR_GET_VALUE,
                DtpRegistry.getDtpExecutor(ThreadPoolNames.THREAD_FACTOR_GET_VALUE));
        featureConfiguration.registerThreadPool(ThreadPoolNames.THREAD_CONNECTOR,
                DtpRegistry.getDtpExecutor(ThreadPoolNames.THREAD_CONNECTOR));
        featureConfiguration.registerThreadPool(ThreadPoolNames.THREAD_HBASE_CLIENT,
                DtpRegistry.getDtpExecutor(ThreadPoolNames.THREAD_HBASE_CLIENT));
        log.info("已注册 {} 个 dynamic-tp 线程池", 8);
    }
}
