package com.ly.ttd.feature.sample.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ttd_meta_field")
public class MetaFieldEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String resourceKey;
    private String resourceName;
    private String version;
    private Long projectId;
    private String language;
    private String script;
    private String returnType;
    private String defaultValue;
    private String exceptionValue;
    private String categoryTag;
}
