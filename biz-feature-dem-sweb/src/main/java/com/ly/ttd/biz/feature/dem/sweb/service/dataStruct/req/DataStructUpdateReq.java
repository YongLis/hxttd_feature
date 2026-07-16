package com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.req;

import com.ly.ttd.base.result.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 数据集更新请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class DataStructUpdateReq extends BaseRequest {

    @NotNull(message = "ID不能为空")
    @Schema(description = "数据集ID", example = "1")
    private Long id;

    @Schema(description = "资源名称", example = "用户信息数据集")
    private String resourceName;
}