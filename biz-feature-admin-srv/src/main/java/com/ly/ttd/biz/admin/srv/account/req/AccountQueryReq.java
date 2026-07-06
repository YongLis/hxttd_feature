package com.ly.ttd.biz.admin.srv.account.req;

import com.ly.ttd.biz.admin.req.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 账户查询请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class AccountQueryReq extends PageQuery {

    /**
     * 账户名称
     */
    @Schema(description = "账户名称(模糊查询)", example = "admin")
    private String userAccount;
}
