import {RemoteRequestPost} from '@/utils/http';

/** 表字段定义 */
export interface TableColumnDetail {
    id: string;
    tableName: string;
    columnName: string;
    columnType: string;
    nullAble: string;
    factorCode?: string;
    crtUser?: string;
    crtTime?: string;
}

/** 表定义详情 */
export interface TableDefDetail {
    id: string;
    tableName: string;
    dataSource: string;
    topic?: string;
    columnCount: number;
    columns?: TableColumnDetail[];
    crtUser?: string;
    crtTime?: string;
    uptUser?: string;
    uptTime?: string;
    deleted: boolean;
}

/** 表字段请求参数 */
export interface TableColumnReq {
    id?: string;
    columnName: string;
    columnType: string;
    nullAble: string;
    factorCode?: string;
}

/** 添加表定义 */
export function addTableDef(params: {
    tableName: string;
    dataSource: string;
    topic?: string;
    columns?: TableColumnReq[];
    projectId?: number;
}) {
    return RemoteRequestPost('/api/table-def/add', params);
}

/** 更新表定义 */
export function updateTableDef(params: {
    id: string;
    tableName: string;
    dataSource: string;
    topic?: string;
    columns?: TableColumnReq[];
    projectId?: number;
}) {
    return RemoteRequestPost('/api/table-def/update', params);
}

/** 删除表定义 */
export function deleteTableDef(id: string) {
    return RemoteRequestPost('/api/table-def/delete?id=' + id, {});
}

/** 分页查询表定义 */
export function pageQueryTableDef(params: any) {
    return RemoteRequestPost('/api/table-def/page', params);
}

/** 查询表定义详情 */
export function getTableDefDetail(tableName: string) {
    return RemoteRequestPost('/api/table-def/detail?tableName=' + encodeURIComponent(tableName), {});
}

/** 表定义选项（下拉列表用） */
export interface TableDefOption {
    tableName: string;
    dataSource?: string;
}

/** 获取所有有效表定义 */
export function getAllTableDef() {
    return RemoteRequestPost<{ code: string; data: TableDefOption[] }>('/api/table-def/getAll', {});
}
