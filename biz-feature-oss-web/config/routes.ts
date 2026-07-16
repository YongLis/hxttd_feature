/**
 * @name umi 的路由配置
 * @description 只支持 path,component,routes,redirect,wrappers,name,icon 的配置
 * @param path  path 只支持两种占位符配置，第一种是动态参数 :id 的形式，第二种是 * 通配符，通配符只能出现路由字符串的最后。
 * @param component 配置 location 和 path 匹配后用于渲染的 React 组件路径。可以是绝对路径，也可以是相对路径，如果是相对路径，会从 src/pages 开始找起。
 * @param routes 配置子路由，通常在需要为多个路径增加 layout 组件时使用。
 * @param redirect 配置路由跳转
 * @param wrappers 配置路由组件的包装组件，通过包装组件可以为当前的路由组件组合进更多的功能。 比如，可以用于路由级别的权限校验
 * @param name 配置路由的标题，默认读取国际化文件 menu.ts 中 menu.xxxx 的值，如配置 name 为 login，则读取 menu.ts 中 menu.login 的取值作为标题
 * @param icon 配置路由的图标，取值参考 https://ant.design/components/icon-cn， 注意去除风格后缀和大小写，如想要配置图标为 <StepBackwardOutlined /> 则取值应为 stepBackward 或 StepBackward，如想要配置图标为 <UserOutlined /> 则取值应为 user 或者 User
 * @doc https://umijs.org/docs/guides/routes
 */
export default [
    {
        path: '/',
        redirect: '/home/index',
    },
    {
        path: '/home',
        routes: [
            {
                path: '/home',
                redirect: '/home/index',
            },
            {
                path: '/home/index',
                component: './home/index',
            },
        ],
    },
    {
        path: '/nav',
        name: '系统导航',
        redirect: '/home/index',
    },
    {
        path: '/user',
        layout: false,
        routes: [
            {
                path: '/user',
                redirect: '/user/login',
            },
            {
                path: '/user/login',
                layout: false,
                name: 'login',
                component: './user/login',
            },
            {
                component: '404',
                path: '/user/*',
            }
        ],
    },
    {
        path: '/access-point',
        name: '接入点管理',
        routes: [
            {
                path: '/access-point/index',
                name: '接入点列表',
                component: './accessPoint/AccessPointIndex'
            },
            {
                path: '/access-point/add',
                name: '添加接入点',
                component: './accessPoint/AccessPointAdd',
                hideInMenu: true,
            },
            {
                path: '/access-point/edit',
                name: '编辑接入点',
                component: './accessPoint/AccessPointAdd',
                hideInMenu: true,
            },
            {
                path: '/access-point/api',
                name: 'API接口文档',
                component: './accessPoint/AccessPointApi',
                hideInMenu: true,
            },
        ]
    },
    {
        path: '/meta-field',
        name: '元字段管理',
        routes: [
            {
                path: '/meta-field/index',
                name: '元字段列表',
                component: './metaField/MetaFieldIndex'
            },
            {
                path: '/meta-field/add',
                name: '添加元字段',
                component: './metaField/MetaFieldAdd',
                hideInMenu: true,
            },
            {
                path: '/meta-field/update',
                name: '修改元字段',
                component: './metaField/MetaFieldUpdate',
                hideInMenu: true,
            },
            {
                path: '/meta-field/detail',
                name: '元字段详情',
                component: './metaField/FieldDetail',
                hideInMenu: true,
            },
            {
                path: '/meta-field/test-case',
                name: '测试用例',
                component: './metaField/TestCaseIndex'
            },
            {
                path: '/meta-field/test-case/add',
                name: '添加测试用例',
                component: './metaField/TestCaseAdd',
                hideInMenu: true,
            },

        ]
    },
    {
        path: '/feature-config',
        name: '实时特征',
        routes: [
            {
                path: '/feature-config/index',
                name: '特征列表',
                component: './featureConfig/FeatureConfigIndex'
            },
            {
                path: '/feature-config/add',
                name: '添加特征',
                component: './featureConfig/FeatureConfigAdd',
                hideInMenu: true,
            },
            {
                path: '/feature-config/update',
                name: '更新特征',
                component: './featureConfig/FeatureConfigUpdate',
                hideInMenu: true,
            },
            {
                path: '/feature-config/detail',
                name: '特征详情',
                component: './featureConfig/FeatureDetailView',
                hideInMenu: true,
            },
        ]
    },
    {
        path: '/connector',
        name: '连接器管理',
        routes: [
            {
                path: '/connector/index',
                name: '连接器列表',
                component: './connector/ConnectorIndex'
            },
            {
                path: '/connector/add',
                name: '添加连接器',
                component: './connector/ConnectorAdd',
                hideInMenu: true,
            },
            {
                path: '/connector/edit',
                name: '编辑连接器',
                component: './connector/ConnectorAdd',
                hideInMenu: true,
            },
            {
                path: '/connector/jdbc/add',
                name: 'JDBC连接器',
                component: './connector/ConnectorJdbc',
                hideInMenu: true,
            },
            {
                path: '/connector/es/add',
                name: 'ES连接器',
                component: './connector/ConnectorEs',
                hideInMenu: true,
            },
            {
                path: '/connector/http/add',
                name: 'HTTP连接器',
                component: './connector/ConnectorHttp',
                hideInMenu: true,
            },
        ]
    },
    {
        path: '/factor',
        name: '指标管理',
        routes: [
            {
                path: '/factor/index',
                name: '指标列表',
                component: './factor/FactorIndex'
            },
            {
                path: '/factor/meta/add',
                name: '添加指标',
                component: './factor/FactorMetaAdd',
                hideInMenu: true,
            },
            {
                path: '/factor/derivative/add',
                name: '添加指标',
                component: './factor/FactorDerivativeAdd',
                hideInMenu: true,
            },
            {
                path: '/factor/derivative/update',
                name: '编辑衍生指标',
                component: './factor/FactorDerivativeUpdate',
                hideInMenu: true,
            },
            {
                path: '/factor/feature/add',
                name: '添加指标',
                component: './factor/FactorFeatureAdd',
                hideInMenu: true,
            },
        ]
    },
    {
        path: '/event-message',
        name: '流程实例',
        routes: [
            {
                path: '/event-message/feature-trace',
                name: '特征溯源',
                component: './eventMessage/FeatureTraceIndex'
            },
        ]
    },

    {
        path: '/data-pipeline',
        name: '数据通道',
        routes: [
            {
                path: '/data-pipeline/kafka-topic',
                name: '主题管理',
                component: './kafkaTopic/KafkaTopicIndex'
            },
        ]
    },

    {
        path: '/audit',
        name: '审核管理',
        routes: [
            {
                path: '/audit/index',
                name: '审核列表',
                component: './audit/AuditIndex'
            },
            {
                path: '/audit/view',
                name: '审核',
                component: './audit/AuditView',
                hideInMenu: true,
            },
            // {
            //   path: '/audit/detail',
            //   name: '审核详情',
            //   component: './audit/AuditDetail',
            //   hideInMenu: true,
            // },
        ]
    },


    {
        path: '/system',
        name: '系统配置',
        // access: 'isAdmin',
        routes: [
            {
                path: '/system/project',
                name: '项目管理',
                component: './system/ProjectIndex'
            },
            {
                path: '/system/account',
                name: '账户管理',
                component: './system/AccountIndex'
            },
            {
                path: '/system/dict',
                name: '字典管理',
                component: './system/DictIndex'
            },
            {
                path: '/system/project-member',
                name: '项目成员',
                component: './system/ProjectMemberIndex',
                hideInMenu: true,
            },
        ]
    },
    {
        component: '404',
        path: '/*',
    },
];
