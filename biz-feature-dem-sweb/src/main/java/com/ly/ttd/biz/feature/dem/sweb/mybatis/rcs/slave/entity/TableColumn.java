package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 数据表列定义
 *
 * @TableName ttd_table_column
 */
@Data
@TableName("ttd_table_column")
public class TableColumn extends BaseEntity {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 表id
     */
    @TableField("table_name")
    private String tableName;

    /**
     * 列名
     */
    @TableField("column_name")
    private String columnName;

    /**
     * 类型
     */
    @TableField("column_type")
    private String columnType;

    /**
     * 是否允许为空：Y是 N否
     */
    private String empty;

    /**
     * 指标编码
     */
    @TableField("feature_code")
    private String featureCode;
}