package com.ly.ttd.biz.admin.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author yong.li
 * @since 2026/4/13 13:50
 */
@Data
public class PageQuery extends BaseRequest {

    @Schema(description = "每页条数", example = "10")
    private long pageSize;

    @Schema(description = "当前页码", example = "1")
    private long current;

}
