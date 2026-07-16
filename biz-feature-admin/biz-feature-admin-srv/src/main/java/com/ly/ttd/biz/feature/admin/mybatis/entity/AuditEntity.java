package com.ly.ttd.biz.feature.admin.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 审核记录实体 (表: ttd_audit)
 *
 * @author yong.li
 * @since 2026-05-30
 */
@Data
@TableName("ttd_audit")
public class AuditEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String resourceType;

    private String resourceKey;

    private String resourceName;

    private String auditStatus;

    private String operationType;

    private String beforeContent;

    private String afterContent;

    private String auditComment;

    private String submitUser;

    private String auditUser;

    private Date submitTime;

    private Date auditTime;

}
