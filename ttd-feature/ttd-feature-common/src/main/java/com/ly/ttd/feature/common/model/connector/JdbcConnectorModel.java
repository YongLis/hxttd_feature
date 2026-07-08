package com.ly.ttd.feature.common.model.connector;

import com.alibaba.fastjson.JSON;
import lombok.Data;

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
    public static JdbcConnectorModel convertResource(String resourceJson) {
        return JSON.parseObject(resourceJson, JdbcConnectorModel.class);
    }


}
