package com.ly.ttd.biz.feature.admin.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 特征数据表
 *
 * @TableName ttd_feature_data_store
 */
@Data
@TableName("ttd_feature_data_store")
public class FeatureDataStore extends BaseEntity {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 实体键
     */
    @TableField("entity_key")
    private String entityKey;

    /**
     * 特征名称
     */
    @TableField("feature_name")
    private String featureName;

    /**
     * 特征值
     */
    @TableField("feature_value")
    private String featureValue;

    /**
     * 特征值类型
     */
    @TableField("object_type")
    private String objectType;

}