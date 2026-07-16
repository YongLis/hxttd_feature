package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 元数据实体表
 *
 * @TableName ttd_feature_entity
 */
@Data
@TableName("ttd_feature_entity")
public class FeatureEntity extends BaseEntity {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long entityId;

    /**
     * 实体名称
     */
    @TableField("entity_name")
    private String entityName;

    /**
     * 关联主键
     */
    @TableField("join_key")
    private String joinKey;

    /**
     * 备注
     */
    private String remark;
}