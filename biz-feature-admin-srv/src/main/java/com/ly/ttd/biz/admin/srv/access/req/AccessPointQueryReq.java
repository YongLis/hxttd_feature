package com.ly.ttd.biz.admin.srv.access.req;

import com.ly.ttd.biz.admin.req.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 接入点查询请求
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class AccessPointQueryReq extends PageQuery {

    @Schema(description = "接入点编码", example = "AP_USER_INFO")
    private String code;

    @Schema(description = "接入点名称(模糊查询)", example = "用户信息")
    private String name;

    @Schema(description = "操作类型")
    private String operationType;

    @Schema(description = "状态: ENABLED/DISABLED", example = "ENABLED")
    private String status;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private String sorterField;

    /**
     * 排序方向(ascend/descend)
     */
    @Schema(description = "排序方向: ascend/descend", example = "ascend")
    private String sorterOrder;
}
