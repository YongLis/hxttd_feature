package com.ly.ttd.biz.admin.srv.access.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 接入点文档响应
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class AccessPointDocRes {

    @Schema(description = "接口URL")
    private String apiUrl;

    @Schema(description = "接入点编码")
    private String code;

    @Schema(description = "接入点名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "是否删除")
    private Boolean deleted;

    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "创建时间")
    private Date crtTime;

    /**
     * 请求入参列表
     */
    @Schema(description = "请求入参列表")
    private List<ParamItem> reqParam;

    @Schema(description = "响应出参列表")
    private List<ParamItem> resParam;

}
