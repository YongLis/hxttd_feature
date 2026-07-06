import {request} from "@umijs/max";
import {ProjectMemberDetail} from "@/pages/system/data";

export interface PageResult<T> {
    code: string;
    message: string;
    current: number;
    pageSize: number;
    total: number;
    data: T[];
}

/**
 * 分页查询项目成员
 */
export async function pageQueryProjectUser(params: any) {
    return request<PageResult<ProjectMemberDetail>>('/api/project-user/page', {
        method: 'POST',
        data: params,
    });
}

/**
 * 添加项目成员
 */
export const addProjectUser = (params: { projectId: number; userName: string }) => {
    const sessionId = localStorage.getItem('sessionId') || '';
    return request('/api/project-user/add', {
        method: 'POST',
        data: params,
        headers: {
            'Content-Type': 'application/json',
            'X-Session-Id': sessionId,
        },
    });
};

/**
 * 删除项目成员
 */
export const deleteProjectUser = (projectId: number, userName: string) => {
    const sessionId = localStorage.getItem('sessionId') || '';
    return request('/api/project-user/delete', {
        method: 'POST',
        data: {projectId, userName},
        headers: {
            'Content-Type': 'application/json',
            'X-Session-Id': sessionId,
        },
    });
};

export type {ProjectMemberDetail};
