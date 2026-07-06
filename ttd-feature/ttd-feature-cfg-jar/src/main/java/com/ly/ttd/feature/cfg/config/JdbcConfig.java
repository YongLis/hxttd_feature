package com.ly.ttd.feature.cfg.config;

import lombok.Data;

/**
 * JDBC 数据源配置
 *
 * @author yong.li
 * @since 2026/3/27 16:08
 */
@Data
public class JdbcConfig {

    /**
     * 数据源名称
     */
    private String sourceName;

    /**
     * JDBC URL
     */
    private String url;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 驱动类名
     */
    private String driverName;

    // ========== Druid 连接池配置 ==========

    /**
     * 初始化连接数
     */
    private int initialSize = 10;

    /**
     * 最大活跃连接数
     */
    private int maxActive = 50;

    /**
     * 最小空闲连接数
     */
    private int minIdle = 10;

    /**
     * 获取连接最大等待时间(ms)
     */
    private long maxWait = 60000;

    /**
     * 是否回收泄露连接
     */
    private boolean removeAbandoned = true;

    /**
     * 泄露连接超时时间(ms)
     */
    private long removeAbandonedTimeoutMillis = 300000;

    /**
     * 检测空闲连接间隔(ms)
     */
    private long timeBetweenEvictionRunsMillis = 30000;

    /**
     * 连接保持空闲不被驱逐的最小时间(ms)
     */
    private long minEvictableIdleTimeMillis = 180000;

    /**
     * 是否保活
     */
    private boolean keepAlive = true;

    /**
     * 获取连接时是否检测有效性
     */
    private boolean testOnBorrow = false;

    /**
     * 归还连接时是否检测有效性
     */
    private boolean testOnReturn = false;

    /**
     * 空闲时是否检测有效性
     */
    private boolean testWhileIdle = true;

    /**
     * 验证查询SQL
     */
    private String validationQuery = "SELECT 1";

    /**
     * 是否缓存PreparedStatement
     */
    private boolean poolPreparedStatements = true;

    /**
     * PreparedStatement最大缓存数
     */
    private int maxPoolPreparedStatementPerConnectionSize = 20;
}
