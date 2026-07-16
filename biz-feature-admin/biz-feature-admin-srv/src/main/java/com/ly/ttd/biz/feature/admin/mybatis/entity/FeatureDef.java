package com.ly.ttd.biz.feature.admin.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 特征定义表
 *
 * @TableName ttd_feature_def
 */
@Data
@TableName("ttd_feature_def")
public class FeatureDef extends BaseEntity {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long defId;

    /**
     * 特征名称
     */
    @TableField("feature_name")
    private String featureName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 特征数据类型
     */
    @TableField("object_type")
    private String objectType;

    /**
     * 转换逻辑
     */
    @TableField("transform")
    private String transform;

    /**
     * 版本
     */
    private String version;

    /**
     * 特征视图ID
     */
    @TableField("view_id")
    private String viewId;
}