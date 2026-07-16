package com.ly.ttd.feature.admin.api.dto;

import com.ly.ttd.feature.common.model.factor.ConnectorParamField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 衍生指标
 *
 * @author yong.li
 * @since 2026/7/2 09:35
 */
@Data
public class DerivativeFactorDto extends FactorDto {

    @NotBlank(message = "指标类型不能为空")
    @Schema(description = "指标类型: META/DERIVATIVE/FEATURE", example = "DERIVATIVE")
    private String factorType;

    @NotBlank(message = "返回值类型不能为空")
    @Schema(description = "返回值类型: STRING/LONG/DOUBLE/DECIMAL/BOOLEAN/LIST/DATE/DICT", example = "LONG")
    private String returnType;

    /**
     * 关联指标编码
     */
    @Schema(description = "关联指标编码")
    private List<String> factorCodes;

    /**
     * 连接类型
     */
    @Schema(description = "连接类型")
    private String connectorType;

    /**
     * 连接编码
     */
    @Schema(description = "连接编码")
    private String connectorCode;

    /**
     * 连接参数
     */
    @Schema(description = "连接参数")
    private List<ConnectorParamField> params;
    /**
     * 脚本语言(aviator/groovy)
     */
    @Schema(description = "脚本语言")
    private String language;

    /**
     * 前置脚本
     */
    @Schema(description = "前置脚本")
    private String conditionScript;


    /**
     * 计算脚本
     */
    @Schema(description = "计算脚本")
    private String script;
}
