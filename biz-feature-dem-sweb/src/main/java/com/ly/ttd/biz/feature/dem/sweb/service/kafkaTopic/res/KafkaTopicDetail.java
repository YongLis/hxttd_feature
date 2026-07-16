package com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.res;

import com.ly.ttd.base.result.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Kafka Topic 更新请求
 *
 * @author yong.li
 * @since 2026/7/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KafkaTopicDetail extends BaseRequest {

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "Topic名称")
    private String name;

    @Schema(description = "分区数（≥1）")
    private Integer partitions;

    @Schema(description = "副本数（≥1）")
    private Integer replicas;

    @Schema(description = "消费者组")
    private String consumerGroup;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "审核/驳回原因")
    private String auditReason;

    private String crtUser;

    private String uptUser;

    private Date crtTime;

    private Date uptTime;

    private Boolean deleted;
}
