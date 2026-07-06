package com.ly.ttd.biz.admin.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目用户实体
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ttd_project_user")
public class ProjectUserEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 用户账户
     */
    private String userAccount;

}
