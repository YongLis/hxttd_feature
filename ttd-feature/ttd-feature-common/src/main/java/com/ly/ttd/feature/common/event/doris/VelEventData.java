package com.ly.ttd.feature.common.event.doris;

import com.alibaba.fastjson2.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ly.ttd.feature.common.enums.VelocityTimeModeEnum;
import com.ly.ttd.utils.DateFormatUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 实时特征计算事件流数据
 *
 * @author yong.li
 * @since 2026/4/21 15:35
 */
@Data
public class VelEventData {

    /**
     * 接入点
     */
    @JsonProperty("point_code")
    private String pointCode;
    /**
     * 交易流水号
     */
    @JsonProperty("txn_id")
    private String txnId;

    /**
     * 业务编码
     */
    @JsonProperty("biz_code")
    private String bizCode;
    /**
     * 交易时间
     */
    @JsonProperty("txn_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date txnTime;

    /**
     * 特征代码
     */
    @JsonProperty("feature_code")
    private String featureCode;
    /**
     * 主值
     */
    @JsonProperty("master_value")
    private String masterValue;
    /**
     * 从值
     */
    @JsonProperty("salve_value")
    private String slaveValue;
    /**
     * 实时特征值
     */
    @JsonProperty("vel_value")
    private BigDecimal velValue;
    /**
     * 聚合函数(SUM/COUNT/AVG/MIN/MAX)
     */
    @JsonProperty("aggregate_mode")
    private String aggregateMode;

    /**
     * 时间模式(TTL/DAY/MONTH/YEAR)
     */
    @JsonProperty("time_mode")
    private String timeMode;

    /**
     * 时间单位
     */
    @JsonProperty("time_unit")
    private String timeUnit;

    /**
     * 时间窗口
     */
    @JsonProperty("time_window")
    private Integer timeWindow;

    /**
     * 过期时间
     */
    @JsonProperty("expire_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    @JsonProperty("row_key")
    private String rowKey;

    /**
     * 速率计算事件流数据的行键
     */
    @JsonProperty("stated_key")
    private String statedKey;

    public String buildRowKey() {
        return String.format("%s_%s_%s_%s", txnId, pointCode, featureCode, masterValue);
    }


    /**
     * 获取速率计算事件流数据的行键
     */
    public String getStatedKey() {

        VelocityTimeModeEnum cumTypeEnum = VelocityTimeModeEnum.getEnumByType(timeMode);

        switch (cumTypeEnum) {
            case TTL:
                statedKey = String.format("fea_ttl_%s_%s", featureCode, masterValue);
                break;
            case CURRENT_DAY:
                statedKey = String.format("fea_cd_%s_%s_%s", featureCode, masterValue, DateUtils.format(txnTime, "yyyyMMdd"));
                break;
            case CURRENT_MONTH:
                statedKey = String.format("fea_cm_%s_%s_%s", featureCode, masterValue, DateUtils.format(txnTime, "yyyyMM"));
                break;
            case CURRENT_YEAR:
                statedKey = String.format("fea_cy_%s_%s_%s", featureCode, masterValue, DateUtils.format(txnTime, "yyyy"));
                break;
            case FOREVER:
                statedKey = String.format("fea_ff_%s_%s", featureCode, masterValue);
                break;
            default:
                statedKey = String.format("fea_def_%s_%s", featureCode, masterValue);
                break;
        }
        return statedKey;
    }


    public Map<String, Object> convertDorisMap() {
        Map<String, Object> map = new HashMap<>();
        // doris数据库字段全部小写，将当前属性名转换为小写
        map.put("point_code", pointCode);
        map.put("biz_code", bizCode);
        map.put("txn_id", txnId);
        map.put("txn_time", DateFormatUtils.format(Objects.nonNull(txnTime) ? txnTime : new Date(), DateFormatUtils.S_YYYY_MM_DD_HH_MM_SS));
        map.put("feature_code", Objects.nonNull(featureCode) ? featureCode : "");
        map.put("master_value", Objects.nonNull(masterValue) ? masterValue : "");
        map.put("slave_value", Objects.nonNull(slaveValue) ? slaveValue : "");
        map.put("vel_value", Objects.nonNull(velValue) ? velValue : BigDecimal.ZERO);
        map.put("time_mode", Objects.nonNull(timeMode) ? timeMode : "");
        map.put("aggregate_mode", Objects.nonNull(aggregateMode) ? aggregateMode : "");
        map.put("time_unit", Objects.nonNull(timeUnit) ? timeUnit : "");
        map.put("time_window", Objects.nonNull(timeWindow) ? timeWindow : 0);
        map.put("expire_time", DateFormatUtils.format(Objects.nonNull(expireTime) ? expireTime : null, DateFormatUtils.S_YYYY_MM_DD_HH_MM_SS));
        map.put("row_key", getRowKey());

        return map;
    }


}