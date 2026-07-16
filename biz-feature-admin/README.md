# biz-feature-admin-srv — 运营支撑后台（后端服务）

## 概述

`biz-feature-admin-srv` 是 TTD 统一特征平台的运营管理后台后端服务，基于 Spring Boot 3.5 + MyBatis-Plus
3.5，负责特征、指标、连接器、元字段、接入点、字典、项目、账户等全部配置资源的 CRUD 管理与审批流程。

## 技术栈

| 组件                      | 版本     | 用途             |
|-------------------------|--------|----------------|
| Spring Boot             | 3.5.0  | Web 服务框架       |
| Spring Security         | —      | 认证与权限          |
| Spring Cache + Caffeine | —      | 本地缓存           |
| MyBatis-Plus            | 3.5.11 | ORM + 分页       |
| MySQL                   | 8.0.33 | 持久化存储          |
| FastJSON                | 2.0.58 | JSON 序列化       |
| HuTool                  | 5.8.44 | 通用工具集          |
| Aviator                 | 5.4.3  | 表达式引擎（指标校验）    |
| SpringDoc OpenAPI       | 2.8.7  | Swagger API 文档 |
| JDK                     | 17     | 编译与运行          |

## 包结构

```
com.ly.ttd.biz.feature/
├── BizTtdAdminSrvApplication        # Spring Boot 启动类
├── cache/                           # 本地缓存配置
├── common/                          # 公共工具 / 响应封装
├── config/                          # Spring Security / CORS / MyBatis 配置
├── consts/                          # 常量
├── controller/                      # REST 控制器（17个）
│   ├── AccessPointController        #  /api/access-point/*
│   ├── AccountController            #  /api/account/*
│   ├── AuditController              #  /api/audit/*
│   ├── ConnectorAuditController     #  /api/connector-audit/*
│   ├── ConnectorController          #  /api/connector/*
│   ├── DataStructController         #  /api/data-struct/*
│   ├── DictController               #  /api/dict/*
│   ├── DictCodeController           #  /api/dict-code/*
│   ├── EventMessageController       #  /api/event-message/*
│   ├── FactorAuditController        #  /api/factor-audit/*
│   ├── FactorController             #  /api/factor/*
│   ├── FeatureAuditController       #  /api/feature-audit/*
│   ├── FeatureConfigController      #  /api/feature-config/*
│   ├── MetaFieldController          #  /api/meta-field/*
│   ├── MetaFieldTestCaseController  #  /api/meta-field-test-case/*
│   ├── ResChgController             #  /api/resource-chg/*
│   ├── SequenceController           #  /api/sequence/*
│   ├── SystemController             #  /api/system/*
│   ├── ProjectController            #  /api/project/*
│   └── UserController               #  /api/user/*
├── interceptor/                     # 登录拦截器
├── mybatis/
│   ├── entity/                      # 数据库实体（19个 Entity，见 schema.sql）
│   └── mapper/                      # MyBatis-Plus Mapper 接口
├── req/                             # 通用请求对象（PageQuery）
├── res/                             # 通用响应对象
├── srv/                             # 业务服务层（按模块分包）
│   ├── access/                      #   接入点管理（CRUD + API文档 + PDF 导出）
│   ├── account/                     #   账户管理
│   ├── audit/                       #   通用审核流程（特征/指标/连接器审批）
│   ├── connector/                   #   连接器管理
│   ├── dataStruct/                  #   数据集管理
│   ├── dependency/                  #   血缘依赖查询（UP/DOWN）
│   ├── dict/                        #   字典管理（Dict → DictCode 双层）
│   ├── eventMessage/                #   事件消息 / 特征溯源
│   ├── factor/                      #   指标管理（Meta/Derivative/Feature 三种类型）
│   ├── feature/                     #   特征配置管理 + Velocity 表达式生成
│   ├── metaField/                   #   元字段管理 + 测试用例
│   ├── project/                     #   项目管理
│   ├── resource/                    #   资源变更记录（版本历史）
│   ├── sequence/                    #   序列号管理
│   └── user/                        #   用户管理
└── util/                            # 工具类
```

## 数据库

19 张业务表，建表语句见 `src/main/resources/db/schema.sql`：

| 分类    | 表名                                                    |
|-------|-------------------------------------------------------|
| 用户/项目 | `ttd_project`, `ttd_user_account`, `ttd_project_user` |
| 元字段   | `ttd_meta_field`, `ttd_meta_field_test_case`          |
| 特征配置  | `ttd_feature_config`                                  |
| 指标    | `ttd_factor`, `ttd_factor_dependency`                 |
| 连接器   | `ttd_connector`                                       |
| 数据集   | `ttd_data_struct`, `ttd_data_field`                   |
| 接入点   | `ttd_access_point`, `ttd_access_point_param`          |
| 审核    | `ttd_audit`                                           |
| 变更历史  | `ttd_resource_chg`                                    |
| 字典    | `ttd_dict`, `ttd_dict_code`                           |
| 序列    | `ttd_sequence`                                        |
| 事件消息  | `ttd_event_message`                                   |

### 逻辑删除

继承 `BaseEntity` 的表均支持逻辑删除（`deleted` 字段），MyBatis-Plus 自动在 CRUD 时添加 `deleted=0` 条件。

### 审核流程

```
提交 → PENDING → 审批通过 (APPROVED) → 自动写入资源表 + 生成 ResourceChg 变更记录
              → 审批驳回 (REJECTED)  → 记录驳回原因
```

## API 路径一览

| 模块      | 基础路径                             | 说明                       |
|---------|----------------------------------|--------------------------|
| 特征配置    | `/api/feature-config/*`          | CRUD                     |
| 指标      | `/api/factor/*`                  | CRUD + 血缘查询              |
| 连接器     | `/api/connector/*`               | CRUD（JDBC/ES/HBase/HTTP） |
| 元字段     | `/api/meta-field/*`              | CRUD + 测试用例              |
| 接入点     | `/api/access-point/*`            | CRUD + API 文档            |
| 特征审核    | `/api/feature-audit/*`           | 审批流程                     |
| 指标审核    | `/api/factor-audit/*`            | 审批流程                     |
| 连接器审核   | `/api/connector-audit/*`         | 审批流程                     |
| 字典      | `/api/dict/*` `/api/dict-code/*` | 字典主表 + 编码                |
| 数据集     | `/api/data-struct/*`             | 数据集管理                    |
| 项目      | `/api/project/*`                 | 项目管理                     |
| 账户      | `/api/account/*`                 | 账户管理                     |
| 用户      | `/api/user/*`                    | 用户管理                     |
| 事件溯源    | `/api/event-message/*`           | 调用链查询                    |
| 资源变更    | `/api/resource-chg/*`            | 版本历史查询                   |
| 序列号     | `/api/sequence/*`                | 业务序列号                    |
| Swagger | `/swagger-ui.html`               | API 文档                   |

## 启动

```bash
cd biz-feature-admin-srv
mvn spring-boot:run
# 服务端口: 7082
# Swagger: http://localhost:7082/swagger-ui.html
```

## 依赖

- MySQL 8.0（`tcbiz_feature` 数据库）
- Nacos 配置中心（`mysql.properties` 配置项）
