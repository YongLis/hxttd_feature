package com.ly.ttd.biz.admin.srv.access.req;

import com.ly.ttd.biz.admin.req.BaseRequest;
import com.ly.ttd.biz.admin.srv.access.res.ParamItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 接入点更新请求
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class AccessPointUpdateReq extends BaseRequest {

    @NotNull(message = "ID不能为空")
    @Schema(description = "接入点ID", example = "1")
    private Long id;

    @Schema(description = "接入点名称", example = "用户信息查询")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "来源类型")
    private String sourceType;

    @Schema(description = "状态: ENABLED/DISABLED", example = "ENABLED")
    private String status;

    @Schema(description = "操作类型")
    private String operationType;

    @Schema(description = "查询模式")
    private String queryMode;

    @Schema(description = "请求入参列表")
    private List<ParamItem> params;
}
