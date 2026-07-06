package com.ly.ttd.feature.common.enums;

/**
 * @author yong.li
 * @since 2026/4/14 13:00
 */

public enum FeatureResultCodeEnum {
    SUCCESS("0000", "成功"),
    ERROR("9999", "失败"),


    // ES connector数据源异常
    ES_CONNECTOR_ERROR("2000", "es connector数据源异常"),

    ES_CONNECTOR_NOT_FOUND("2001", "es connector数据源不存在"),
    ES_CONNECTOR_NOT_SUPPORT("2002", "es connector数据源不支持"),


    // http connector数据源异常
    HTTP_CONNECTOR_ERROR("3000", "http connector数据源异常"),
    HTTP_CONNECTOR_STATUS_CODE_NOT_200("3001", "http请求错误"),


    // jdbc connector数据源异常
    JDBC_CONNECTOR_ERROR("4000", "jdbc connector数据源异常"),
    JDBC_CONNECTOR_NOT_FOUND("4001", "jdbc connector数据源不存在"),
    JDBC_CONNECTOR_NOT_SUPPORT("4002", "jdbc connector数据源不支持"),


    // hbase connector数据源异常
    HBASE_CONNECTOR_ERROR("5000", "hbase connector数据源异常"),


    // rpc connector数据源异常
    RPC_CONNECTOR_ERROR("6000", "spark connector数据源异常"),

    // FLOW
    FLOW_ERROR("6500", "节点执行异常"),
    FLOW_INSTANCE_BUILD_ERROR("6501", "初始化执行实例失败"),


    // RULE
    RULE_NOT_FOUND("7000", "规则找不到"),


    // RULE
    DECISION_ILLEGAL("7500", "决策流程为空"),
    ;


    private String code;
    private String message;

    FeatureResultCodeEnum(String code, String message) {
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
