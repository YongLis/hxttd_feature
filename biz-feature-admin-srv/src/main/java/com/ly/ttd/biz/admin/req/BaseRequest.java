package com.ly.ttd.biz.admin.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author yong.li
 * @since 2026/4/13 10:45
 */
@Data
public class BaseRequest {
    @Schema(description = "项目ID", example = "1")
    private Long projectId;

    @Schema(description = "会话ID")
    private String sessionId;
}
