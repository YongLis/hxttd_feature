package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class EventMessageDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "事件ID")
    private String eventId;
    @Schema(description = "事件类型")
    private String eventType;
    @Schema(description = "操作类型(DEPLOY/UPDATE/OFFLINE)")
    private String operationType;
    @Schema(description = "事件体数据(JSON)")
    private String body;
    @Schema(description = "处理状态(PENDING/PROCESSING/SUCCESS/FAILED)")
    private String status;
    @Schema(description = "重试次数")
    private Integer retryCount;
    @Schema(description = "错误信息")
    private String errorMessage;
    @Schema(description = "创建时间")
    private Date crtTime;
    @Schema(description = "更新时间")
    private Date uptTime;
    @Schema(description = "创建人")
    private String crtUser;
    @Schema(description = "修改人")
    private String uptUser;
}
