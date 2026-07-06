package com.ly.ttd.feature.cfg;

import com.alibaba.druid.filter.FilterAdapter;
import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * @author yong.li
 * @since 2026/6/15 22:22
 */
@Slf4j
public class ConnectionCreateFilter extends FilterAdapter {

    private String dataSourceName;
    private Consumer<String> consumer;

    public ConnectionCreateFilter(String dataSourceName, Consumer<String> consumer) {
        this.dataSourceName = dataSourceName;
        this.consumer = consumer;
    }

    @Override
    public ConnectionProxy connection_connect(FilterChain chain, Properties info) throws SQLException {
        ConnectionProxy connection = super.connection_connect(chain, info);
        // --- 物理连接已成功创建 ---
        consumer.accept(dataSourceName);
        log.info("create jdbcTemplate success, dataSourceName: {}", dataSourceName);
        return connection;
    }
}
