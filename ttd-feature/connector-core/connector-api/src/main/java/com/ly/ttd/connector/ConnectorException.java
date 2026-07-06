package com.ly.ttd.connector;

import com.ly.ttd.connector.enums.ConnectorResultCodeEnum;

/**
 * 连接器异常
 *
 * @author yong.li
 * @since 2026/04/15
 */
public class ConnectorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private String errorCode;

    public ConnectorException(String message) {
        super(message);
        this.errorCode = "9999";
    }

    public ConnectorException(ConnectorResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.errorCode = resultCodeEnum.getCode();
    }

    public ConnectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectorException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ConnectorException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
