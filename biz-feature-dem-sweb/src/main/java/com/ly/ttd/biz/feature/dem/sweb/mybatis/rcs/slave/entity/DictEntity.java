package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典配置
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ttd_dict")
public class DictEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统编码
     */
    private String systemCode;

    /**
     * 字典编码
     */
    private String dictCode;
    /**
     * 字典名称
     */
    private String dictName;
}
