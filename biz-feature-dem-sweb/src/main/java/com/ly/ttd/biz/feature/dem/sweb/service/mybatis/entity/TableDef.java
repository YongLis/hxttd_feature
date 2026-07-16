package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 数据表定义
 *
 * @TableName ttd_table_def
 */
@Data
@TableName("ttd_table_def")
public class TableDef extends BaseEntity {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 表名
     */
    @TableField("table_name")
    private String tableName;

    /**
     * 库名
     */
    @TableField("data_source")
    private String dataSource;

    /**
     * 关联topic
     */
    private String topic;
}