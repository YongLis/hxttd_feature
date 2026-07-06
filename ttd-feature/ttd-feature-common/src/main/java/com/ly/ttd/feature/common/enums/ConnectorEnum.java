package com.ly.ttd.feature.common.enums;

/**
 * @author yong.li
 * @since 2026/3/27 11:37
 */
public enum ConnectorEnum {
    ES("ES", "ES"),
    JDBC("JDBC", "数据库"),
    HBASE("HBASE", "Hbase"),
    HTTP("HTTP", "HTTP"),
    RPC("RPC", "RPC");


    private String code;
    private String desc;

    private ConnectorEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ConnectorEnum getEnumByCode(String code) {
        for (ConnectorEnum connectorEnum : ConnectorEnum.values()) {
            if (connectorEnum.getCode().equals(code)) {
                return connectorEnum;
            }
        }
        return null;
    }

}
