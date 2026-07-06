package com.ly.ttd.biz.admin.srv.project.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 项目更新请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class ProjectUpdateReq {

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    @Schema(description = "项目ID", example = "1")
    private Long id;

    /**
     * 项目名称
     */
    @Schema(description = "项目名称", example = "特征平台")
    private String name;
}
