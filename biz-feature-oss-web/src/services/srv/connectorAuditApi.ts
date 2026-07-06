import {RemoteRequestPost} from '@/utils/http';

/** 连接器审核列表记录 */
export interface ConnectorAuditRecord {
    id: number;
    resourceKey: string;
    resourceName: string;
    connectorType: string;
    auditStatus: string;
    operationType: string;
    auditComment?: string;
    submitUser: string;
    auditUser?: string;
    submitTime: string;
    auditTime?: string;
}

/** 分页查询连接器审核列表 */
export function pageQueryConnectorAudit(params: any) {
    return RemoteRequestPost<{
        code: string;
        data: ConnectorAuditRecord[];
        total: number;
        pageSize: number;
        current: number;
    }>('/api/connector-audit/page', params);
}

/** 连接器审核操作(通过/驳回) */
export function approveConnectorAudit(params: { id: number; auditStatus: string; auditComment?: string }) {
    return RemoteRequestPost('/api/connector-audit/approve', params);
}
