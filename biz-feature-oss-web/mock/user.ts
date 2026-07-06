import type {Request, Response} from 'express';

/**
 * 当前登录用户状态
 */
let currentUser: {
    userAccount: string;
    role: string;
    tenantId: number;
    projectIds: number[];
    selectedProjectId: number;
} | null = null;

/**
 * 生成 UUID
 */
function generateUUID(): string {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
        const r = (Math.random() * 16) | 0;
        const v = c === 'x' ? r : (r & 0x3) | 0x8;
        return v.toString(16);
    });
}

// =============================================
//  认证相关 Mock
// =============================================
export default {
    /**
     * 用户登录
     * POST /api/admin/login
     */
    'POST /api/admin/login': (req: Request, res: Response) => {
        const {userName, password} = req.body;

        // 模拟管理员账户
        if (userName === 'admin' && password === 'admin123') {
            const sessionId = generateUUID();

            currentUser = {
                userAccount: 'admin',
                role: 'admin',
                tenantId: 1,
                projectIds: [1, 2],
                selectedProjectId: 1,
            };

            res.json({
                code: '0000',
                message: 'success',
                data: {
                    sessionId,
                    userInfo: {
                        id: 1,
                        userName: 'admin',
                        role: 'admin',
                        nickname: '管理员',
                        email: 'admin@ttd.com',
                        phone: '',
                    },
                },
            });
            return;
        }

        // 模拟普通用户
        if (userName === 'user' && password === 'user123') {
            const sessionId = generateUUID();

            currentUser = {
                userAccount: 'user',
                role: 'user',
                tenantId: 1,
                projectIds: [1],
                selectedProjectId: 1,
            };

            res.json({
                code: '0000',
                message: 'success',
                data: {
                    sessionId,
                    userInfo: {
                        id: 2,
                        userName: 'user',
                        role: 'user',
                        nickname: '普通用户',
                        email: 'user@ttd.com',
                        phone: '',
                    },
                },
            });
            return;
        }

        // 登录失败
        res.json({
            code: '0003',
            message: '账号或密码错误',
        });
    },

    /**
     * 用户登出
     * POST /api/admin/logout
     */
    'POST /api/admin/logout': (_req: Request, res: Response) => {
        currentUser = null;
        res.json({
            code: '0000',
            message: 'success',
            data: true,
        });
    },

    /**
     * 获取当前用户基本信息
     * POST /api/admin/getCurrentUser
     */
    'POST /api/admin/getCurrentUser': (_req: Request, res: Response) => {
        if (!currentUser || !currentUser.userAccount) {
            res.json({
                code: '0004',
                message: '用户未登录',
            });
            return;
        }

        res.json({
            code: '0000',
            message: 'success',
            data: {
                userInfo: {
                    id: currentUser.role === 'admin' ? 1 : 2,
                    userName: currentUser.userAccount,
                    role: currentUser.role,
                    nickname: currentUser.role === 'admin' ? '管理员' : '普通用户',
                    email: `${currentUser.userAccount}@ttd.com`,
                    phone: '',
                },
            },
        });
    },

    /**
     * 获取当前用户完整信息（租户+项目）
     * POST /api/user/getCurrent
     */
    'POST /api/user/getCurrent': (_req: Request, res: Response) => {
        if (!currentUser || !currentUser.userAccount) {
            res.json({
                code: '0004',
                message: '用户未登录',
            });
            return;
        }

        res.json({
            code: '0000',
            message: 'success',
            data: {
                userAccount: currentUser.userAccount,
                role: currentUser.role,
                tenantId: currentUser.tenantId,
                projectIds: currentUser.projectIds,
                selectedProjectId: currentUser.selectedProjectId,
            },
        });
    },
};
