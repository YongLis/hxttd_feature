package com.ly.ttd.biz.admin.srv.audit.res;

import com.ly.ttd.biz.admin.mybatis.entity.AuditEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * 审核列表查询响应
 *
 * @author yong.li
 * @since 2026-05-30
 */
@Data
public class AuditQueryRes {

    @Schema(description = "主键ID")
    private Long id;

    /**
     * 关联特征ID
     */
    @Schema(description = "关联特征类型")
    private String resourceType;

    /**
     * 关联特征ID
     */
    @Schema(description = "关联特征资源key")
    private String resourceKey;

    @Schema(description = "资源名称")
    private String resourceName;

    /**
     * 审核状态
     */
    @Schema(description = "审核状态")
    private String auditStatus;

    /**
     * 操作类型：ADD/UPDATE/DELETE
     */
    @Schema(description = "操作类型：ADD/UPDATE/DELETE")
    private String operationType;

    /**
     * 审核意见
     */
    @Schema(description = "审核意见")
    private String auditComment;

    /**
     * 提交人
     */
    @Schema(description = "提交人")
    private String submitUser;

    /**
     * 审核人
     */
    @Schema(description = "审核人")
    private String auditUser;

    /**
     * 提交时间
     */
    @Schema(description = "提交时间")
    private Date submitTime;

    /**
     * 审核时间
     */
    @Schema(description = "审核时间")
    private Date auditTime;

    /**
     * Entity → DTO 转换
     */
    public static AuditQueryRes fromEntity(AuditEntity entity) {
        AuditQueryRes res = new AuditQueryRes();
        BeanUtils.copyProperties(entity, res);
        return res;
    }
}
