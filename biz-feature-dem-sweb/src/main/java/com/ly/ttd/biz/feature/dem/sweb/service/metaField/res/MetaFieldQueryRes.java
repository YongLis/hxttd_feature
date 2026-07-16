package com.ly.ttd.biz.feature.dem.sweb.service.metaField.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 元字段查询响应
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class MetaFieldQueryRes {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "资源唯一标识键")
    private String resourceKey;

    @Schema(description = "资源名称")
    private String resourceName;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "所属项目ID")
    private Long projectId;

    @Schema(description = "脚本语言")
    private String language;

    @Schema(description = "脚本内容")
    private String script;

    @Schema(description = "返回值类型")
    private String returnType;

    @Schema(description = "返回值类型描述")
    private String returnTypeDesc;

    @Schema(description = "默认值")
    private String defaultValue;

    @Schema(description = "异常值")
    private String exceptionValue;

    @Schema(description = "分类标签")
    private String categoryTag;

    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "创建时间")
    private Date crtTime;
}
