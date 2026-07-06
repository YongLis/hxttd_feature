package com.ly.ttd.biz.admin.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 特征配置实体 (表: ttd_feature_config)
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ttd_feature_config")
public class FeatureConfigEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String resourceKey;

    private String resourceName;

    private String version;

    private Long projectId;

    private String featureCode;

    private String defaultValue;
    private String exceptionValue;
    private Long timeout;

    private String mainDimension;
    private String slaveDimension;
    private String language;

    private String conditionScript;

    private String mainDimScript;

    private String slaveDimScript;

    private String metaFields;

    private String returnType;

    private String valueType;

    private String valueScript;

    private String fixValue;

    private String aggregateMode;

    private String timeMode;

    private String timeUnit;

    private Integer timeWindow;

    private String resourceJson;
}
