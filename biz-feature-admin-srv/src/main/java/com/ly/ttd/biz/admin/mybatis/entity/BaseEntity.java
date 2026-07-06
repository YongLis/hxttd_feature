package com.ly.ttd.biz.admin.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 基础实体类
 *
 * @author yong.li
 * @since 2026/4/1 14:08
 */
@Getter
@Setter
public class BaseEntity {

    @TableField("crt_user")
    private String crtUser;

    @TableField("upt_user")
    private String uptUser;

    @TableField("crt_time")
    private Date crtTime;

    @TableField("upt_time")
    private Date uptTime;

    private Boolean deleted;
}
