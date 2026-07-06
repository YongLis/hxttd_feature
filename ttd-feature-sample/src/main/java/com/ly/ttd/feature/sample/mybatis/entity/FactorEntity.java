package com.ly.ttd.feature.sample.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ttd_factor")
public class FactorEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String resourceKey;
    private String resourceName;
    private String version;
    private Long projectId;
    private String factorType;
    private String returnType;
    private String defaultValue;
    private String exceptionValue;
    private Long timeout;
    private String resourceJson;
    private String refFeatureCode;
}
