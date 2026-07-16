package com.ly.ttd.biz.feature.admin.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据管道任务
 *
 * @TableName ttd_pipe_task
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ttd_pipe_task")
public class PipeTask extends BaseEntity {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 接入点
     */
    @TableField("point_code")
    private String pointCode;

    /**
     * 任务编码
     */
    @TableField("task_code")
    private String taskCode;

    /**
     * 任务描述
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 表名
     */
    @TableField("table_name")
    private String tableName;

    /**
     * 绑定kafka Topic
     */
    @TableField("kafka_topic")
    private String kafkaTopic;

    /**
     * 任务状态(0-开启,1-未开启)
     */
    @TableField("task_status")
    private String taskStatus;
}
