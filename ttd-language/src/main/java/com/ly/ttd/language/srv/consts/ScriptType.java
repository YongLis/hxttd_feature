package com.ly.ttd.language.srv.consts;

/**
 * @author yong.li
 * @since 2026/5/11 15:16
 */
public enum ScriptType {
    GROOVY("groovy", "groovy"),
    AVIATOR("aviator", "aviator"),
    JEXL("jexl", "jexl");

    private String code;
    private String name;

    ScriptType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
