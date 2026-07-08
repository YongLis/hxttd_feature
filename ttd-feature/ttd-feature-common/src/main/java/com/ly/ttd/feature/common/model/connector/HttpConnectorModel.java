package com.ly.ttd.feature.common.model.connector;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.Map;

/**
 * Http连接器定义
 *
 * @author yong.li
 * @since 2026/5/23 21:27
 */
@Data
public class HttpConnectorModel extends ConnectorModel {

    private String url;
    private String method;
    private Map<String, Object> header;
    private Map<String, Object> param;

    public static HttpConnectorModel convertResource(String resourceJson) {
        return JSON.parseObject(resourceJson, HttpConnectorModel.class);
    }

}
