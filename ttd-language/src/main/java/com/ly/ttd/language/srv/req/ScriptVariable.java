package com.ly.ttd.language.srv.req;

import java.util.Map;

/**
 * @author yong.li
 * @since 2026/5/11 15:08
 */
public class ScriptVariable {
    /**
     * 规则编码
     */
    private String resourceKey;

    /**
     * 脚本语言
     */
    private String lang;
    /**
     * 脚本
     */
    private String script;
    /**
     * 参数
     */
    private Map<String, Object> params;


    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
