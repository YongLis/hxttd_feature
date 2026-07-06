package com.ly.ttd.feature.engine.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author yong.li
 * @since 2026/6/30 13:32
 */
@Configuration
public class DataSourceBuilder {

    @Bean
//    @ConditionalOnProperty("ttd.db.feature.datasource.url")
    @ConditionalOnBean(TtdFeatureConfig.class)
    public DataSource buildDataSource(TtdFeatureConfig config) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(config.getDbFeatureDataSourceUrl());
        dataSource.setUsername(config.getDbFeatureDataSourceUsername());
        dataSource.setPassword(config.getDbFeatureDataSourcePassword());
        dataSource.setDriverClassName(config.getDbFeatureDataSourceDriver());
        dataSource.setInitialSize(10);
        dataSource.setMaxActive(50);
        dataSource.setMinIdle(10);
        dataSource.setMaxWait(60000);

        // 连接泄漏检测
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeoutMillis(300000);

        dataSource.setTimeBetweenEvictionRunsMillis(30000);
        dataSource.setMinEvictableIdleTimeMillis(180000);
        dataSource.setKeepAlive(true);
        dataSource.setTestOnBorrow(false);

        return dataSource;
    }


}
