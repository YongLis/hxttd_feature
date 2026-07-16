package com.ly.ttd.biz.feature.dem.sweb.service.eventMessage.req;


import com.ly.ttd.base.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 特征溯源查询请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class FeatureTraceQueryReq extends PageQuery {

    @Schema(description = "事件ID", example = "EVT20260701001")
    private String eventId;

    @Schema(description = "状态: SUCCESS/FAILED/PROCESSING", example = "SUCCESS")
    private String status;

    @Schema(description = "操作类型")
    private String operationType;
}
