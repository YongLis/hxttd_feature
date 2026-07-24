package com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.req;

import com.ly.ttd.base.result.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Kafka Topic 添加请求
 *
 * @author yong.li
 * @since 2026/7/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KafkaTopicAddReq extends BaseRequest {

    @NotBlank(message = "Topic名称不能为空")
    @Schema(description = "Topic名称（唯一）")
    private String name;

    @NotNull(message = "分区数不能为空")
    @Min(value = 1, message = "分区数不能小于1")
    @Schema(description = "分区数（≥1）")
    private Integer partitions;

    @NotNull(message = "副本数不能为空")
    @Min(value = 1, message = "副本数不能小于1")
    @Schema(description = "副本数（≥1）")
    private Integer replicas;

    @Schema(description = "消费者组")
    private String consumerGroup;

    @Schema(description = "状态：INIT(待创建)/AUDIT(审核中)/AUDIT_PASS/AUDIT_REJECT")
    private String topicStatus;

    @Schema(description = "备注")
    private String remark;
}
