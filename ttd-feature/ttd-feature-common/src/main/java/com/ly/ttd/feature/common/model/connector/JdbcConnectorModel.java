package com.ly.ttd.feature.common.model.connector;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.feature.common.model.DataFieldModel;
import lombok.Data;

import java.util.List;

/**
 * @author yong.li
 * @since 2026/5/23 21:27
 */
@Data
public class JdbcConnectorModel extends ConnectorModel {
    /**
     * 数据源名称
     */
    private String dataSourceName;
    /**
     * SQL语句
     */
    private String sql;

    /**
     * 参数列表
     */
    private List<DataFieldModel> fields;


    /**
     * 前置条件
     */
    private String condition;


    public static JdbcConnectorModel convertResource(String resourceJson) {
        return JSON.parseObject(resourceJson, JdbcConnectorModel.class);
    }


}
