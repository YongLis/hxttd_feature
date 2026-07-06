package com.ly.ttd.connector.enums;

/**
 * @author yong.li
 * @since 2026/4/14 13:00
 */

public enum ConnectorResultCodeEnum {
    SUCCESS("0000", "成功"),
    ERROR("9999", "失败"),


    // ES connector数据源异常
    ES_CONNECTOR_ERROR("2000", "数据源异常"),
    ES_CONNECTOR_NOT_FOUND("2001", "数据源不存在"),
    ES_CONNECTOR_NOT_SUPPORT("2002", "数据源不支持"),


    // http connector数据源异常
    HTTP_CONNECTOR_ERROR("3000", "数据源异常"),
    HTTP_CONNECTOR_STATUS_CODE_NOT_200("3001", "http请求错误"),


    // jdbc connector数据源异常
    JDBC_CONNECTOR_ERROR("4000", "数据源异常"),
    JDBC_CONNECTOR_DATA_SOURCE_NAME_EMPTY("4001", "数据源名称不能为空"),
    JDBC_CONNECTOR_NOT_FOUND("4003", "数据源不存在"),
    JDBC_CONNECTOR_NOT_SUPPORT("4002", "数据源不支持"),


    // hbase connector数据源异常
    HBASE_CONNECTOR_ERROR("5000", "数据源异常"),


    // rpc connector数据源异常
    RPC_CONNECTOR_ERROR("6000", "数据源异常"),
    ;


    private String code;
    private String message;

    ConnectorResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
