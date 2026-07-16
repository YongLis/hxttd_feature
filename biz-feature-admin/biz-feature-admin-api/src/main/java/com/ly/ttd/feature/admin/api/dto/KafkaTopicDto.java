package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class KafkaTopicDto extends BaseDto {
    @Schema(description = "主键ID")
    private String id;
    @Schema(description = "Topic名称(唯一)")
    private String name;
    @Schema(description = "分区数(≥1)")
    private Integer partitions;
    @Schema(description = "副本数(≥1)")
    private Integer replicas;
    @Schema(description = "消费者组")
    private String consumerGroup;
    @Schema(description = "状态")
    private String topicStatus;

    @Schema(description = "备注")
    private String remark;
    @Schema(description = "审核/驳回原因")
    private String auditReason;
}
