package com.ly.ttd.biz.admin.srv.metaField.req;

import com.ly.ttd.biz.admin.req.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 测试用例查询请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class TestCaseQueryReq extends PageQuery {

    @Schema(description = "元字段编码", example = "META_USER_ID")
    private String metaFieldCode;

    @Schema(description = "案例类型", example = "NORMAL")
    private String caseType;

    @Schema(description = "交易号", example = "BIZ20260701001")
    private String bizOrderNo;
}
