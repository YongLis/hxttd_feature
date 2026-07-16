package com.ly.ttd.biz.feature.admin.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 特征视图表
 *
 * @TableName ttd_feature_view
 */
@Data
@TableName("ttd_feature_view")
public class FeatureView extends BaseEntity {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long viewId;

    /**
     * 视图名称
     */
    @TableField("view_name")
    private String viewName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 实体id
     */
    @TableField("entity_id")
    private Long entityId;

    /**
     * 数据来源
     */
    @TableField("source_table")
    private String sourceTable;

    /**
     * 视图类型：offline, online, batch
     */
    @TableField("view_type")
    private String viewType;

    /**
     * 数据生命周期（天）
     */
    private Integer lifecycle;
}