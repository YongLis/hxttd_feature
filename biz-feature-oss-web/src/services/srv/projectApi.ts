import {ProjectDetail} from "@/pages/system/data"
import {request} from "@umijs/max";
import {API} from "./typings";

export interface PageResult<T> {
    code: string;
    message: string;
    current: number;
    pageSize: number;
    total: number;
    data: T[];
}

/**
 * 获取项目分页列表
 * @param params 查询参数
 * @returns 分页数据
 */
export async function pageQueryProject(params: any) {
    return request<PageResult<ProjectDetail>>('/api/project/page', {
        method: 'POST',
        data: params,
    });
}


/**
 * 查询所有项目
 */
export async function getAllProject() {
    return request<API.Result<ProjectDetail[]>>('/api/project/getAll', {
        method: 'POST',
    });
}

/**
 * 添加项目
 * @param params 项目数据
 */
export const addProject = (params: any) => {
    const sessionId = localStorage.getItem('sessionId') || ''
    return request('/api/project/add', {
        method: 'POST',
        data: params,
        headers: {
            'Content-Type': 'application/json',
            'X-Session-Id': sessionId
        }
    })
}

/**
 * 更新项目
 * @param params 项目数据
 */
export const updateProject = (params: any) => {
    const sessionId = localStorage.getItem('sessionId') || ''
    return request('/api/project/update', {
        method: 'POST',
        data: params,
        headers: {
            'Content-Type': 'application/json',
            'X-Session-Id': sessionId
        }
    })
}

/**
 * 删除项目
 * @param id 项目ID
 */
export const deleteProject = (id: number) => {
    const sessionId = localStorage.getItem('sessionId') || ''
    return request(`/api/project/delete?id=${id}`, {
        method: 'POST',
        headers: {
            'X-Session-Id': sessionId
        }
    })
}

// 导出 ProjectDetail 类型供其他页面使用
export type {ProjectDetail}
