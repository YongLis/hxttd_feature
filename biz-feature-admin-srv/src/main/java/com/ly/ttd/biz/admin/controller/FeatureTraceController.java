package com.ly.ttd.biz.admin.controller;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.srv.eventMessage.FeatureTraceQueryService;
import com.ly.ttd.biz.admin.srv.eventMessage.req.FeatureTraceQueryReq;
import com.ly.ttd.biz.admin.srv.eventMessage.res.FeatureTraceQueryRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 特征溯源 Controller (只读查询)
 *
 * @author yong.li
 * @since 2026-05-16
 */
@RestController
@RequestMapping("/api/feature-trace")
@Tag(name = "特征溯源", description = "特征计算链路追踪与事件消息查询接口")
public class FeatureTraceController {

    @Resource
    private FeatureTraceQueryService featureTraceQueryService;

    @Operation(summary = "分页查询特征调用链路", description = "根据 txnId、特征编码等条件查询计算调用链")
    @PostMapping("/page")
    public PageResult<FeatureTraceQueryRes> page(@RequestBody FeatureTraceQueryReq req) {
        return featureTraceQueryService.pageQuery(req);
    }
}
