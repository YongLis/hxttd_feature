import {RemoteRequestPost} from '@/utils/http';

export interface PipeTaskDetail {
    id?: string;
    pointCode?: string;
    taskCode?: string;
    taskName?: string;
    tableName?: string;
    kafkaTopic?: string;
    taskStatus?: string;
    taskPriority?: number;
    crtUser?: string;
    crtTime?: string;
    uptUser?: string;
    uptTime?: string;
    deleted?: boolean;
}

export interface PageResult<T> {
    code: string;
    data: T[];
    total: number;
    pageSize: number;
    current: number;
}

/**
 * 分页查询管道任务
 */
export function pageQueryPipeTask(params: any) {
    return RemoteRequestPost<PageResult<PipeTaskDetail>>('/api/pipe-task/page', params);
}

/**
 * 新增管道任务
 */
export function addPipeTask(params: any) {
    return RemoteRequestPost('/api/pipe-task/add', params);
}

/**
 * 更新管道任务
 */
export function updatePipeTask(params: any) {
    return RemoteRequestPost('/api/pipe-task/update', params);
}

/**
 * 删除管道任务
 */
export function deletePipeTask(id: string) {
    return RemoteRequestPost('/api/pipe-task/delete?id=' + id, {});
}

/**
 * 查询管道任务详情
 */
export function getPipeTaskDetail(id: string) {
    return RemoteRequestPost<PipeTaskDetail>('/api/pipe-task/detail?id=' + id, {});
}
