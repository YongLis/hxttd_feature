package com.ly.ttd.feature.sample.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
