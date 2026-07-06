package com.ly.ttd.feature.common.event.doris;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ly.ttd.utils.DateFormatUtils;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/5/20 16:09
 */
@Data
public class VelMasterValueRecord {
    /**
     * 速率编码
     */
    @JsonProperty("feature_code")
    private String featureCode;
    /**
     * 主值
     */
    @JsonProperty("master_value")
    private String masterValue;
    /**
     * 缓存item长度
     */
    @JsonProperty("cache_size")
    private Integer cacheSize;

    /**
     * 缓存数据字节长度
     */
    @JsonProperty("cache_byte_size")
    private Long cacheByteSize;

    /**
     * 过期时间
     */
    @JsonProperty("expire_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;
    @JsonProperty("txn_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date txnTime;

    @JsonProperty("redis_key")
    private String redisKey;

    @JsonProperty("row_key")
    private String rowKey;

    public String buildRowKey() {
        return String.format("%s_%s", featureCode, masterValue);
    }

    public Map<String, Object> convertDorisMap() {
        Map<String, Object> map = new HashMap<>();
        // doris数据库字段全部小写，将当前属性名转换为小写
        map.put("feature_code", featureCode);
        map.put("master_value", masterValue);
        map.put("cache_size", cacheSize);
        map.put("cache_byte_size", cacheByteSize);
        map.put("expire_time", DateFormatUtils.format(expireTime, DateFormatUtils.S_YYYY_MM_DD_HH_MM_SS));
        map.put("txn_time", DateFormatUtils.format(txnTime, DateFormatUtils.S_YYYY_MM_DD_HH_MM_SS));
        map.put("redis_key", redisKey);
        map.put("row_key", getRowKey());
        return map;
    }
}
