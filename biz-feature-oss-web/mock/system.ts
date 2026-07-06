import type {Request, Response} from 'express';

// =============================================
//  模拟数据
// =============================================

// 租户数据
const tenants: any[] = [
    {id: 1, name: '默认租户', crtTime: '2026-05-10 10:00:00', crtUser: 'admin'},
    {id: 2, name: '测试租户A', crtTime: '2026-05-11 14:30:00', crtUser: 'admin'},
    {id: 3, name: '测试租户B', crtTime: '2026-05-12 09:15:00', crtUser: 'admin'},
];

// 项目数据
const projects: any[] = [
    {id: 1, tenantId: 1, projectCode: 'RCS', name: '智能决策引擎', crtTime: '2026-05-10 10:00:00', crtUser: 'admin'},
    {id: 2, tenantId: 1, projectCode: 'DAT', name: '数据分析平台', crtTime: '2026-05-11 11:00:00', crtUser: 'admin'},
    {id: 3, tenantId: 1, projectCode: 'UHX', name: '用户画像系统', crtTime: '2026-05-12 08:30:00', crtUser: 'admin'},
    {id: 4, tenantId: 2, projectCode: 'TET', name: '测试项目', crtTime: '2026-05-13 16:00:00', crtUser: 'user'},
];

// 账户数据
const accounts: any[] = [
    {id: 1, userAccount: 'admin', crtTime: '2026-05-10 10:00:00', crtUser: 'system'},
    {id: 2, userAccount: 'user', crtTime: '2026-05-11 10:00:00', crtUser: 'admin'},
    {id: 3, userAccount: 'test01', crtTime: '2026-05-12 10:00:00', crtUser: 'admin'},
];

let nextTenantId = 4;
let nextProjectId = 5;
let nextAccountId = 4;

// =============================================
//  租户管理 Mock
// =============================================

/**
 * 租户分页查询
 * POST /api/tenant/page
 */
function tenantPage(req: Request, res: Response) {
    const {current = 1, pageSize = 10, name} = req.body;

    let filtered = [...tenants];
    if (name) {
        filtered = filtered.filter(t => t.name.includes(name));
    }

    const start = (current - 1) * pageSize;
    const data = filtered.slice(start, start + pageSize);

    res.json({
        code: '0000',
        message: 'success',
        current,
        pageSize,
        total: filtered.length,
        data,
    });
}

/**
 * 租户添加
 * POST /api/tenant/add
 */
function tenantAdd(req: Request, res: Response) {
    const {name} = req.body;

    if (!name) {
        res.json({code: '0001', message: '租户名称不能为空'});
        return;
    }

    const newTenant = {
        id: nextTenantId++,
        name,
        crtTime: new Date().toISOString().replace('T', ' ').substring(0, 19),
        crtUser: 'admin',
    };
    tenants.push(newTenant);

    res.json({code: '0000', message: 'success', data: true});
}

/**
 * 租户删除
 * POST /api/tenant/delete
 */
function tenantDelete(req: Request, res: Response) {
    const id = Number(req.query.id);
    const index = tenants.findIndex(t => t.id === id);

    if (index === -1) {
        res.json({code: '0001', message: '租户不存在'});
        return;
    }

    tenants.splice(index, 1);
    res.json({code: '0000', message: 'success', data: true});
}

// =============================================
//  项目管理 Mock
// =============================================

/**
 * 项目分页查询
 * POST /api/project/page
 */
function projectPage(req: Request, res: Response) {
    const {current = 1, pageSize = 10, name, projectCode} = req.body;

    let filtered = [...projects];
    if (name) {
        filtered = filtered.filter(p => p.name.includes(name));
    }
    if (projectCode) {
        filtered = filtered.filter(p => p.projectCode === projectCode);
    }

    const start = (current - 1) * pageSize;
    const data = filtered.slice(start, start + pageSize);

    res.json({
        code: '0000',
        message: 'success',
        current,
        pageSize,
        total: filtered.length,
        data,
    });
}

/**
 * 项目添加
 * POST /api/project/add
 */
function projectAdd(req: Request, res: Response) {
    const {tenantId, projectCode, name} = req.body;

    if (!tenantId || !projectCode || !name) {
        res.json({code: '0001', message: '参数不能为空'});
        return;
    }

    // 检查项目代码唯一性
    if (projects.find(p => p.projectCode === projectCode)) {
        res.json({code: '0002', message: '项目代码已存在'});
        return;
    }

    const newProject = {
        id: nextProjectId++,
        tenantId,
        projectCode,
        name,
        crtTime: new Date().toISOString().replace('T', ' ').substring(0, 19),
        crtUser: 'admin',
    };
    projects.push(newProject);

    res.json({code: '0000', message: 'success', data: true});
}

/**
 * 项目更新
 * POST /api/project/update
 */
function projectUpdate(req: Request, res: Response) {
    const {id, name} = req.body;

    const project = projects.find(p => p.id === id);
    if (!project) {
        res.json({code: '0001', message: '项目不存在'});
        return;
    }

    if (name) project.name = name;
    res.json({code: '0000', message: 'success', data: true});
}

/**
 * 项目删除
 * POST /api/project/delete
 */
function projectDelete(req: Request, res: Response) {
    const id = Number(req.query.id);
    const index = projects.findIndex(p => p.id === id);

    if (index === -1) {
        res.json({code: '0001', message: '项目不存在'});
        return;
    }

    projects.splice(index, 1);
    res.json({code: '0000', message: 'success', data: true});
}

// =============================================
//  账户管理 Mock
// =============================================

/**
 * 账户分页查询
 * POST /api/account/page
 */
function accountPage(req: Request, res: Response) {
    const {current = 1, pageSize = 10, userAccount} = req.body;

    let filtered = [...accounts];
    if (userAccount) {
        filtered = filtered.filter(a => a.userAccount.includes(userAccount));
    }

    const start = (current - 1) * pageSize;
    const data = filtered.slice(start, start + pageSize);

    res.json({
        code: '0000',
        message: 'success',
        current,
        pageSize,
        total: filtered.length,
        data,
    });
}

/**
 * 账户添加
 * POST /api/account/add
 */
function accountAdd(req: Request, res: Response) {
    const {userAccount, password} = req.body;

    if (!userAccount || !password) {
        res.json({code: '0001', message: '账户名和密码不能为空'});
        return;
    }

    if (accounts.find(a => a.userAccount === userAccount)) {
        res.json({code: '0002', message: '账户已存在'});
        return;
    }

    const newAccount = {
        id: nextAccountId++,
        userAccount,
        crtTime: new Date().toISOString().replace('T', ' ').substring(0, 19),
        crtUser: 'admin',
    };
    accounts.push(newAccount);

    res.json({code: '0000', message: 'success', data: true});
}

/**
 * 账户更新
 * POST /api/account/update
 */
function accountUpdate(req: Request, res: Response) {
    const {id, userAccount} = req.body;

    const account = accounts.find(a => a.id === id);
    if (!account) {
        res.json({code: '0001', message: '账户不存在'});
        return;
    }

    if (userAccount) account.userAccount = userAccount;
    res.json({code: '0000', message: 'success', data: true});
}

/**
 * 账户删除
 * POST /api/account/delete
 */
function accountDelete(req: Request, res: Response) {
    const id = Number(req.query.id);
    const index = accounts.findIndex(a => a.id === id);

    if (index === -1) {
        res.json({code: '0001', message: '账户不存在'});
        return;
    }

    accounts.splice(index, 1);
    res.json({code: '0000', message: 'success', data: true});
}

// =============================================
//  Export
// =============================================
export default {
    // 租户管理
    'POST /api/tenant/page': tenantPage,
    'POST /api/tenant/add': tenantAdd,
    'POST /api/tenant/delete': tenantDelete,

    // 项目管理
    'POST /api/project/page': projectPage,
    'POST /api/project/add': projectAdd,
    'POST /api/project/update': projectUpdate,
    'POST /api/project/delete': projectDelete,

    // 账户管理
    'POST /api/account/page': accountPage,
    'POST /api/account/add': accountAdd,
    'POST /api/account/update': accountUpdate,
    'POST /api/account/delete': accountDelete,

    /**
     * 重置密码
     * POST /api/account/resetPassword
     */
    'POST /api/account/resetPassword': (req: Request, res: Response) => {
        const {id, newPassword} = req.body;
        const account = accounts.find(a => a.id === id);
        if (!account) {
            res.json({code: '0001', message: '账户不存在'});
            return;
        }
        // 模拟密码重置成功
        res.json({code: '0000', message: 'success', data: true});
    },
};
