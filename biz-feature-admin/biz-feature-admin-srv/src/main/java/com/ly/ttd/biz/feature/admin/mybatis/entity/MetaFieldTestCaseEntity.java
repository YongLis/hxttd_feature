package com.ly.ttd.biz.feature.admin.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元字段测试用例实体
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ttd_meta_field_test_case")
public class MetaFieldTestCaseEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("meta_field_code")
    private String metaFieldCode;

    @TableField("case_type")
    private String caseType;

    private String bizOrderNo;

    @TableField("case_content")
    private String caseContent;

    @TableField("target_value")
    private String targetValue;
}
