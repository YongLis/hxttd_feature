package com.ly.ttd.biz.admin.srv.metaField.req;

import com.ly.ttd.biz.admin.req.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 元字段查询请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class MetaFieldQueryReq extends PageQuery {

    /**
     * 资源名称（模糊查询）
     */
    @Schema(description = "资源名称(模糊查询)", example = "用户")
    private String resourceName;

    /**
     * 资源唯一标识键（精确查询）
     */
    @Schema(description = "资源唯一标识键(精确查询)", example = "META_USER_ID")
    private String resourceKey;
    /**
     * 分类标签
     */
    @Schema(description = "分类标签")
    private String categoryTag;
}
