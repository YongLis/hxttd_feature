import {AccountDetail} from "@/pages/system/data"
import {request} from "@umijs/max";

export interface PageResult<T> {
    code: string;
    message: string;
    current: number;
    pageSize: number;
    total: number;
    data: T[];
}

/**
 * 获取账户分页列表
 * @param params 查询参数
 * @returns 分页数据
 */
export async function pageQueryAccount(params: any) {
    return request<PageResult<AccountDetail>>('/api/account/page', {
        method: 'POST',
        data: params,
    });
}

/**
 * 添加账户
 * @param params 账户数据
 */
export const addAccount = (params: any) => {
    const sessionId = localStorage.getItem('sessionId') || ''
    return request('/api/account/add', {
        method: 'POST',
        data: params,
        headers: {
            'Content-Type': 'application/json',
            'X-Session-Id': sessionId
        }
    })
}

/**
 * 更新账户
 * @param params 账户数据
 */
export const updateAccount = (params: any) => {
    const sessionId = localStorage.getItem('sessionId') || ''
    return request('/api/account/update', {
        method: 'POST',
        data: params,
        headers: {
            'Content-Type': 'application/json',
            'X-Session-Id': sessionId
        }
    })
}

/**
 * 删除账户
 * @param id 账户ID
 */
export const deleteAccount = (id: number) => {
    const sessionId = localStorage.getItem('sessionId') || ''
    return request(`/api/account/delete?id=${id}`, {
        method: 'POST',
        headers: {
            'X-Session-Id': sessionId
        }
    })
}

/**
 * 重置密码
 * @param params 账户数据（包含id和新密码）
 */
export const resetPassword = (params: any) => {
    const sessionId = localStorage.getItem('sessionId') || ''
    return request('/api/account/resetPassword', {
        method: 'POST',
        data: params,
        headers: {
            'Content-Type': 'application/json',
            'X-Session-Id': sessionId
        }
    })
}

// 导出 AccountDetail 类型供其他页面使用
export type {AccountDetail}
