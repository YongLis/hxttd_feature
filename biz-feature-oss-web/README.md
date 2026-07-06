# biz-feature-oss-web — 运营支撑后台（前端应用）

## 概述

`biz-feature-oss-web` 是 TTD 统一特征平台的运营管理后台前端应用，基于 UmiJS Max + Ant Design Pro
体系，提供特征配置、指标管理、连接器、元字段、接入点、审核流程、系统配置、特征溯源等全部管理功能。

## 技术栈

| 组件                  | 版本     | 用途                        |
|---------------------|--------|---------------------------|
| UmiJS Max           | 4.3.x  | React 企业级框架（路由/请求/权限）     |
| React               | 19.x   | 视图层                       |
| TypeScript          | 5.x    | 类型安全                      |
| Ant Design          | 5.25.x | UI 组件库                    |
| ProComponents       | 2.7.x  | ProTable / ProForm 高级业务组件 |
| CodeMirror 6        | 6.x    | Groovy / SQL 代码编辑器        |
| @xyflow/react       | 12.x   | 特征血缘 DAG 可视化              |
| AntV / L7           | 2.x    | 地理可视化                     |
| jspdf + html2canvas | —      | PDF 导出（接入点文档）             |
| WangEditor          | 5.x    | 富文本编辑器                    |
| Biome               | 2.x    | 代码规范检查                    |
| Jest                | 30.x   | 单元测试                      |

## 目录结构

```
biz-feature-oss-web/
├── config/
│   ├── routes.ts                    # 路由表（11个页面模块）
│   ├── proxy.ts                     # 开发代理 → localhost:7082
│   └── config.ts                    # UmiJS 框架配置
├── src/
│   ├── pages/                       # 页面组件（按功能模块目录）
│   │   ├── home/                    #   首页 Dashboard
│   │   ├── user/                    #   登录页
│   │   ├── accessPointModel/        #   接入点 CRUD + API 文档 + PDF 导出
│   │   ├── metaField/               #   元字段 + 测试用例管理
│   │   ├── featureConfig/           #   实时特征配置（事件过滤/窗口配置）
│   │   ├── connector/               #   连接器管理（JDBC/ES/HBase/HTTP 类型表单）
│   │   ├── factor/                  #   指标管理 + 血缘可视化
│   │   ├── eventMessage/            #   流程实例 / 特征溯源
│   │   ├── audit/                   #   审核中心（特征/指标/连接器审批）
│   │   └── system/                  #   系统配置（项目/账户/字典）
│   ├── services/srv/                # API 请求层（统一 request 封装）
│   ├── components/                  # 公共组件
│   │   ├── CustomProTable           #   统一分页表格
│   │   ├── GroovyEditor             #   CodeMirror 6 Groovy 编辑器
│   │   └── FlowChart                #   @xyflow/react 血缘图
│   └── utils/                       # 工具函数
└── package.json
```

## 路由结构

| 路由                  | 页面   | 说明                  |
|---------------------|------|---------------------|
| `/home/index`       | 首页   | Dashboard           |
| `/user/login`       | 登录   | 用户登录                |
| `/access-point/*`   | 接入点  | CRUD + API 文档 + PDF |
| `/meta-field/*`     | 元字段  | 管理 + 测试用例           |
| `/feature-config/*` | 特征配置 | CRUD + 详情           |
| `/connector/*`      | 连接器  | 类型表单（JDBC/ES/HTTP）  |
| `/factor/*`         | 指标   | 管理 + 血缘图            |
| `/event-message/*`  | 溯源   | 流程实例 / 特征追踪         |
| `/audit/*`          | 审核   | 特征/指标/连接器审批         |
| `/system/*`         | 系统   | 项目/账户/字典            |
| `/*`                | 404  | 兜底页                 |

## 核心页面功能

### 特征配置管理

- 条件脚本编辑（Groovy/JEXL 语法高亮）
- 主/从维度脚本配置
- 聚合模式（SUM/COUNT/AVG/MAX/MIN）+ 时间窗口设置
- Velocity 表达式自动生成与预览

### 指标管理

- 三种指标类型：元字段指标（MetaFactor）/ 派生指标（DerivativeFactor）/ 特征指标（FeatureFactor）
- 超时/默认值/异常值配置
- @xyflow/react 血缘 DAG 可视化（上下游依赖追溯）

### 连接器管理

- 四种连接器类型动态表单（JDBC/ES/HBase/HTTP）
- 资源配置 JSON 编辑器
- 测试连接功能

### 审核中心

- 三种资源审核（特征/指标/连接器）
- 变更前后内容 Diff 对比
- 审批通过 → 自动发布到资源表 + 记录变更历史

## 公共组件

- **CustomProTable**：统一分页表格（`scroll.x = 1200px`）
- **GroovyEditor / SqlEditor**：CodeMirror 6 代码编辑器，支持语法高亮和自动补全
- **FlowChart**：基于 @xyflow/react 的特征血缘 DAG 可视化

## 启动

```bash
cd biz-feature-oss-web

# 安装依赖
yarn install

# 开发模式
yarn dev                          # → http://localhost:8000
# 通过 config/proxy.ts 自动代理 /api/* 到 localhost:7082

# 生产构建
yarn build                        # 产出 dist/ 目录

# 代码检查
npx biome check --write src/
```

## 部署

生产环境将 `dist/` 目录部署到 Nginx，配置反向代理：

```nginx
location /api/ {
    proxy_pass http://127.0.0.1:7082;
}
location / {
    try_files $uri $uri/ /index.html;
}
```
