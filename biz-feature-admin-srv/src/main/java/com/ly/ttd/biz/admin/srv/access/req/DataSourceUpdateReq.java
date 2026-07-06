package com.ly.ttd.biz.admin.srv.access.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 数据源更新请求
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class DataSourceUpdateReq {

    @NotNull(message = "ID不能为空")
    @Schema(description = "数据源ID", example = "1")
    private Long id;

    @Schema(description = "数据源类别: JDBC/REDIS/ES/HTTP", example = "JDBC")
    private String sourceCategory;

    @Schema(description = "数据源编码", example = "DS_USER_DB")
    private String sourceCode;

    @Schema(description = "数据源名称", example = "用户数据库")
    private String sourceName;

    @Schema(description = "关联接入点ID", example = "1")
    private Long accessPointId;

    @Schema(description = "连接信息(JSON配置)")
    private String connectionInfo;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "排序序号", example = "1")
    private Integer sortOrder;
}
