package com.ly.ttd.feature.common.event.doris;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 实时特征事件流
 *
 * @author yong.li
 * @since 2026/4/24 15:29
 */
@Setter
@Getter
public class VelReadEvent {
    /**
     * 速率因子key
     */
    @JsonProperty("factor_code")
    private String factorCode;

    @JsonProperty("factor_name")
    private String factorName;

    /**
     * 是否计入当笔交易 0不计入，1计入计算
     */
    @JsonProperty("vel_cur_flag")
    private String velCurFlag;

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
     * 交易时间
     */
    @JsonProperty("txn_time")
    private Date txnTime;

    /**
     * 速率代码
     */
    @JsonProperty("feature_code")
    private String featureCode;

    /**
     * 主值
     */
    @JsonProperty("master_values")
    private List<String> masterValues;

    /**
     * 从值
     */
    @JsonProperty("slave_values")
    private List<String> slaveValues;
    /**
     * 速率值
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

    @JsonProperty("row_key")
    private String rowKey;

    public String buildRowKey(String masterValue) {
        return String.format("%s:%s:%s:%s", txnId, pointCode, featureCode, masterValue);
    }
}
