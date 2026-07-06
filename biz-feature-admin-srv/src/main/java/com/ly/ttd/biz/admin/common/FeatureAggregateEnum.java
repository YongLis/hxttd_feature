package com.ly.ttd.biz.admin.common;

/**
 * 特征聚合函数
 *
 * @author yong.li
 * @since 2026/5/19 14:12
 */
public enum FeatureAggregateEnum {

    SUM("SUM", "求和"),
    AVG("AVG", "求平均"),
    MAX("MAX", "求最大"),
    MIN("MIN", "求最小"),
    COUNT("COUNT", "去重计数");

    private String code;
    private String msg;

    FeatureAggregateEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
