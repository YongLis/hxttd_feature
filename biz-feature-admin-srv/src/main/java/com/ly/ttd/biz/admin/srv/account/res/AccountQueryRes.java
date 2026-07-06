package com.ly.ttd.biz.admin.srv.account.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 账户查询响应
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class AccountQueryRes {

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 用户账户
     */
    @Schema(description = "用户账户")
    private String userAccount;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String crtUser;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date crtTime;
}
