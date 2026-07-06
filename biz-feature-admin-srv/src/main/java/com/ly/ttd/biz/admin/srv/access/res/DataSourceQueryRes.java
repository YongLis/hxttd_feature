package com.ly.ttd.biz.admin.srv.access.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 数据源查询响应
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class DataSourceQueryRes {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "接入点ID")
    private Long accessPointId;

    @Schema(description = "数据源类别")
    private String sourceCategory;

    @Schema(description = "数据源码")
    private String sourceCode;

    @Schema(description = "数据源名称")
    private String sourceName;

    @Schema(description = "连接信息")
    private String connectionInfo;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "排序号")
    private Integer sortOrder;

    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "创建时间")
    private Date crtTime;
}
