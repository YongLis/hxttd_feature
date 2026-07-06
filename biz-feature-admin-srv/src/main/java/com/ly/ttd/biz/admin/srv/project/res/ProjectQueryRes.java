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
public class ProjectQueryRes {

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 项目代码
     */
    @Schema(description = "项目代码")
    private String projectCode;

    /**
     * 项目名称
     */
    @Schema(description = "项目名称")
    private String name;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String crtUser;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date crtTime;

    /**
     * 用户数量
     */
    @Schema(description = "成员数量")
    private Integer userCount;
}
