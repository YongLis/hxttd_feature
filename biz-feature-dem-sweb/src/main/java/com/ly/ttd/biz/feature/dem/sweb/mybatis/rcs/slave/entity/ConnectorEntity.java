package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连接器实体 (表: ttd_connector)
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ttd_connector")
public class ConnectorEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String resourceKey;

    private String resourceName;

    private String version;

    private Long projectId;

    private String connectorType;

    private String defaultValue;

    private String exceptionValue;

    private Long timeout;

    private String resourceJson;
}
