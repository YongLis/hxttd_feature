package com.ly.ttd.biz.feature.dem.sweb.service.access.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 接入点查询响应
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class AccessPointQueryRes {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "接入点编码")
    private String code;

    @Schema(description = "接入点名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "所属项目ID")
    private Long projectId;

    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "创建时间")
    private Date crtTime;

    @Schema(description = "是否删除")
    private Boolean deleted;

    /**
     * 请求入参列表
     */
    @Schema(description = "请求入参列表")
    private List<ParamItem> params;
}
