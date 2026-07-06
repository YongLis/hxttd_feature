package com.ly.ttd.feature.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目用户实体
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class ProjectUserModel {

    private Long id;
    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 用户账户
     */
    private String userAccount;

    /**
     * 用户名称
     */
    private String userName;
}
