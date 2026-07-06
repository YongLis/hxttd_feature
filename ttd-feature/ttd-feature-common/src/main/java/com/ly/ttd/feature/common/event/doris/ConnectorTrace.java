package com.ly.ttd.feature.common.event.doris;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * 连接器调用跟踪
 * 记录连接器调用的详细信息，包括请求参数、响应结果、异常信息等
 *
 * @author yong.li
 * @since 2026/6/15 14:11
 */
@Data
public class ConnectorTrace {
    @JsonProperty("txn_id")
    private String txnId;
    @JsonProperty("connector_id")
    private String connectorId;
    @JsonProperty("connector_type")
    private String connectorType;

    /**
     * 耗时/毫秒
     */
    private Long cost;
    /**
     * 调用状态
     */
    @JsonProperty("call_state")
    private String callState;

    /**
     * 请求字段
     */
    @JsonProperty("request_json")
    private String requestJson;

    /**
     * 响应字段
     */
    @JsonProperty("response_json")
    private String responseJson;

    /**
     * 请求时间
     */
    @JsonProperty("call_time")
    private Date callTime;

    /**
     * 错误消息
     */
    @JsonProperty("error_msg")
    private String errorMsg;

}
