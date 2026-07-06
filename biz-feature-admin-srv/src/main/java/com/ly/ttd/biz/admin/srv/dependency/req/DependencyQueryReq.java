package com.ly.ttd.biz.admin.srv.dependency.req;

import com.ly.ttd.biz.admin.req.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 查询血缘
 *
 * @author yong.li
 * @since 2026/7/1 14:53
 */
@Data
public class DependencyQueryReq extends BaseRequest {

    @NotEmpty(message = "资源标识键不能为空")
    @Schema(description = "资源标识键")
    private String resourceKey;

    @NotEmpty(message = "资源类型不能为空")
    @Schema(description = "资源类型")
    private String resourceType;

}
