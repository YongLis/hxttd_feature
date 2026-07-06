package com.ly.ttd.biz.admin.srv.factor.res;

import com.ly.ttd.feature.common.model.factor.ConnectorParamField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 衍生指标详情
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class DerivativeFactorDetailRes extends FactorDetailRes {

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
    @Schema(description = "关联指标编码")
    private List<ConnectorParamField> params;
    /**
     * 脚本语言(aviator/groovy)
     */
    @Schema(description = "脚本语言(aviator/groovy)")
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
