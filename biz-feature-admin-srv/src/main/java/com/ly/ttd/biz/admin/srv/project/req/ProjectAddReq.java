package com.ly.ttd.biz.admin.srv.project.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 项目添加请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class ProjectAddReq {
    /**
     * 项目代码
     */
    @NotBlank(message = "项目代码不能为空")
    @Size(max = 8, message = "项目代码不能超过8个字符")
    @Schema(description = "项目代码(不超过8个字符)", example = "FP")
    private String projectCode;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 256, message = "项目名称不能超过256个字符")
    @Schema(description = "项目名称", example = "特征平台")
    private String name;
}
