-- ================================================================================
-- TTD 统一特征平台 (hxttd_feature) — 数据库建表语句
-- 基于 Entity 类 (MyBatis-Plus) 精确生成
-- 数据库: MySQL 8.0+
-- 生成日期: 2026-07-06
-- 注意: 所有表名和字段名遵循 MyBatis-Plus 驼峰转下划线约定
-- ================================================================================

-- ========================================
-- 1. 项目表 (ttd_project)
-- 实体: ProjectEntity extends BaseEntity
-- ========================================
CREATE TABLE `ttd_project`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `project_code` VARCHAR(128) NOT NULL COMMENT '项目代码',
    `name`         VARCHAR(256) NOT NULL COMMENT '项目名称',
    `crt_user`     VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`     VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    `crt_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_project_code` (`project_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='项目表';

-- ========================================
-- 2. 用户账户表 (ttd_user_account)
-- 实体: UserAccountEntity extends BaseEntity
-- ========================================
CREATE TABLE `ttd_user_account`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_account` VARCHAR(256) NOT NULL COMMENT '账户',
    `password`     VARCHAR(256) NOT NULL COMMENT '密码',
    `crt_user`     VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`     VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    `crt_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_account` (`user_account`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='用户表';

-- ========================================
-- 3. 项目用户关联表 (ttd_project_user)
-- 实体: ProjectUserEntity extends BaseEntity
-- ========================================
CREATE TABLE `ttd_project_user`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `project_id`   BIGINT       NOT NULL COMMENT '项目ID',
    `user_account` VARCHAR(256) NOT NULL COMMENT '用户账户',
    `crt_user`     VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`     VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    `crt_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_project_user` (`project_id`, `user_account`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='项目用户关联表';

-- ========================================
-- 4. 元字段表 (ttd_meta_field)
-- 实体: MetaFieldEntity extends BaseEntity
-- ========================================
CREATE TABLE `ttd_meta_field`
(
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `resource_key`    VARCHAR(128) NOT NULL COMMENT '资源唯一标识键',
    `resource_name`   VARCHAR(256) NOT NULL COMMENT '资源名称',
    `version`         VARCHAR(32)  NOT NULL DEFAULT '1.0.0' COMMENT '资源版本号',
    `project_id`      BIGINT       NOT NULL COMMENT '所属项目ID',
    `language`        VARCHAR(32)  NOT NULL COMMENT '脚本语言(groovy/aviator)',
    `script`          TEXT         NOT NULL COMMENT '计算脚本',
    `return_type`     VARCHAR(32)  NOT NULL COMMENT '返回值类型(STRING/LONG/DOUBLE/BOOLEAN/INTEGER)',
    `default_value`   VARCHAR(512)          DEFAULT NULL COMMENT '默认值',
    `exception_value` VARCHAR(512)          DEFAULT NULL COMMENT '异常值',
    `category_tag`    VARCHAR(64)           DEFAULT 'public' COMMENT '分类标签(public或接入点编码)',
    `crt_user`        VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`        VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    `crt_time`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_resource_key` (`resource_key`, `deleted`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_version` (`version`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='元字段表';

-- ========================================
-- 5. 特征配置表 (ttd_feature_config)
-- 实体: FeatureConfigEntity extends BaseEntity
-- ========================================
CREATE TABLE `ttd_feature_config`
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `resource_key`     VARCHAR(128) NOT NULL COMMENT '资源唯一标识键',
    `resource_name`    VARCHAR(256) NOT NULL COMMENT '资源名称',
    `version`          VARCHAR(32)  NOT NULL DEFAULT '1.0.0' COMMENT '资源版本号',
    `project_id`       BIGINT       NOT NULL COMMENT '所属项目ID',
    `feature_code`     VARCHAR(128) NOT NULL COMMENT '特征编码',
    `default_value`    VARCHAR(512)          DEFAULT NULL COMMENT '默认值',
    `exception_value`  VARCHAR(512)          DEFAULT NULL COMMENT '异常值',
    `timeout`          BIGINT                DEFAULT 100 COMMENT '超时时间(毫秒)',
    `main_dimension`   VARCHAR(128) NOT NULL COMMENT '主维度',
    `slave_dimension`  VARCHAR(128) NOT NULL COMMENT '从维度',
    `language`         VARCHAR(32)  NOT NULL COMMENT '脚本语言',
    `condition_script` TEXT                  DEFAULT NULL COMMENT '条件脚本',
    `main_dim_script`  TEXT         NOT NULL COMMENT '主维度脚本',
    `slave_dim_script` TEXT                  DEFAULT NULL COMMENT '从维度脚本',
    `meta_fields`      TEXT                  DEFAULT NULL COMMENT '依赖元字段(JSON数组)',
    `return_type`      VARCHAR(32)  NOT NULL COMMENT '返回值类型',
    `value_type`       VARCHAR(32)  NOT NULL COMMENT '指标值类型(FIXED/DYNAMIC_VALUE)',
    `value_script`     TEXT                  DEFAULT NULL COMMENT '指标值脚本',
    `fix_value`        VARCHAR(512)          DEFAULT NULL COMMENT '固定值',
    `aggregate_mode`   VARCHAR(32)           DEFAULT NULL COMMENT '聚合函数(SUM/COUNT/AVG/MAX/MIN)',
    `time_mode`        VARCHAR(32)           DEFAULT NULL COMMENT '时间模式(TTL/cd/cm/cy)',
    `time_unit`        VARCHAR(32)           DEFAULT NULL COMMENT '时间单位(MINUTE/HOUR/DAY)',
    `time_window`      INT                   DEFAULT NULL COMMENT '时间窗口',
    `resource_json`    TEXT                  DEFAULT NULL COMMENT '资源JSON配置',
    `crt_user`         VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`         VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    `crt_time`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`          TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_resource_key` (`resource_key`, `deleted`),
    UNIQUE KEY `uk_feature_code` (`feature_code`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_main_dimension` (`main_dimension`),
    KEY `idx_slave_dimension` (`slave_dimension`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='特征配置表';

-- ========================================
-- 6. 指标表 (ttd_factor)
-- 实体: FactorEntity extends BaseEntity
-- ========================================
CREATE TABLE `ttd_factor`
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `resource_key`     VARCHAR(128) NOT NULL COMMENT '资源唯一标识键',
    `resource_name`    VARCHAR(256) NOT NULL COMMENT '资源名称',
    `version`          VARCHAR(32)  NOT NULL DEFAULT '1.0.0' COMMENT '资源版本号',
    `project_id`       BIGINT       NOT NULL COMMENT '所属项目ID',
    `factor_type`      VARCHAR(32)  NOT NULL COMMENT '指标类型(META/DERIVATIVE/FEATURE)',
    `return_type`      VARCHAR(32)  NOT NULL COMMENT '返回值类型',
    `default_value`    VARCHAR(512)          DEFAULT NULL COMMENT '默认值',
    `exception_value`  VARCHAR(512)          DEFAULT NULL COMMENT '异常值',
    `timeout`          BIGINT                DEFAULT NULL COMMENT '超时时间(毫秒)',
    `resource_json`    TEXT                  DEFAULT NULL COMMENT '资源内容JSON',
    `ref_feature_code` VARCHAR(128)          DEFAULT NULL COMMENT '关联特征编码(FeatureFactor类型)',
    `crt_user`         VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`         VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    `crt_time`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`          TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_resource_key` (`resource_key`, `deleted`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_factor_type` (`factor_type`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='指标表';

-- ========================================
-- 7. 指标血缘依赖表 (ttd_factor_dependency)
-- 实体: FactorDependencyEntity extends BaseEntity
-- ========================================
CREATE TABLE `ttd_factor_dependency`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `project_id`  BIGINT       NOT NULL COMMENT '项目ID',
    `parent`      VARCHAR(128) NOT NULL COMMENT '父指标编码(下游依赖方)',
    `parent_type` VARCHAR(128) NOT NULL COMMENT '父指标类型',
    `child`       VARCHAR(128) NOT NULL COMMENT '子指标编码(上游被依赖方)',
    `crt_user`    VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`    VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    `crt_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_parent_child` (`parent`, `parent_type`, `child`),
    KEY `idx_project_parent` (`project_id`, `parent`),
    KEY `idx_project_child` (`project_id`, `child`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='指标血缘依赖表';

-- ========================================
-- 8. 连接器表 (ttd_connector)
-- 实体: ConnectorEntity extends BaseEntity
-- ========================================
CREATE TABLE `ttd_connector`
(
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `resource_key`    VARCHAR(128) NOT NULL COMMENT '资源唯一标识键',
    `resource_name`   VARCHAR(256) NOT NULL COMMENT '资源名称',
    `version`         VARCHAR(32)  NOT NULL DEFAULT '1.0.0' COMMENT '资源版本号',
    `project_id`      BIGINT       NOT NULL COMMENT '所属项目ID',
    `connector_type`  VARCHAR(32)  NOT NULL COMMENT '连接器类型(JDBC/ES/HBASE/HTTP)',
    `default_value`   VARCHAR(512)          DEFAULT NULL COMMENT '默认值',
    `exception_value` VARCHAR(512)          DEFAULT NULL COMMENT '异常值',
    `timeout`         BIGINT                DEFAULT NULL COMMENT '超时时间(毫秒)',
    `resource_json`   TEXT                  DEFAULT NULL COMMENT '资源JSON配置',
    `crt_user`        VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`        VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    `crt_time`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_resource_key` (`resource_key`, `deleted`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_connector_type` (`connector_type`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='连接器表';

-- ========================================
-- 9. 数据集表 (ttd_data_struct)
-- 实体: DataStructEntity extends BaseEntity
-- ========================================
CREATE TABLE `ttd_data_struct`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `resource_key`  VARCHAR(128) NOT NULL COMMENT '资源唯一标识键',
    `resource_name` VARCHAR(256) NOT NULL COMMENT '资源名称',
    `version`       VARCHAR(32)  NOT NULL DEFAULT '1.0.0' COMMENT '资源版本号',
    `crt_user`      VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`      VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    `crt_time`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`       TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_resource_key` (`resource_key`, `deleted`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='数据集表';

-- ========================================
-- 10. 数据字段表 (ttd_data_field)
-- 实体: DataFieldEntity (不继承BaseEntity，无逻辑删除)
-- ========================================
CREATE TABLE `ttd_data_field`
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `data_struct_code` VARCHAR(128) NOT NULL COMMENT '数据集编码',
    `field_code`       VARCHAR(128) NOT NULL COMMENT '字段编码',
    `field_name`       VARCHAR(256) NOT NULL COMMENT '字段名称',
    `factor_code`      VARCHAR(128)          DEFAULT NULL COMMENT '指标编码',
    `object_type`      VARCHAR(32)  NOT NULL COMMENT '字段类型(NUMBER/STRING/BOOLEAN)',
    `default_value`    VARCHAR(512)          DEFAULT NULL COMMENT '默认值',
    `sort_order`       INT          NOT NULL DEFAULT 0 COMMENT '排序顺序',
    PRIMARY KEY (`id`),
    KEY `idx_data_struct_code` (`data_struct_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='数据字段表';

-- ========================================
-- 11. 事件消息表 (ttd_event_message)
-- 实体: EventMessageEntity (不继承BaseEntity，字段直接定义)
-- ========================================
CREATE TABLE `ttd_event_message`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `event_id`       VARCHAR(128) NOT NULL COMMENT '事件ID',
    `event_type`     VARCHAR(64)  NOT NULL COMMENT '事件类型',
    `operation_type` VARCHAR(32)  NOT NULL COMMENT '操作类型(DEPLOY/UPDATE/OFFLINE)',
    `body`           TEXT         NOT NULL COMMENT '事件体数据(JSON)',
    `status`         VARCHAR(32)  NOT NULL DEFAULT 'PENDING' COMMENT '处理状态(PENDING/PROCESSING/SUCCESS/FAILED)',
    `retry_count`    INT          NOT NULL DEFAULT 0 COMMENT '重试次数',
    `error_message`  TEXT                  DEFAULT NULL COMMENT '错误信息',
    `crt_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `crt_user`       VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`       VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_event_id` (`event_id`),
    KEY `idx_event_type` (`event_type`),
    KEY `idx_status` (`status`),
    KEY `idx_crt_time` (`crt_time`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='事件消息表';

-- ========================================
-- 12. 接入点表 (ttd_access_point)
-- 实体: AccessPointEntity extends BaseEntity
-- ========================================
CREATE TABLE `ttd_access_point`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `code`       VARCHAR(128) NOT NULL COMMENT '接入点编码',
    `name`       VARCHAR(256) NOT NULL COMMENT '接入点名称',
    `version`    VARCHAR(32)  NOT NULL DEFAULT '1.0.0' COMMENT '版本号',
    `remark`     VARCHAR(1024)         DEFAULT NULL COMMENT '备注',
    `project_id` BIGINT       NOT NULL COMMENT '所属项目ID',
    `api_json`   TEXT                  DEFAULT NULL COMMENT 'API文档JSON',
    `crt_user`   VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`   VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    `crt_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`    TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`, `deleted`),
    KEY `idx_project_id` (`project_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='接入点表';

-- ========================================
-- 13. 接入点请求参数表 (ttd_access_point_param)
-- 实体: AccessPointParamEntity extends BaseEntity
-- ========================================
CREATE TABLE `ttd_access_point_param`
(
    `id`                BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `access_point_code` VARCHAR(128) NOT NULL COMMENT '接入点编码',
    `param_name`        VARCHAR(256) NOT NULL COMMENT '参数名称',
    `version`           VARCHAR(32)  NOT NULL DEFAULT '1.0.0' COMMENT '版本号',
    `param_code`        VARCHAR(128) NOT NULL COMMENT '参数编码',
    `param_type`        VARCHAR(32)  NOT NULL COMMENT '参数类型(STRING/NUMBER/BOOLEAN/DATE)',
    `required`          TINYINT      NOT NULL DEFAULT 0 COMMENT '是否必填(0-否,1-是)',
    `default_value`     VARCHAR(512)          DEFAULT NULL COMMENT '默认值',
    `description`       VARCHAR(512)          DEFAULT NULL COMMENT '描述',
    `sort_order`        INT          NOT NULL DEFAULT 0 COMMENT '排序顺序',
    `parent_param_code` VARCHAR(128)          DEFAULT NULL COMMENT '父参数编码',
    `param_level`       INT          NOT NULL DEFAULT 0 COMMENT '参数级别(0-根节点,1-一级,2-二级)',
    `crt_user`          VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`          VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    `crt_time`          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`           TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    KEY `idx_access_point_code` (`access_point_code`),
    KEY `idx_parent_param_code` (`parent_param_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='接入点请求参数表';

-- ========================================
-- 14. 元字段测试用例表 (ttd_meta_field_test_case)
-- 实体: MetaFieldTestCaseEntity extends BaseEntity
-- ========================================
CREATE TABLE `ttd_meta_field_test_case`
(
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `meta_field_code` VARCHAR(128) NOT NULL COMMENT '元字段编码',
    `case_type`       VARCHAR(128) NOT NULL COMMENT '用例类型(NORMAL-普通,ERROR-异常)',
    `biz_order_no`    VARCHAR(128) NOT NULL COMMENT '交易号',
    `case_content`    TEXT                  DEFAULT NULL COMMENT '请求数据(JSON)',
    `target_value`    TEXT                  DEFAULT NULL COMMENT '期望值',
    `crt_user`        VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`        VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    `crt_time`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    KEY `idx_meta_field_code` (`meta_field_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='元字段测试用例表';

-- ========================================
-- 15. 审核记录表 (ttd_audit)
-- 实体: AuditEntity (不继承BaseEntity)
-- ========================================
CREATE TABLE `ttd_audit`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `resource_type`  VARCHAR(128) NOT NULL COMMENT '资源类型(feature/factor/connector)',
    `resource_key`   VARCHAR(128) NOT NULL COMMENT '资源唯一标识键',
    `resource_name`  VARCHAR(256) NOT NULL COMMENT '资源名称',
    `audit_status`   VARCHAR(32)  NOT NULL COMMENT '审核状态(PENDING/APPROVED/REJECTED)',
    `operation_type` VARCHAR(32)  NOT NULL COMMENT '操作类型(ADD/UPDATE/DELETE)',
    `before_content` TEXT                  DEFAULT NULL COMMENT '变更前内容(JSON)',
    `after_content`  TEXT                  DEFAULT NULL COMMENT '变更后内容(JSON)',
    `audit_comment`  TEXT                  DEFAULT NULL COMMENT '审核意见',
    `submit_user`    VARCHAR(128) NOT NULL COMMENT '提交人',
    `audit_user`     VARCHAR(128)          DEFAULT NULL COMMENT '审核人',
    `submit_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    `audit_time`     DATETIME              DEFAULT NULL COMMENT '审核时间',
    PRIMARY KEY (`id`),
    KEY `idx_resource_key` (`resource_key`),
    KEY `idx_resource_type` (`resource_type`),
    KEY `idx_audit_status` (`audit_status`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='审核记录表';

-- ========================================
-- 16. 资源变更记录表 (ttd_resource_chg)
-- 实体: ResourceChgEntity (不继承BaseEntity)
-- ========================================
CREATE TABLE `ttd_resource_chg`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `resource_key`   VARCHAR(128) NOT NULL COMMENT '资源唯一标识键',
    `resource_type`  VARCHAR(128) NOT NULL COMMENT '资源类型',
    `operation_type` VARCHAR(128) NOT NULL COMMENT '操作类型(ADD/UPDATE/DELETE/ROLLBACK)',
    `before_version` VARCHAR(32)           DEFAULT NULL COMMENT '变更前版本号',
    `after_version`  VARCHAR(32)           DEFAULT NULL COMMENT '变更后版本号',
    `before_content` TEXT                  DEFAULT NULL COMMENT '变更前资源内容(JSON)',
    `after_content`  TEXT                  DEFAULT NULL COMMENT '变更后资源内容(JSON)',
    `audit_status`   VARCHAR(32)           DEFAULT NULL COMMENT '审核状态',
    `crt_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `crt_user`       VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    KEY `idx_resource_key` (`resource_key`),
    KEY `idx_resource_type` (`resource_type`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='资源变更记录表';

-- ========================================
-- 17. 字典表 (ttd_dict)
-- 实体: DictEntity extends BaseEntity
-- ========================================
CREATE TABLE `ttd_dict`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `system_code` VARCHAR(128) NOT NULL COMMENT '系统编码',
    `dict_code`   VARCHAR(128) NOT NULL COMMENT '字典编码',
    `dict_name`   VARCHAR(256) NOT NULL COMMENT '字典名称',
    `crt_user`    VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`    VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    `crt_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_system_dict_code` (`system_code`, `dict_code`, `deleted`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='字典表';

-- ========================================
-- 18. 字典编码表 (ttd_dict_code)
-- 实体: DictCodeEntity extends BaseEntity
-- ========================================
CREATE TABLE `ttd_dict_code`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dict_id`    BIGINT       NOT NULL COMMENT '字典ID(关联ttd_dict.id)',
    `dict_key`   VARCHAR(128) NOT NULL COMMENT '字典键',
    `dict_value` VARCHAR(512) NOT NULL COMMENT '字典值',
    `crt_user`   VARCHAR(128)          DEFAULT NULL COMMENT '创建人',
    `upt_user`   VARCHAR(128)          DEFAULT NULL COMMENT '修改人',
    `crt_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `upt_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`    TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dict_key` (`dict_id`, `dict_key`, `deleted`),
    KEY `idx_dict_id` (`dict_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='字典编码表';

-- ========================================
-- 19. 序列表 (ttd_sequence)
-- 实体: SequenceEntity (不继承BaseEntity)
-- ========================================
CREATE TABLE `ttd_sequence`
(
    `id`       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `seq_code` VARCHAR(128) NOT NULL COMMENT '序列编码',
    `seq_name` VARCHAR(256) NOT NULL COMMENT '序列名称',
    `val`      BIGINT       NOT NULL DEFAULT 0 COMMENT '序列当前值',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_seq_code` (`seq_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='序列表';
