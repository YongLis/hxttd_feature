package com.ly.ttd.feature.common.event.doris;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ly.ttd.utils.DateFormatUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/5/20 16:09
 */
@Data
public class VelReadSnapshot {
    @JsonProperty("factor_code")
    private String factorCode;
    @JsonProperty("factor_name")
    private String factorName;
    /**
     * 速率编码
     */
    @JsonProperty("feature_code")
    private String featureCode;
    /**
     * 接入点
     */
    @JsonProperty("point_code")
    private String pointCode;
    /**
     * 主值
     */
    @JsonProperty("master_value")
    private List<String> masterValue;
    /**
     * 从值
     */
    @JsonProperty("txn_id")
    private String txnId;
    /**
     * 快照数据(计算数据)
     */

    private String data;
    /**
     * 计算值
     */
    private BigDecimal value;

    /**
     * 过期时间
     */
    @JsonProperty("expire_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;
    /**
     * 查询时间
     */
    @JsonProperty("query_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date queryTime;


    @JsonProperty("row_key")
    private String rowKey;

    public String buildRowKey() {
        return String.format("%s_%s_%s", txnId, featureCode, pointCode);
    }

    public Map<String, Object> convertDorisMap() {
        Map<String, Object> map = new HashMap<>();
        // doris数据库字段全部小写，将当前属性名转换为小写
        map.put("feature_code", featureCode);
        map.put("master_value", masterValue);
        map.put("factor_code", factorCode);
        map.put("factor_name", factorName);
        map.put("txn_id", txnId);
        map.put("data", data);
        map.put("value", value);
        map.put("expire_time", DateFormatUtils.format(expireTime, DateFormatUtils.S_YYYY_MM_DD_HH_MM_SS));
        map.put("query_time", DateFormatUtils.format(queryTime, DateFormatUtils.S_YYYY_MM_DD_HH_MM_SS));
        map.put("row_key", getRowKey());
        return map;
    }

    /**
     * 去读快照详情
     *
     * @author yong.li
     * @since 2026/8/22 18:15
     */
    @Data
    public static class ReadSnapshotDetail {
        private VelReadEvent readEventDto;
        private String cacheData;

        public ReadSnapshotDetail() {
        }

        public ReadSnapshotDetail(VelReadEvent readEventDto, String cacheData) {
            this.readEventDto = readEventDto;
            this.cacheData = cacheData;
        }
    }
}
