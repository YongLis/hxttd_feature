package com.ly.ttd.feature.cfg.connector;

import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.config.JdbcConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JDBC 多数据源配置解析器
 * <p>
 * 从 Spring Environment 中读取 Nacos 拉取的 JDBC 多数据源配置，
 * 解析为 {@link JdbcConfig} 并注册到 {@link FeatureConfiguration}。
 * <p>
 * 调用时机：由 {@code FeatureConfigInit#afterSingletonsInstantiated()} 显式调用
 * {@link #parseJdbcConfig()}，确保在 {@code Connectors} 初始化之前完成数据源注册。
 * <p>
 * 配置格式（在 Nacos mysql.properties 中）：
 * <pre>
 * ttd.datasource.jdbc[0].sourceName=&lt;名称&gt;
 * ttd.datasource.jdbc[0].url=&lt;JDBC URL&gt;
 * ttd.datasource.jdbc[0].userName=&lt;用户名&gt;
 * ttd.datasource.jdbc[0].password=&lt;密码&gt;
 * ttd.datasource.jdbc[0].driverName=&lt;驱动类名&gt;
 * ttd.datasource.jdbc[1].sourceName=&lt;名称&gt;
 * ...
 * </pre>
 *
 * @author yong.li
 * @since 2026/7/1 14:23
 */
@Slf4j
@Component
public class JdbcDataSourceParser {

    private static final String JDBC_PREFIX = "ttd.datasource.jdbc";

    /**
     * 匹配 [n].property 格式，例如 "[0].url" → index=0, key="url"
     */
    private static final Pattern INDEX_KEY_PATTERN = Pattern.compile("^\\[(\\d+)]\\.(.+)$");

    @Resource
    private ConfigurableEnvironment environment;

    private final FeatureConfiguration configuration;

    public JdbcDataSourceParser(FeatureConfiguration configuration) {
        log.info("JdbcDataSourceParser init");
        this.configuration = configuration;
    }

    /**
     * 解析并注册所有 JDBC 多数据源配置。
     * 由 {@code FeatureConfigInit#afterSingletonsInstantiated()} 调用。
     */
    public void parseJdbcConfig() {
        Properties allProps = collectProperties(environment, JDBC_PREFIX);
        if (allProps.isEmpty()) {
            log.info("JdbcDataSourceParser — no JDBC data source config found with prefix '{}'", JDBC_PREFIX);
            return;
        }

        Map<Integer, Properties> groupedByIndex = groupByIndex(allProps);
        log.info("JdbcDataSourceParser — found {} JDBC data source(s) to register", groupedByIndex.size());

        for (Map.Entry<Integer, Properties> entry : groupedByIndex.entrySet()) {
            int index = entry.getKey();
            Properties p = entry.getValue();

            JdbcConfig config = new JdbcConfig();
            String sourceName = p.getProperty("sourceName");
            if (sourceName == null || sourceName.isEmpty()) {
                log.warn("JdbcDataSourceParser — datasource[{}] missing sourceName, skip", index);
                continue;
            }
            config.setSourceName(sourceName);
            config.setUrl(p.getProperty("url"));
            config.setUserName(p.getProperty("userName"));
            config.setPassword(p.getProperty("password"));
            config.setDriverName(p.getProperty("driverName"));

            // 连接池参数（可选，未配置时使用 JdbcConfig 默认值）
            setIfPresent(p, "initialSize", v -> config.setInitialSize(Integer.parseInt(v)));
            setIfPresent(p, "maxActive", v -> config.setMaxActive(Integer.parseInt(v)));
            setIfPresent(p, "minIdle", v -> config.setMinIdle(Integer.parseInt(v)));
            setIfPresent(p, "maxWait", v -> config.setMaxWait(Long.parseLong(v)));
            setIfPresent(p, "removeAbandoned", v -> config.setRemoveAbandoned(Boolean.parseBoolean(v)));
            setIfPresent(p, "removeAbandonedTimeoutMillis", v -> config.setRemoveAbandonedTimeoutMillis(Long.parseLong(v)));
            setIfPresent(p, "timeBetweenEvictionRunsMillis", v -> config.setTimeBetweenEvictionRunsMillis(Long.parseLong(v)));
            setIfPresent(p, "minEvictableIdleTimeMillis", v -> config.setMinEvictableIdleTimeMillis(Long.parseLong(v)));
            setIfPresent(p, "keepAlive", v -> config.setKeepAlive(Boolean.parseBoolean(v)));
            setIfPresent(p, "testOnBorrow", v -> config.setTestOnBorrow(Boolean.parseBoolean(v)));
            setIfPresent(p, "testOnReturn", v -> config.setTestOnReturn(Boolean.parseBoolean(v)));
            setIfPresent(p, "testWhileIdle", v -> config.setTestWhileIdle(Boolean.parseBoolean(v)));
            setIfPresent(p, "validationQuery", v -> config.setValidationQuery(v));
            setIfPresent(p, "poolPreparedStatements", v -> config.setPoolPreparedStatements(Boolean.parseBoolean(v)));
            setIfPresent(p, "maxPoolPreparedStatementPerConnectionSize", v -> config.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(v)));

            configuration.addJdbcConfig(config);
            log.info("JdbcDataSourceParser — registered JDBC data source: sourceName={}, url={}",
                    config.getSourceName(), config.getUrl());
        }
    }

    /**
     * 从 Environment 中采集所有匹配前缀的属性，去除前缀后返回。
     */
    private Properties collectProperties(ConfigurableEnvironment env, String prefix) {
        Properties result = new Properties();
        for (PropertySource<?> ps : env.getPropertySources()) {
            if (ps instanceof EnumerablePropertySource) {
                for (String key : ((EnumerablePropertySource<?>) ps).getPropertyNames()) {
                    if (key.startsWith(prefix)) {
                        Object value = ps.getProperty(key);
                        if (value != null) {
                            result.setProperty(key.substring(prefix.length()), value.toString());
                        }
                    }
                }
            }
        }
        return result;
    }

    private static void setIfPresent(Properties p, String key, Consumer<String> setter) {
        String value = p.getProperty(key);
        if (value != null && !value.isEmpty()) {
            setter.accept(value);
        }
    }

    /**
     * 按索引分组：key 格式为 "[0].url" → 解析出 index=0, subKey="url"。
     * 使用 TreeMap 保证按索引排序。
     */
    private Map<Integer, Properties> groupByIndex(Properties props) {
        Map<Integer, Properties> grouped = new TreeMap<>();
        for (String key : props.stringPropertyNames()) {
            Matcher matcher = INDEX_KEY_PATTERN.matcher(key);
            if (!matcher.matches()) {
                log.debug("JdbcDataSourceParser — skip unrecognized key: {}", key);
                continue;
            }
            int index = Integer.parseInt(matcher.group(1));
            String subKey = matcher.group(2);
            grouped.computeIfAbsent(index, k -> new Properties())
                    .setProperty(subKey, props.getProperty(key));
        }
        return grouped;
    }
}
