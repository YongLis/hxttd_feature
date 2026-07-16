package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 事件消息实体
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ttd_event_message")
public class EventMessageEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 操作类型(DEPLOY/UPDATE/OFFLINE)
     */
    private String operationType;

    /**
     * 事件体数据(JSON)
     */
    private String body;

    /**
     * 处理状态(PENDING/PROCESSING/SUCCESS/FAILED)
     */
    private String status;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 创建时间
     */
    private java.util.Date crtTime;

    /**
     * 更新时间
     */
    private java.util.Date uptTime;

    /**
     * 创建人
     */
    private String crtUser;

    /**
     * 修改人
     */
    private String uptUser;
}
