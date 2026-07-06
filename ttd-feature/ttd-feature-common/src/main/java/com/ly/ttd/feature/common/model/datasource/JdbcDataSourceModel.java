package com.ly.ttd.feature.common.model.datasource;

import lombok.Data;

/**
 * @author yong.li
 * @since 2026/6/9 11:01
 */
@Data
public class JdbcDataSourceModel {
    private String sourceCode;
    private String sourceName;

    private String url;
    private String userName;
    private String password;
    private String driverName;
}
