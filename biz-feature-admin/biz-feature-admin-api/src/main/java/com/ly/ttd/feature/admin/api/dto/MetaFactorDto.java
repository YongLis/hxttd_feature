package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 指标新增请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class MetaFactorDto extends BaseDto {

    private Long id;

    private Long projectId;

    @Schema(description = "资源标识键", example = "USER_INFO")
    private String resourceKey;

    @Schema(description = "资源名称", example = "用户信息")
    private String resourceName;

    @Schema(description = "版本号")
    private String version;


    // 默认值
    @Schema(description = "默认值")
    private String defaultValue;
    // 异常值
    @Schema(description = "异常值")
    private String exceptionValue;
    // 超时时间
    @Schema(description = "超时时间(毫秒)", example = "3000")
    private Long timeout;


    @NotBlank(message = "指标类型不能为空")
    @Schema(description = "指标类型: META/DERIVATIVE/FEATURE", example = "FEATURE")
    private String factorType;

    @NotBlank(message = "返回值类型不能为空")
    @Schema(description = "返回值类型: STRING/LONG/DOUBLE/DECIMAL/BOOLEAN/LIST/DATE/DICT", example = "LONG")
    private String returnType;

    @NotBlank(message = "元字段编码不能为空")
    @Schema(description = "元字段编码")
    private String metaFieldCode;


}
