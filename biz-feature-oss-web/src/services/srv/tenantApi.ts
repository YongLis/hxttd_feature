import {TenantDetail} from "@/pages/system/data"
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
 * 获取租户分页列表
 * @param params 查询参数
 * @returns 分页数据
 */
export async function pageQueryTenant(params: any) {
    return request<PageResult<TenantDetail>>('/api/tenant/page', {
        method: 'POST',
        data: params,
    });
}

/**
 * 添加租户
 * @param params 租户数据
 */
export const addTenant = (params: any) => {
    const sessionId = localStorage.getItem('sessionId') || ''
    return request('/api/tenant/add', {
        method: 'POST',
        data: params,
        headers: {
            'Content-Type': 'application/json',
            'X-Session-Id': sessionId
        }
    })
}

/**
 * 删除租户
 * @param id 租户ID
 */
export const deleteTenant = (id: number) => {
    const sessionId = localStorage.getItem('sessionId') || ''
    return request(`/api/tenant/delete?id=${id}`, {
        method: 'POST',
        headers: {
            'X-Session-Id': sessionId
        }
    })
}

// 导出 TenantDetail 类型供其他页面使用
export type {TenantDetail}
