package com.ly.ttd.feature.cfg.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/4/14 16:23
 */
@Data
public class HbaseConfig {

    private String sourceName;

    // 指定 Zookeeper 服务器地址列表，格式为 host1:port,host2:port,...。
    public static final String ZOOKEEPER_QUORUM = "zookeeper.quorum";
    // ookeeper 客户端端口，默认是 2181。
    public static final String ZOOKEEPER_PROPERTY_CLIENTPORT = "zookeeper.property.clientPort";
    /**
     * 扫描缓存大小，默认为 100。增加这个值可以减少 RPC 调用次数，但会增加内存消耗。
     */
    public static final String CLIENT_SCANNER_CACHING = "client.scanner.caching";
    /**
     * 单个 KeyValue 的最大大小，默认是 10485760 bytes (10MB)。
     */
    public static final String CLIENT_KEYVALUE_MAXSIZE = "client.keyvalue.maxsize";
    /**
     * RPC 调用超时时间，单位为毫秒。
     */
    public static final String RPC_TIMEOUT = "rpc.timeout";
    /**
     * 扫描器超时时间，单位为毫秒。
     */
    public static final String CLIENT_SCANNER_TIMEOUT_PERIOD = "client.scanner.timeout.period";
    /**
     * 单次扫描返回的最大结果数量。
     */
    public static final String CLIENT_SCANNER_MAX_RESULT_SIZE = "client.scanner.max.result.size";
    /**
     * 客户端操作超时时间，单位为毫秒。
     */
    public static final String CLIENT_OPERATION_TIMEOUT = "client.operationTimeout";

    public static final String HBASE_CLIENT_RETRIES_NUMBER = "hbase.client.retries.number";


    /**
     * 指定 Zookeeper 服务器地址列表，格式为 host1:port,host2:port,...。
     */
    private String zookeeperQuorum;
    /**
     * Zookeeper 客户端端口，默认是 2181。
     */
    private String clientPort;
    /**
     * 扫描缓存大小，默认为 100。增加这个值可以减少 RPC 调用次数，但会增加内存消耗。
     */
    private Integer clientScannerCaching = 100;
    /**
     * 单个 KeyValue 的最大大小，默认是 10485760 bytes (10MB)。
     */
    private Long clientKeyValueMaxSize = 1024 * 1024 * 10L;

    /**
     * RPC 调用超时时间，单位为毫秒。
     */
    private Integer rpcTimeout = 30000;
    /**
     * 扫描器超时时间，单位为毫秒。
     */
    private Integer clientScannerTimeoutPeriod = 30000;

    /**
     * 单次扫描返回的最大结果数量。
     */
    private Integer clientScannerMaxResultSize = 100;

    /**
     * 客户端操作超时时间，单位为毫秒。
     */
    private Integer clientOperationTimeout = 30000;

    /**
     * 重试次数
     */
    private Integer hbaseClientRetriesNumber = 3;


    public Map<String, Object> buildConfigMap() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ZOOKEEPER_QUORUM, zookeeperQuorum);
        configMap.put(ZOOKEEPER_PROPERTY_CLIENTPORT, clientPort);
        configMap.put(CLIENT_SCANNER_CACHING, clientScannerCaching);
        configMap.put(CLIENT_KEYVALUE_MAXSIZE, clientKeyValueMaxSize);
        configMap.put(CLIENT_SCANNER_TIMEOUT_PERIOD, clientScannerTimeoutPeriod);
        configMap.put(CLIENT_SCANNER_MAX_RESULT_SIZE, clientScannerMaxResultSize);
        configMap.put(CLIENT_OPERATION_TIMEOUT, clientOperationTimeout);
        configMap.put(RPC_TIMEOUT, rpcTimeout);
        configMap.put(HBASE_CLIENT_RETRIES_NUMBER, hbaseClientRetriesNumber);
        return configMap;
    }

}
