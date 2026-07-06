import {RemoteRequestPost} from '@/utils/http';

export interface DictDetail {
    id: number;
    systemCode: string;
    dictCode: string;
    dictName: string;
    crtTime?: string;
    crtUser?: string;
    deleted: boolean;
}

export interface PageResult<T> {
    code: string;
    message: string;
    current: number;
    pageSize: number;
    total: number;
    data: T[];
}

/**
 * 分页查询字典
 */
export function pageQueryDict(params: any) {
    return RemoteRequestPost<PageResult<DictDetail>>('/api/dict/page', params);
}

/**
 * 添加字典
 */
export function addDict(params: any) {
    return RemoteRequestPost('/api/dict/add', params);
}

/**
 * 更新字典
 */
export function updateDict(params: any) {
    return RemoteRequestPost('/api/dict/update', params);
}

/**
 * 删除字典
 */
export function deleteDict(id: number) {
    return RemoteRequestPost('/api/dict/delete?id=' + id, {});
}
