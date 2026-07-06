package com.ly.ttd.feature.engine.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 特征引擎统一配置项
 * <p>
 * 所有配置项通过 Nacos 热加载（group=feature_config, dataId=engine_core.properties），
 * 通过 @Value 注入并设定默认值。
 *
 * @author yong.li
 * @since 2026/6/27 00:06
 */
@Component
@Getter
public class TtdFeatureConfig {

    // ======================== 项目配置 ========================
    @Value("${ttd.feature.project.id}")
    private Long projectId;

    // ======================== 指标库数据源 ========================
    @Value("${ttd.db.feature.datasource.url}")
    private String dbFeatureDataSourceUrl;
    @Value("${ttd.db.feature.datasource.username}")
    private String dbFeatureDataSourceUsername;
    @Value("${ttd.db.feature.datasource.password}")
    private String dbFeatureDataSourcePassword;
    @Value("${ttd.db.feature.datasource.driver}")
    private String dbFeatureDataSourceDriver;

    // ======================== 连接器缓存 ========================
    @Value("${ttd.connector.cache.enabled}")
    private boolean connectorCacheEnabled;
    @Value("${ttd.connector.cache.ttl}")
    private long connectorCacheTtl;
    @Value("${ttd.connector.cache.max-size}")
    private int connectorCacheMaxSize;
    @Value("${ttd.connector.cache.key-prefix}")
    private String connectorCacheKeyPrefix;
    /**
     * 连接器慢查询告警阈值（毫秒），超过此阈值打印warn日志
     */
    @Value("${ttd.connector.slow-query-threshold}")
    private long connectorSlowQueryThreshold;

    /**
     * 线程池通用线程存活时间（秒）
     */
    @Value("${ttd.pool.keep-alive-seconds:60000}")
    private long poolKeepAliveSeconds;


    // ======================== 特征引擎运行时 ========================
    /**
     * 特征执行默认超时（毫秒）
     */
    @Value("${ttd.feature.execution-timeout:100}")
    private int featureExecutionTimeout;
    /**
     * 模型内因子并发获取超时（毫秒）
     */
    @Value("${ttd.feature.factor-get-timeout:100}")
    private int factorGetTimeout;
    /**
     * 缓存首次刷新延迟（毫秒）
     */
    @Value("${ttd.feature.cache-refresh.initial-delay:60000}")
    private long cacheRefreshInitialDelay;
    /**
     * 缓存刷新间隔（毫秒）
     */
    @Value("${ttd.feature.cache-refresh.fixed-rate:120000}")
    private long cacheRefreshFixedRate;


    // ======================== JDBC连接池默认值 ========================
    @Value("${ttd.jdbc.initial-size:10}")
    private int jdbcInitialSize;
    @Value("${ttd.jdbc.min-idle:10}")
    private int jdbcMinIdle;
    @Value("${ttd.jdbc.max-active:100}")
    private int jdbcMaxActive;
    @Value("${ttd.jdbc.max-wait:60000}")
    private int jdbcMaxWait;
    @Value("${ttd.jdbc.query-timeout:10000}")
    private int jdbcQueryTimeout;

    // ======================== ES默认值 ========================
    @Value("${ttd.es.connect-timeout:10000}")
    private int esConnectTimeout;
    @Value("${ttd.es.socket-timeout:10000}")
    private int esSocketTimeout;

    // ======================== HBase默认值 ========================
    @Value("${ttd.hbase.rpc-timeout:10000}")
    private int hbaseRpcTimeout;
    @Value("${ttd.hbase.operation-timeout:10000}")
    private int hbaseOperationTimeout;
    @Value("${ttd.hbase.scanner-caching:100000}")
    private int hbaseScannerCaching;
    @Value("${ttd.hbase.client-retries:3}")
    private int hbaseClientRetriesNumber;
    @Value("${ttd.hbase.scanner-timeout:10000}")
    private int hbaseScannerTimeoutPeriod;

    // ======================== HTTP连接器默认值 ========================
    @Value("${ttd.http.connect-timeout:10000}")
    private int httpConnectTimeout;
    @Value("${ttd.http.read-timeout:10000}")
    private int httpReadTimeout;
}
