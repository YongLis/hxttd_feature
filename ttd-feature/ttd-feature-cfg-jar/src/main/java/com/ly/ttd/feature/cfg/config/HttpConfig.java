package com.ly.ttd.feature.cfg.config;

import lombok.Data;

import java.util.Map;

/**
 * @author yong.li
 * @since 2026/3/27 16:08
 */
@Data
public class HttpConfig {
    private String sourceName;
    private String url;

    /**
     * POST, GET
     */
    private String method;
    private Map<String, String> headers;
    private String body;

}
