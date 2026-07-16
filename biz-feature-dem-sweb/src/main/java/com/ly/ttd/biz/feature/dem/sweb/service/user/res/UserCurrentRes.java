package com.ly.ttd.biz.feature.dem.sweb.service.user.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 当前登录用户信息响应（含租户和项目）
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class UserCurrentRes {

    /**
     * 用户账户
     */
    @Schema(description = "用户账户")
    private String userAccount;

    /**
     * 角色
     */
    @Schema(description = "角色")
    private String role;

    /**
     * 所属项目ID列表（允许用户拥有多个项目）
     */
    @Schema(description = "所属项目ID列表（允许用户拥有多个项目）")
    private List<Long> projectIds;

    /**
     * 选中项目ID（默认取第一个项目ID）
     */
    @Schema(description = "选中项目ID（默认取第一个项目ID）")
    private Long selectedProjectId;
}
