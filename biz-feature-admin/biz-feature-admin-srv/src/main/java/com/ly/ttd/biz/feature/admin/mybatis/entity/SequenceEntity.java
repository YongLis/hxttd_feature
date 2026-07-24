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
@TableName("ttd_sequence")
public class SequenceEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 序列编码
     */
    private String seqCode;

    /**
     * 序列名称
     */
    private String seqName;

    /**
     * 序列值
     */
    private Integer val;
}
