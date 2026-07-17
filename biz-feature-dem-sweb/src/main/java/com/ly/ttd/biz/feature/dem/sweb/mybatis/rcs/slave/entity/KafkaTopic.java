package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Kafka Topic 管理表
 *
 * @TableName ttd_kafka_topic
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@TableName("ttd_kafka_topic")
public class KafkaTopic extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * Topic名称（唯一）
     */
    private String name;

    /**
     * 分区数（≥1）
     */
    private Integer partitions;

    /**
     * 副本数（≥1）
     */
    private Integer replicas;

    /**
     * 消费者组
     */
    @TableField("consumer_group")
    private String consumerGroup;

    @TableField("topic_status")
    private String topicStatus;


    /**
     * 备注
     */
    private String remark;

    /**
     * 审核/驳回原因
     */
    @TableField("audit_reason")
    private String auditReason;
}