package com.ly.ttd.feature.common.model.connector;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * Elasticsearch连接器定义
 *
 * @author yong.li
 * @since 2026/5/23 21:27
 */
@Data
public class ElasticsearchConnectorModel extends ConnectorModel {
    private String endpoint;

    private String dsl;

    public static ElasticsearchConnectorModel convertResource(String resourceJson) {
        return JSON.parseObject(resourceJson, ElasticsearchConnectorModel.class);
    }

}
