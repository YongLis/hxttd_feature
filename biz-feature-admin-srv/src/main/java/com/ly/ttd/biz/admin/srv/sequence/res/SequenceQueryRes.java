package com.ly.ttd.biz.admin.srv.sequence.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 序列查询响应
 *
 * @author yong.li
 * @since 2026-06-23
 */
@Data
public class SequenceQueryRes {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "序列编码")
    private String seqCode;

    @Schema(description = "序列名称")
    private String seqName;

    @Schema(description = "当前值")
    private Long val;
}
