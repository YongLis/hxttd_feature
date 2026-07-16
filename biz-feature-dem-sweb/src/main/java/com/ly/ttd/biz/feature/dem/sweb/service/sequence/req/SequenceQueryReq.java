package com.ly.ttd.biz.feature.dem.sweb.service.sequence.req;


import com.ly.ttd.base.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 序列查询请求
 *
 * @author yong.li
 * @since 2026-06-23
 */
@Data
public class SequenceQueryReq extends PageQuery {

    @Schema(description = "序列编码(模糊查询)", example = "SEQ_ORDER_NO")
    private String seqCode;

    @Schema(description = "序列名称(模糊查询)", example = "订单号序列")
    private String seqName;
}
