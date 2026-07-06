import {RemoteRequestPost} from '@/utils/http';

export interface AccessPointDataSource {
    id?: number;
    sourceCategory: string;
    sourceCode: string;
    sourceName: string;
    connectionInfo?: string;
    description?: string;
    sortOrder?: number;
}

export interface ParamItem {
    id?: number;
    paramName: string;
    paramCode: string;
    paramLevel?: number;
    paramType: string;
    version?: string;
    required?: number;
    defaultValue?: string;
    description?: string;
    sortOrder?: number;
    parentId?: number;
    children?: ParamItem[];
}

export interface AccessPointDetail {
    id: number;
    code: string;
    name: string;
    remark: string;
    projectId: number;
    crtUser: string;
    deleted: boolean;
    crtTime: string;
    params?: ParamItem[];
}

/** getApiDoc 返回类型 */
export interface AccessPointDocDetail {
    apiUrl: string;
    code: string;
    name: string;
    version: string;
    remark: string;
    status: string;
    crtUser: string;
    crtTime: string;
    reqParam?: ParamItem[];
    resParam?: ParamItem[];
}


export interface PageResult<T> {
    code: string;
    data: T[];
    total: number;
    pageSize: number;
    current: number;
}

/**
 * 分页查询接入点
 */
export function pageQueryAccessPoint(params: any) {
    return RemoteRequestPost<PageResult<AccessPointDetail>>('/api/access-point/page', params);
}

/**
 * 添加接入点
 */
export function addAccessPoint(params: any) {
    return RemoteRequestPost('/api/access-point/add', params);
}

/**
 * 更新接入点
 */
export function updateAccessPoint(params: any) {
    return RemoteRequestPost('/api/access-point/update', params);
}

/**
 * 删除接入点
 */
export function deleteAccessPoint(id: number) {
    return RemoteRequestPost('/api/access-point/delete?id=' + id, {});
}

/**
 * 获取接入点详情
 */
export function getAccessPointDetail(id: number) {
    return RemoteRequestPost<{ code: string; data: AccessPointDetail }>('/api/access-point/detail?id=' + id, {});
}

export function getAccessPointDoc(id: number) {
    return RemoteRequestPost<{ code: string; data: AccessPointDocDetail }>('/api/access-point/getApiDoc?id=' + id, {});
}

/**
 * 获取全部接入点（下拉列表用）
 */
export interface AccessPointOption {
    code: string;
    name: string;
}

export function getAllAccessPoint() {
    return RemoteRequestPost<{ code: string; data: AccessPointOption[] }>('/api/access-point/getAll', {});
}

/**
 * 查询接入点参数
 *
 */
export function queryAccessPointParams(id: number) {
    return RemoteRequestPost<{ code: string; data: ParamItem[] }>('/api/access-point/params?id=' + id, {});
}
