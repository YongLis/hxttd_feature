package com.ly.ttd.language.srv.tip;

/**
 * @author yong.li
 * @since 2026/1/26 13:12
 */
public class ParamObj {
    /**
     * 类型
     */
    private String type;

    /**
     * 名称
     */
    private String name;


    public ParamObj() {
    }

    public ParamObj(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
