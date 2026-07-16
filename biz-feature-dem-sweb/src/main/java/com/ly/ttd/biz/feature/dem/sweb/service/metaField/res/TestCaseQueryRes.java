package com.ly.ttd.biz.feature.dem.sweb.service.metaField.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 测试用例查询响应
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class TestCaseQueryRes {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "元字段编码")
    private String metaFieldCode;

    @Schema(description = "用例类型")
    private String caseType;

    @Schema(description = "业务订单号")
    private String bizOrderNo;

    @Schema(description = "用例内容")
    private String caseContent;

    @Schema(description = "目标值")
    private String targetValue;

    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "创建时间")
    private Date crtTime;
}
