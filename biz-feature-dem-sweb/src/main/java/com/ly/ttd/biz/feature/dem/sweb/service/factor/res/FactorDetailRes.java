package com.ly.ttd.biz.feature.dem.sweb.service.factor.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 衍生指标详情
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class FactorDetailRes {

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

    @Schema(description = "指标类型")
    private String factorType;

    @Schema(description = "返回值类型")
    private String returnType;

    @Schema(description = "默认值")
    private String defaultValue;

    @Schema(description = "异常值")
    private String exceptionValue;

    @Schema(description = "超时时间（毫秒）")
    private Long timeout;

    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "创建时间")
    private Date crtTime;

}
