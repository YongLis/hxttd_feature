package com.ly.ttd.feature.cfg.connector;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.SimpleJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.alibaba.druid.pool.DruidDataSource;
import com.ly.ttd.feature.cfg.ConnectionCreateFilter;
import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.ThreadPoolNames;
import com.ly.ttd.feature.cfg.config.ElasticsearchConfig;
import com.ly.ttd.feature.cfg.config.JdbcConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yong.li
 * @since 2026/6/22 09:52
 */
@Slf4j
public class Connectors implements DisposableBean {
    private FeatureConfiguration configuration;

    private Map<String, DruidDataSource> druidDataSourceMap = new ConcurrentHashMap<>();
    private Map<String, NamedParameterJdbcTemplate> jdbcTemplateMap = new ConcurrentHashMap<>();

    // es 客户端
    private Map<String, ElasticsearchClient> elasticsearchClientMap = new ConcurrentHashMap<>();

    // hbase 客户端
    private Connection hbaseClient;

    public Connectors(FeatureConfiguration configuration) {
        log.info("Connectors init");
        this.configuration = configuration;

        initJdbc();
        initEsClient();

        initHbaseClient();
    }


    private void initJdbc() {
        log.info("init jdbc start");
        for (JdbcConfig config : configuration.getJdbcConfigMap().values()) {
            DruidDataSource dataSource = createDataSource(config.getSourceName());
            druidDataSourceMap.put(config.getSourceName(), dataSource);
            if (!jdbcTemplateMap.containsKey(config.getSourceName())) {
                createJdbcTemplate(config.getSourceName());
            }
        }
    }


    private void initEsClient() {
        log.info("init es client start");
        for (ElasticsearchConfig config : configuration.getElasticsearchConfigMap().values()) {
            RestClient restClient = buildEsRestClient(config);
            ElasticsearchTransport transport = new RestClientTransport(restClient, new SimpleJsonpMapper());
            ElasticsearchClient client = new ElasticsearchClient(transport);
            elasticsearchClientMap.put(config.getSourceName(), client);
        }
    }

    private RestClient buildEsRestClient(ElasticsearchConfig config) {
        // 1. 配置节点地址
        HttpHost[] hosts = new HttpHost[]{
                new HttpHost(config.getHost(), config.getPort(), "https") // 使用 HTTPS
        };

        // 2. 配置认证（如用户名密码）
        BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(config.getUsername(), config.getPassword())
        );

        // 3. 构建 RestClient
        RestClientBuilder builder = RestClient.builder(hosts)
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        // 关键：配置连接池参数，提升查询效率
                        return httpClientBuilder
                                .setDefaultCredentialsProvider(credsProvider)
                                .setMaxConnTotal(200)          // 全局最大连接数
                                .setMaxConnPerRoute(50);       // 单节点最大连接数
                    }
                });

        return builder.build();

    }


    private void initHbaseClient() {
        log.info("init hbase client start");

        ThreadPoolExecutor executor = configuration.getThreadPool(ThreadPoolNames.THREAD_HBASE_CLIENT);

        if (null == executor) {
            throw new RuntimeException("hbase client thread pool required");
        }

        Configuration conf = HBaseConfiguration.create();
        Map<String, Object> configMap = configuration.getHbaseConfig().buildConfigMap();
        for (String key : configMap.keySet()) {
            conf.set(key, (String) configMap.get(key));
        }
        try {
            hbaseClient = ConnectionFactory.createConnection(conf, executor);
        } catch (Exception e) {
            log.error("init hbase client error");
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 重建数据源和JdbcTemplate
     */
    public void createJdbcTemplate(String dataSourceName) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(druidDataSourceMap.get(dataSourceName));
        jdbcTemplateMap.put(dataSourceName, jdbcTemplate);
    }

    private DruidDataSource createDataSource(String dataSourceName) {
        JdbcConfig config = configuration.getJdbcConfigMap().get(dataSourceName);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(config.getUrl());
        dataSource.setUsername(config.getUserName());
        dataSource.setPassword(config.getPassword());
        if (config.getDriverName() != null && !config.getDriverName().isEmpty()) {
            dataSource.setDriverClassName(config.getDriverName());
        }
        // 连接池参数
        dataSource.setInitialSize(config.getInitialSize());
        dataSource.setMaxActive(config.getMaxActive());
        dataSource.setMinIdle(config.getMinIdle());
        dataSource.setMaxWait(config.getMaxWait());

        // 连接泄漏检测
        dataSource.setRemoveAbandoned(config.isRemoveAbandoned());
        dataSource.setRemoveAbandonedTimeoutMillis(config.getRemoveAbandonedTimeoutMillis());

        // 连接有效性检测
        dataSource.setTimeBetweenEvictionRunsMillis(config.getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(config.getMinEvictableIdleTimeMillis());
        dataSource.setKeepAlive(config.isKeepAlive());
        dataSource.setTestOnBorrow(config.isTestOnBorrow());
        dataSource.setTestOnReturn(config.isTestOnReturn());
        dataSource.setTestWhileIdle(config.isTestWhileIdle());
        if (config.getValidationQuery() != null && !config.getValidationQuery().isEmpty()) {
            dataSource.setValidationQuery(config.getValidationQuery());
        }

        // PSCache
        dataSource.setPoolPreparedStatements(config.isPoolPreparedStatements());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(config.getMaxPoolPreparedStatementPerConnectionSize());

        // 增加拦截器
        dataSource.setProxyFilters(Arrays.asList(new ConnectionCreateFilter(dataSourceName,
                consumer -> createJdbcTemplate(dataSourceName))));
        return dataSource;
    }


    @Override
    public void destroy() throws Exception {
// 销毁时自动注销所有jdbc连接
        if (!druidDataSourceMap.isEmpty()) {
            druidDataSourceMap.forEach((keyName, druidDataSource) -> {
                druidDataSource.close();
            });
        }
    }

    public Connection getHbaseClient() {
        return hbaseClient;
    }

    public Map<String, DruidDataSource> getDruidDataSourceMap() {
        return druidDataSourceMap;
    }

    public Map<String, NamedParameterJdbcTemplate> getJdbcTemplateMap() {
        return jdbcTemplateMap;
    }

    public Map<String, ElasticsearchClient> getElasticsearchClientMap() {
        return elasticsearchClientMap;
    }
}
