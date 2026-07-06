package com.ly.ttd.feature.srv.vel.compile;

import lombok.Data;

/**
 * 数据集结构
 *
 * @author yong.li
 * @since 2026/4/3 09:47
 */

@Data
public class DataFrame {
    /**
     * 元字段名称
     */
    private String columnName;

    /**
     * 元字段名称(中文)
     */
    private String columnNameDesc;

    /**
     * 返回值类型
     */
    private String returnType;

    public DataFrame() {
    }

    public DataFrame(String columnName) {
        this.columnName = columnName;
    }
}
