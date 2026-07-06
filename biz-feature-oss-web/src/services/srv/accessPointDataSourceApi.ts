import {RemoteRequestPost} from '@/utils/http';

export interface DataSourceDetail {
    id: number;
    accessPointId?: number;
    sourceCategory: string;
    sourceCode: string;
    sourceName: string;
    connectionInfo?: string;
    description?: string;
    sortOrder?: number;
    crtUser?: string;
    crtTime?: string;
}

export interface PageResult<T> {
    code: string;
    data: T[];
    total: number;
    pageSize: number;
    current: number;
}

/**
 * 分页查询数据源
 */
export function pageQueryDataSource(params: any) {
    return RemoteRequestPost<PageResult<DataSourceDetail>>('/api/access-point/datasource/page', params);
}

/**
 * 添加数据源
 */
export function addDataSource(params: any) {
    return RemoteRequestPost('/api/access-point/datasource/add', params);
}

/**
 * 更新数据源
 */
export function updateDataSource(params: any) {
    return RemoteRequestPost('/api/access-point/datasource/update', params);
}

/**
 * 删除数据源
 */
export function deleteDataSource(id: number) {
    return RemoteRequestPost('/api/access-point/datasource/delete?id=' + id, {});
}
