package com.ly.ttd.biz.feature.dem.sweb.service.access.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author yong.li
 * @since 2026/5/27 10:47
 */
@Data
public class ParamItem {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "参数名称")
    private String paramName;

    @Schema(description = "参数编码")
    private String paramCode;

    @Schema(description = "参数类型")
    private String paramType;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "参数层级")
    private int paramLevel;

    @Schema(description = "是否必填")
    private Integer required;

    @Schema(description = "默认值")
    private String defaultValue;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "排序号")
    private Integer sortOrder;

    @Schema(description = "父参数编码")
    private String parentParamCode;

    @Schema(description = "子参数列表")
    private List<ParamItem> children;


    public ParamItem() {
    }

    public ParamItem(String paramName, String paramCode, String paramType, int paramLevel, Integer required, String defaultValue, String description, Integer sortOrder, String parentParamCode) {
        this.paramName = paramName;
        this.paramCode = paramCode;
        this.paramType = paramType;
        this.paramLevel = paramLevel;
        this.required = required;
        this.defaultValue = defaultValue;
        this.description = description;
        this.sortOrder = sortOrder;
        this.parentParamCode = parentParamCode;
    }
}
