package com.ly.ttd.biz.admin.srv.project.req;

import com.ly.ttd.biz.admin.req.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 项目查询请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class ProjectUserQueryReq extends PageQuery {

    @Schema(description = "项目ID", example = "1")
    private Long projectId;
}
