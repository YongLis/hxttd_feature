package com.ly.ttd.feature.cfg;

import com.ly.ttd.connector.api.AbstractConnector;
import com.ly.ttd.connector.api.spi.ConnectorInterceptor;
import com.ly.ttd.connector.api.spi.ConnectorProvider;
import com.ly.ttd.connector.engine.ConnectorEngine;
import com.ly.ttd.connector.engine.cache.ConnectorCache;
import com.ly.ttd.connector.engine.cache.ConnectorCacheConfig;
import com.ly.ttd.feature.cfg.config.ElasticsearchConfig;
import com.ly.ttd.feature.cfg.config.HbaseConfig;
import com.ly.ttd.feature.cfg.config.HttpConfig;
import com.ly.ttd.feature.cfg.config.JdbcConfig;
import com.ly.ttd.feature.cfg.connector.Connectors;
import com.ly.ttd.feature.common.model.AccessPointModel;
import com.ly.ttd.feature.common.model.connector.ConnectorModel;
import com.ly.ttd.feature.common.model.factor.DerivativeFactorModel;
import com.ly.ttd.feature.common.model.factor.FeatureFactorModel;
import com.ly.ttd.feature.common.model.factor.MetaFactorModel;
import com.ly.ttd.feature.common.model.meta.MetaFieldModel;
import com.ly.ttd.feature.common.model.struct.DataStructModel;
import com.ly.ttd.feature.common.model.vel.FeatureConfigModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author yong.li
 * @since 2026/6/12 22:26
 */
@Slf4j
public class FeatureConfiguration {

    private ConnectorCacheConfig connectorCacheConfig;

    // 元字段
    private Map<String, MetaFieldModel> metaFieldMap = new ConcurrentHashMap<>();
    // 接入点元字段缓存
    private Map<String, List<String>> pointCodeMetaFieldMap = new ConcurrentHashMap<>();


    // 元字段指标
    private Map<String, MetaFactorModel> metaFactorMap = new ConcurrentHashMap<>();

    // 派生字段指标
    private Map<String, DerivativeFactorModel> derivativeFactorMap = new ConcurrentHashMap<>();

    // 特征指标
    private Map<String, FeatureFactorModel> featureFactorMap = new ConcurrentHashMap<>();

    // 特征配置
    private Map<String, FeatureConfigModel> featureMap = new ConcurrentHashMap<>();

    // 项目下特征配置
    private Map<Long, List<FeatureConfigModel>> projectFeatureMap = new ConcurrentHashMap<>();


    // 线程池缓存
    private Map<String, ThreadPoolExecutor> threadPoolExecutorMap = new ConcurrentHashMap<>();


    // 接入点缓存
    private Map<String, AccessPointModel> pointMap = new ConcurrentHashMap<>();

    // 数据集缓存
    private Map<String, DataStructModel> dataStructMap = new ConcurrentHashMap<>();

    // 连接缓存
    private Map<String, ConnectorModel> connectorMap = new ConcurrentHashMap<>();


    private Connectors connectors;


    /**
     * 数据源配置
     */
    private Map<String, JdbcConfig> jdbcConfigMap = new ConcurrentHashMap<>();

    /**
     * Elasticsearch数据源
     */
    private Map<String, ElasticsearchConfig> elasticsearchConfigMap = new ConcurrentHashMap<>();

    /**
     * Hbase
     */
    private HbaseConfig hbaseConfig;
    /**
     * Http数据源
     */
    private Map<String, HttpConfig> httpConfigMap = new ConcurrentHashMap<>();

    /**
     * 连接引擎
     */
    private ConnectorEngine connectorEngine;


    public FeatureConfiguration(ConnectorCacheConfig connectorCacheConfig) {
        this.connectorCacheConfig = connectorCacheConfig;
    }


    public void startInit() {
        log.info(" feature start init");

        connectors = new Connectors(this);

        initConnectorEngine();
    }

    private void initConnectorEngine() {
        connectorEngine = new ConnectorEngine();
        if (connectorCacheConfig.isEnabled()) {
            connectorEngine.setCache(new ConnectorCache(connectorCacheConfig));
        } else {
            log.warn("Connector not use cache");
        }
        ThreadPoolExecutor executor = getThreadPool(ThreadPoolNames.THREAD_CONNECTOR);
        connectorEngine.setDefaultExecutor(executor);


        // 初始化interceptors
        ServiceLoader<ConnectorInterceptor> interceptorLoader = ServiceLoader.load(ConnectorInterceptor.class);
        List<ConnectorInterceptor> interceptors = new ArrayList<>();
        for (ConnectorInterceptor interceptor : interceptorLoader) {
            interceptors.add(interceptor);
        }
        ServiceLoader<ConnectorProvider> loader = ServiceLoader.load(ConnectorProvider.class);
        for (ConnectorProvider provider : loader) {
            log.info("connector {} register", provider.getConnectorType());
            AbstractConnector abstractConnector = (AbstractConnector) provider.createConnector();
            if (CollectionUtils.isNotEmpty(interceptors)) {
                abstractConnector.addInterceptors(interceptors);
            }
            connectorEngine.registerConnector(provider.getConnectorType(), abstractConnector);
        }
    }


    /**
     * 添加元字段
     */
    public void addMetaField(MetaFieldModel metaField) {
        metaFieldMap.put(metaField.getResourceKey(), metaField);
    }

    /**
     * 添加元字段指标
     */
    public void addMetaFactor(MetaFactorModel metaFactor) {
        metaFactorMap.put(metaFactor.getResourceKey(), metaFactor);
    }

    /**
     * 添加派生字段指标
     */
    public void addDerivativeFactor(DerivativeFactorModel derivativeFactor) {
        derivativeFactorMap.put(derivativeFactor.getResourceKey(), derivativeFactor);
    }

    /**
     * 添加特征指标
     */
    public void addFeatureFactor(FeatureFactorModel featureFactor) {
        featureFactorMap.put(featureFactor.getResourceKey(), featureFactor);
    }

    /**
     * 添加特征配置
     */
    public void addFeatureConfig(FeatureConfigModel featureConfig) {
        featureMap.put(featureConfig.getFeatureCode(), featureConfig);
    }

    /**
     * 添加项目下特征配置
     */
    public void refreshProjectFeature() {
        Map<Long, List<FeatureConfigModel>> tmp =
                featureMap.values().stream()
                        .collect(Collectors.groupingBy(FeatureConfigModel::getProjectId));
        projectFeatureMap.putAll(tmp);
    }

    /**
     * 添加接入点元字段缓存
     */
    public void refreshPointCodeMetaField() {
        Map<String, List<String>> tmp = metaFieldMap.values()
                .stream()
                .collect(Collectors.groupingBy(MetaFieldModel::getPointCode, Collectors.mapping(MetaFieldModel::getResourceKey, Collectors.toList())));
        pointCodeMetaFieldMap.putAll(tmp);
    }

    /**
     * 添加接入点
     */
    public void addAccessPoint(AccessPointModel accessPointModel) {
        pointMap.put(accessPointModel.getCode(), accessPointModel);
    }

    /**
     * 添加数据集
     */
    public void addDataStruct(DataStructModel dataStructModel) {
        dataStructMap.put(dataStructModel.getResourceKey(), dataStructModel);
    }

    /**
     * 添加连接器定义
     */
    public void addConnector(ConnectorModel connectorModel) {
        connectorMap.put(connectorModel.getResourceKey(), connectorModel);
    }

    /**
     * 添加Elasticsearch数据源配置
     */
    public void addElasticsearchConfig(ElasticsearchConfig config) {
        elasticsearchConfigMap.put(config.getSourceName(), config);
    }

    /**
     * 添加JDBC数据源配置
     */
    public void addJdbcConfig(JdbcConfig config) {
        jdbcConfigMap.put(config.getSourceName(), config);
    }

    /**
     * 添加Http数据源配置
     */
    public void addHttpConfig(HttpConfig config) {
        httpConfigMap.put(config.getSourceName(), config);
    }

    public void registerThreadPool(String threadPoolName, ThreadPoolExecutor threadPoolExecutor) {
        threadPoolExecutorMap.put(threadPoolName, threadPoolExecutor);
    }

    public ThreadPoolExecutor getThreadPool(String threadPoolName) {
        return threadPoolExecutorMap.get(threadPoolName);
    }

    public ConnectorEngine getConnectorEngine() {
        return connectorEngine;
    }

    public ConnectorCacheConfig getConnectorCacheConfig() {
        return connectorCacheConfig;
    }

    public Map<String, MetaFieldModel> getMetaFieldMap() {
        return metaFieldMap;
    }

    public Map<String, List<String>> getPointCodeMetaFieldMap() {
        return pointCodeMetaFieldMap;
    }

    public Map<String, MetaFactorModel> getMetaFactorMap() {
        return metaFactorMap;
    }

    public Map<String, DerivativeFactorModel> getDerivativeFactorMap() {
        return derivativeFactorMap;
    }

    public Map<String, FeatureFactorModel> getFeatureFactorMap() {
        return featureFactorMap;
    }

    public Map<String, FeatureConfigModel> getFeatureMap() {
        return featureMap;
    }

    public Map<Long, List<FeatureConfigModel>> getProjectFeatureMap() {
        return projectFeatureMap;
    }

    public Map<String, AccessPointModel> getPointMap() {
        return pointMap;
    }

    public Map<String, DataStructModel> getDataStructMap() {
        return dataStructMap;
    }

    public Map<String, ConnectorModel> getConnectorMap() {
        return connectorMap;
    }

    public Map<String, JdbcConfig> getJdbcConfigMap() {
        return jdbcConfigMap;
    }

    public Map<String, ElasticsearchConfig> getElasticsearchConfigMap() {
        return elasticsearchConfigMap;
    }


    public HbaseConfig getHbaseConfig() {
        return hbaseConfig;
    }

    public Map<String, HttpConfig> getHttpConfigMap() {
        return httpConfigMap;
    }

    public Map<String, ThreadPoolExecutor> getThreadPoolExecutorMap() {
        return threadPoolExecutorMap;
    }

    public Connectors getConnectors() {
        return connectors;
    }
}
