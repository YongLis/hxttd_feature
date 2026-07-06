package com.ly.ttd.feature.cfg;

/**
 * @author yong.li
 * @since 2026/6/10 13:56
 */
public class ThreadPoolNames {

    /**
     * 元字段线程池
     */
    public static final String THREAD_META_FIELD = "THREAD_META_FIELD";

    /**
     * 指标线程池
     */
    public static final String THREAD_METRIC = "THREAD_METRIC";


    /**
     * 实时特征写入
     */
    public static final String THREAD_FEATURE_WRITE = "THREAD_FEATURE_WRITE";


    /**
     * 实时特征线程池
     */
    public static final String THREAD_FEATURE_FEATURES = "THREAD_FEATURE";


    /**
     * 异步日志线程池
     */
    public static final String THREAD_LOG = "THREAD_LOG";


    /**
     * 指标查询
     */
    public static final String THREAD_FACTOR_GET_VALUE = "THREAD_FACTOR_GET_VALUE";


    /**
     * 连接器线程池
     */
    public static final String THREAD_CONNECTOR = "THREAD_CONNECTOR";


    /**
     * Hbase客户端连接池
     */

    public static final String THREAD_HBASE_CLIENT = "THREAD_HBASE_CLIENT";
}
