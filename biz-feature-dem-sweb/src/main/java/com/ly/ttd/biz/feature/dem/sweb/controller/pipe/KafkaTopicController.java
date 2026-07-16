package com.ly.ttd.biz.feature.dem.sweb.controller.pipe;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.base.result.Result;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.KafkaTopicAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.KafkaTopicQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.req.KafkaTopicAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.req.KafkaTopicQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.req.KafkaTopicUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.kafkaTopic.res.KafkaTopicDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Kafka Topic 管理 Controller（已接入统一审核流程）
 *
 * @author yong.li
 * @since 2026/7/8
 */
@Slf4j
@RestController
@RequestMapping("/api/kafka-topic")
@Tag(name = "Kafka Topic管理", description = "Kafka Topic 的 CRUD 管理接口，支持创建、查询、更新和删除，已接入统一审核")
public class KafkaTopicController {

    @Resource
    private KafkaTopicQueryService kafkaTopicQueryService;
    @Resource
    private KafkaTopicAdminService kafkaTopicAdminService;

    @Operation(summary = "分页查询Topic", description = "根据条件分页查询Kafka Topic列表")
    @PostMapping("/page")
    public PageResult<KafkaTopicDetail> page(@RequestBody KafkaTopicQueryReq req) {
        return kafkaTopicQueryService.pageQuery(req);
    }

    @Operation(summary = "新增Topic", description = "提交审核，审批通过后正式创建")
    @PostMapping("/add")
    public Result<Boolean> add(@Valid @RequestBody KafkaTopicAddReq req) {
        try {
            kafkaTopicAdminService.addTopic(req);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("add kafka topic error", e);
            return Result.error("新增Topic失败");
        }
    }

    @Operation(summary = "更新Topic", description = "提交审核，审批通过后正式更新")
    @PostMapping("/update")
    public Result<Boolean> update(@Valid @RequestBody KafkaTopicUpdateReq req) {
        try {
            kafkaTopicAdminService.updateTopic(req);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("update kafka topic error", e);
            return Result.error("更新Topic失败");
        }
    }

    @Operation(summary = "删除Topic", description = "提交审核，审批通过后正式删除")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "Topic ID") @RequestParam Long id) {
        try {
            kafkaTopicAdminService.deleteTopic(id);
            return Result.success(true);
        } catch (BizException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("delete kafka topic error", e);
            return Result.error("删除Topic失败");
        }
    }

    @Operation(summary = "查询Topic详情")
    @PostMapping("/detail")
    public Result<KafkaTopicDetail> detail(@Parameter(description = "topic名称") @RequestParam String topicName) {

        KafkaTopicDetail topic = kafkaTopicQueryService.getByName(topicName);
        if (topic == null) {
            return Result.error("Topic不存在");
        }
        return Result.success(topic);
    }
}
