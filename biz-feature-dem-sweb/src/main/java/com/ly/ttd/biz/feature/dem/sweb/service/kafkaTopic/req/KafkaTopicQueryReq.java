package com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.req;


import com.ly.ttd.base.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Kafka Topic 分页查询请求
 *
 * @author yong.li
 * @since 2026/7/8
 */
@Data
public class KafkaTopicQueryReq extends PageQuery {

    @Schema(description = "Topic名称（模糊查询）")
    private String name;

    @Schema(description = "状态：INIT/AUDIT/AUDIT_PASS/AUDIT_REJECT")
    private String topicStatus;

    @Schema(description = "是否删除")
    private String deleted;
}
