-- ===================================================
-- Kafka Topic 管理表
-- 用于管理接入点脚本策略动态分发的目标 Topic
-- ===================================================
CREATE TABLE `ttd_kafka_topic`
(
    `id`                 VARCHAR(64)  NOT NULL COMMENT '主键ID',
    `name`               VARCHAR(128) NOT NULL COMMENT 'Topic名称（唯一）',
    `partitions`         INT          NOT NULL DEFAULT 1 COMMENT '分区数（≥1）',
    `replicas`           INT          NOT NULL DEFAULT 1 COMMENT '副本数（≥1）',
    `consumer_group`     VARCHAR(128)          DEFAULT NULL COMMENT '消费者组',
    `offset_strategy`    VARCHAR(16)  NOT NULL DEFAULT 'latest' COMMENT '消费偏移策略：latest/earliest/none',
    `data_format`        VARCHAR(16)  NOT NULL DEFAULT 'JSON' COMMENT '数据格式：JSON/Avro/Protobuf/Custom',
    `status`             VARCHAR(16)  NOT NULL DEFAULT 'init' COMMENT '状态：init(待创建)/created(已创建)/active(运行中)',
    `serializer`         VARCHAR(128)          DEFAULT NULL COMMENT 'Key序列化类全限定名',
    `value_deserializer` VARCHAR(128)          DEFAULT NULL COMMENT 'Value反序列化类全限定名',
    `extra_config`       JSON                  DEFAULT NULL COMMENT '扩展配置（K-V键值对）',
    `remark`             VARCHAR(512)          DEFAULT NULL COMMENT '备注',
    `audit_reason`       VARCHAR(512)          DEFAULT NULL COMMENT '审核/驳回原因',
    `created_at`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Kafka Topic 管理表';
