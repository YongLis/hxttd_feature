package com.ly.ttd.biz.admin.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author yong.li
 * @since 2026/6/2 15:44
 */
@Data
public class ResourceReq extends BaseRequest {

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

}
