package com.ly.ttd.biz.admin.srv.access.req;

import com.ly.ttd.biz.admin.req.BaseRequest;
import com.ly.ttd.biz.admin.srv.access.res.ParamItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 接入点添加请求
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class AccessPointAddReq extends BaseRequest {
    //    private String code;
    @NotBlank(message = "接入点名称不能为空")
    @Size(max = 256, message = "名称不能超过256个字符")
    @Schema(description = "接入点名称", example = "用户信息查询")
    private String name;

    @Size(max = 1024, message = "描述不能超过1024个字符")
    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态: ENABLED/DISABLED", example = "ENABLED")
    private String status;

    @Schema(description = "操作类型")
    private String operationType;

    @Schema(description = "查询模式")
    private String queryMode;

    /**
     * 请求入参列表
     */
    @Schema(description = "请求入参列表")
    private List<ParamItem> params;

}
