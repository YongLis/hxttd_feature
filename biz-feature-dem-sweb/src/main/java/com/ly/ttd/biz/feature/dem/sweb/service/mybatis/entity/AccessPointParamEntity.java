package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接入点请求入参配置实体
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ttd_access_point_param")
public class AccessPointParamEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 接入点ID
     */
    private String accessPointCode;

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 版本
     */
    private String version;

    /**
     * 参数编码
     */
    private String paramCode;

    /**
     * 参数类型(STRING/NUMBER/BOOLEAN/DATE)
     */
    private String paramType;

    /**
     * 是否必填(0-否,1-是)
     */
    private Integer required;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序顺序
     */
    private Integer sortOrder;

    /**
     * 父参数编码
     */
    private String parentParamCode;

    private Integer paramLevel;

    private String remark;
}
