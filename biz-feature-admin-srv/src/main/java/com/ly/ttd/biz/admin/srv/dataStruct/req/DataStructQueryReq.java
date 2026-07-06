package com.ly.ttd.biz.admin.srv.dataStruct.req;

import com.ly.ttd.biz.admin.req.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据集查询请求
 *
 * @author yong.li
 * @since 2026-06-23
 */
@Data
public class DataStructQueryReq extends PageQuery {

    @Schema(description = "资源标识键(模糊查询)", example = "DS_USER_INFO")
    private String resourceKey;

    @Schema(description = "资源名称(模糊查询)", example = "用户信息数据集")
    private String resourceName;
}
