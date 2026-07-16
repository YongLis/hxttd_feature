package com.ly.ttd.feature.admin.api.dto;

import com.ly.ttd.feature.common.model.DataFieldModel;
import lombok.Data;

import java.util.List;

/**
 * @author yong.li
 * @since 2026/7/10 14:55
 */
@Data
public class JdbcConnectorDto extends ConnectorDto {

    private String connectorType;
    /**
     * 前置条件
     */
    private String condition;
    /**
     * 参数列表
     */
    private List<DataFieldModel> fields;

    private String returnType;


    /**
     * 数据源名称
     */
    private String dataSourceName;
    /**
     * SQL语句
     */
    private String sql;

}
