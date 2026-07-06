package com.ly.ttd.biz.admin.srv.feature.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 特征配置查询响应
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class FeatureConfigListRes {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "资源唯一标识键")
    private String resourceKey;

    @Schema(description = "资源名称")
    private String resourceName;

    @Schema(description = "特征编码")
    private String featureCode;

    @Schema(description = "返回值类型")
    private String returnType;

    @Schema(description = "默认值")
    private String defaultValue;
    @Schema(description = "异常值")
    private String exceptionValue;
    @Schema(description = "超时时间")
    private Long timeout;


}
