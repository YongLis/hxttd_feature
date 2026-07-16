package com.ly.ttd.biz.feature.admin.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字段实体
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ttd_data_field")
public class DataFieldEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 数据集键
     */
    private String dataStructCode;

    /**
     * 字段编码
     */
    private String fieldCode;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 指标编码
     */
    private String factorCode;

    /**
     * 字段类型(NUMBER/STRING/BOOLEAN)
     */
    private String objectType;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 排序顺序
     */
    private Integer sortOrder;
}
