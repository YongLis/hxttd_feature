package com.ly.ttd.biz.admin.srv.project.req;

import lombok.Data;

/**
 * 项目添加请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class ProjectUserAddReq {
    private Long projectId;
    private String userAccount;
}
