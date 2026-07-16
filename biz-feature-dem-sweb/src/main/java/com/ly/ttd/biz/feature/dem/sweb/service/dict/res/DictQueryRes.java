package com.ly.ttd.biz.feature.dem.sweb.service.dict.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 字典查询响应
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class DictQueryRes {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "系统编码")
    private String systemCode;

    @Schema(description = "字典编码")
    private String dictCode;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "创建时间")
    private Date crtTime;
}
