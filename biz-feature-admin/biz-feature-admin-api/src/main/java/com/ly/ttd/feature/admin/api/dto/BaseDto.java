package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * DTO基类
 *
 * @author yong.li
 * @since 2026-06-23
 */
@Data
public class BaseDto {

    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "更新人")
    private String uptUser;

    @Schema(description = "创建时间")
    private Date crtTime;

    @Schema(description = "更新时间")
    private Date uptTime;

    @Schema(description = "是否删除")
    private Boolean deleted;
}
