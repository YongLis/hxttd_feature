package com.ly.ttd.feature.common.language;

import lombok.Data;

import java.util.Map;

/**
 * @author yong.li
 * @since 2026/5/11 15:08
 */
@Data
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

}
