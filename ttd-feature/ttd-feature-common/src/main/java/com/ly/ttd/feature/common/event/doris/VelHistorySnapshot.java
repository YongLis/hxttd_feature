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
public class VelHistorySnapshot {
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
     * 交易号
     */
    @JsonProperty("txn_id")
    private String txnId;

    /**
     * 快照数据(变更前)
     */
    @JsonProperty("before_data")
    private String beforeData;
    /**
     * 快照数据(变更后)
     */
    @JsonProperty("after_data")
    private String afterData;

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
        map.put("txn_id", txnId);
        map.put("before_data", beforeData);
        map.put("after_data", afterData);
        map.put("expire_time", DateFormatUtils.format(expireTime, DateFormatUtils.S_YYYY_MM_DD_HH_MM_SS));
        map.put("txn_time", DateFormatUtils.format(txnTime, DateFormatUtils.S_YYYY_MM_DD_HH_MM_SS));
        map.put("redis_key", redisKey);
        map.put("row_key", getRowKey());
        return map;
    }

}
