package com.ly.ttd.feature.common.model.connector;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.feature.common.model.DataFieldModel;
import lombok.Data;

import java.util.List;

/**
 * Elasticsearch连接器定义
 *
 * @author yong.li
 * @since 2026/5/23 21:27
 */
@Data
public class ElasticsearchConnectorModel extends ConnectorModel {

    /**
     * 前置条件
     */
    private String condition;

    private String endpoint;

    private String dsl;
    /**
     * 参数列表
     */
    private List<DataFieldModel> fields;


    public static ElasticsearchConnectorModel convertResource(String resourceJson) {
        return JSON.parseObject(resourceJson, ElasticsearchConnectorModel.class);
    }

}
