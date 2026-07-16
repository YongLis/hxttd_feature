package com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Kafka Topic 更新请求
 *
 * @author yong.li
 * @since 2026/7/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KafkaTopicUpdateReq extends KafkaTopicAddReq {

    @NotBlank(message = "ID不能为空")
    @Schema(description = "主键ID")
    private String id;
}
