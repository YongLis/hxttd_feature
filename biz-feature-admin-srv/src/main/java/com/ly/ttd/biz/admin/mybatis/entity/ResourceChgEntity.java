package com.ly.ttd.biz.admin.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 资源版本历史实体
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
@TableName("ttd_resource_chg")
public class ResourceChgEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 资源唯一标识键
     */
    private String resourceKey;

    /**
     * 资源类型
     */
    private String resourceType;

    /**
     * 操作类型：ADD/UPDATE/ROLLBACK/DELETE
     */
    private String operationType;

    /**
     * 变更前版本号
     */
    private String beforeVersion;

    /**
     * 变更后版本号
     */
    private String afterVersion;

    /**
     * 变更前资源内容
     */
    private String beforeContent;

    /**
     * 变更后资源内容
     */
    private String afterContent;

    /**
     * 审核状态
     */
    private String auditStatus;

    /**
     * 创建时间
     */
    private java.util.Date crtTime;

    /**
     * 创建人
     */
    private String crtUser;
}
