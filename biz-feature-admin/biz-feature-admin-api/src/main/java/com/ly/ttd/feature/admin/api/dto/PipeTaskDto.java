package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PipeTaskDto extends BaseDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "接入点")
    private String pointCode;
    @Schema(description = "任务编码")
    private String taskCode;
    @Schema(description = "任务描述")
    private String taskName;
    @Schema(description = "表名")
    private String tableName;
    @Schema(description = "绑定kafka Topic")
    private String kafkaTopic;
    @Schema(description = "任务状态(0-开启,1-未开启)")
    private String taskStatus;
    @Schema(description = "优先级(1-100， 1优先级最高)")
    private String taskPriority;
}
