package com.ly.ttd.biz.admin.srv.eventMessage.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 特征溯源查询响应
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class FeatureTraceQueryRes {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "事件ID")
    private String eventId;

    @Schema(description = "事件类型")
    private String eventType;

    @Schema(description = "操作类型")
    private String operationType;

    @Schema(description = "事件体")
    private String body;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "重试次数")
    private Integer retryCount;

    @Schema(description = "错误信息")
    private String errorMessage;

    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "创建时间")
    private Date crtTime;
}
