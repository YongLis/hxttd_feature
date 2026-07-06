package com.ly.ttd.connector.api;

import com.ly.ttd.consts.enums.ExecuteState;
import lombok.Data;

/**
 * @author yong.li
 * @since 2026/3/27 11:56
 */
@Data
public class ConnectorResponse<T extends AbstractConnectorRequest> {

    /**
     * 请求参数
     */
    private T req;

    /**
     * 响应报文
     */
    private Object res;

    /**
     * 执行耗时
     */
    private Long cost;

    /**
     * 执行状态
     */
    private ExecuteState state;

    /**
     * 错误原因
     */
    private String errorMsg;

}
