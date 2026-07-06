package com.ly.ttd.biz.admin.srv.project.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 项目查询响应
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class ProjectUserQueryRes {
    @Schema(description = "主键ID")
    private Long id;
    /**
     * 项目ID
     */
    @Schema(description = "项目ID")
    private Long projectId;
    /**
     * 用户账户
     */
    @Schema(description = "用户账户")
    private String userAccount;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String crtUser;
    @Schema(description = "更新人")
    private String uptUser;
    @Schema(description = "创建时间")
    private Date crtTime;
    @Schema(description = "更新时间")
    private Date uptTime;
    @Schema(description = "是否删除")
    private Boolean deleted;
}
