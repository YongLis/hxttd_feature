import {RemoteRequestGet, RemoteRequestPost} from '@/utils/http';

export interface ConnectorDetail {
    id: number;
    resourceKey: string;
    resourceName: string;
    version: string;
    projectId: number;
    connectorType: string;
    defaultValue: string;
    exceptionValue: string;
    factorCodes: string[];
    timeout: number;
    resourceJson?: string;
    crtUser: string;
    crtTime: string;
}

export interface PageResult<T> {
    code: string;
    data: T[];
    total: number;
    pageSize: number;
    current: number;
}

/** 分页查询连接器列表 */
export function pageQueryConnector(params: any) {
    return RemoteRequestPost<PageResult<ConnectorDetail>>('/api/connector/page', params);
}

/** 获取连接器详情 */
export function getConnectorDetail(id: number) {
    return RemoteRequestGet<{ code: string; data: ConnectorDetail }>('/api/connector/detail?id=' + id);
}

/** 添加连接器 */
export function addConnector(params: any) {
    return RemoteRequestPost('/api/connector/add', params);
}

/** 更新连接器 */
export function updateConnector(params: any) {
    return RemoteRequestPost('/api/connector/update', params);
}

/** 删除连接器 */
export function deleteConnector(id: number, connectorType: string) {
    return RemoteRequestPost('/api/connector/delete?id=' + id + '&connectorType=' + connectorType, {});
}
