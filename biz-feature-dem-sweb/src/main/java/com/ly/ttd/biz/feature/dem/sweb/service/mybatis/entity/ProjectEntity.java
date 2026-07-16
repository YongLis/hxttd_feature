package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目实体
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ttd_project")
public class ProjectEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 项目代码
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String name;
}
