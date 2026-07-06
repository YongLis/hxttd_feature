package com.ly.ttd.biz.admin.srv.dict.req;

import com.ly.ttd.biz.admin.req.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典键值查询请求
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class DictCodeQueryReq extends PageQuery {

    @Schema(description = "字典ID", example = "1")
    private Long dictId;

    @Schema(description = "字典键", example = "MALE")
    private String dictKey;

    @Schema(description = "是否删除", example = "true")
    private String deleted;
}
