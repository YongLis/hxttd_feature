import React, {useRef} from 'react';
import {Button, Input, Space, Tag, Typography} from 'antd';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {AuditRecord, pageQueryAudit} from '@/services/srv/auditApi';
import {CustomPageContainer, CustomProTable} from '@/components';
import {ResourceTypeEnum} from '@/components/consts/AppEnum';
import {history} from '@umijs/max';

const {Text} = Typography;
const {TextArea} = Input;

/** 操作类型映射 */
const operationTypeLabel: Record<string, string> = {
    ADD: '新增',
    UPDATE: '修改',
    DELETE: '删除',
};

const operationTypeColor: Record<string, string> = {
    ADD: 'blue',
    UPDATE: 'orange',
    DELETE: 'red',
};

const auditStatusColorMap: Record<string, string> = {
    PENDING: 'orange',
    APPROVED: 'green',
    REJECTED: 'red',
};

const auditStatusTextMap: Record<string, string> = {
    PENDING: '待审核',
    APPROVED: '已通过',
    REJECTED: '已驳回',
};


const toAuditView = (record: AuditRecord) => {
    history.push(`/audit/view`, {id: record.id, resourceType: record.resourceType});
};
const AuditIndex: React.FC = () => {
    const actionRef = useRef<ActionType | null>(null);


    const listColumns: ProColumns<AuditRecord>[] = [
        {
            title: '资源类型', dataIndex: 'resourceType', key: 'resourceType', width: 100, align: 'center',
            valueEnum: ResourceTypeEnum,
            search: true,
        },
        {
            title: '资源标识键',
            dataIndex: 'resourceKey',
            key: 'resourceKey',
            width: 160,
            ellipsis: true,
            copyable: true,
        },
        {
            title: '指标名称',
            dataIndex: 'resourceName',
            key: 'resourceName',
            width: 140,
            ellipsis: true,
            hideInSearch: true,
        },

        {
            title: '操作类型',
            dataIndex: 'operationType',
            key: 'operationType',
            width: 80,
            align: 'center',
            hideInSearch: true,
            render: (_, r) => <Tag
                color={operationTypeColor[r.operationType]}>{operationTypeLabel[r.operationType] || r.operationType}</Tag>,
        },
        {
            title: '审核状态', dataIndex: 'auditStatus', key: 'auditStatus', width: 100, align: 'center',
            valueType: 'select',
            valueEnum: {PENDING: '待审核', APPROVED: '已通过', REJECTED: '已驳回'},
            render: (_, r) => <Tag
                color={auditStatusColorMap[r.auditStatus] || 'default'}>{auditStatusTextMap[r.auditStatus] || r.auditStatus}</Tag>,
        },
        {title: '提交人', dataIndex: 'submitUser', key: 'submitUser', width: 100, align: 'center', hideInSearch: true},
        {
            title: '提交时间',
            dataIndex: 'submitTime',
            key: 'submitTime',
            width: 160,
            valueType: 'dateTime',
            align: 'center',
            hideInSearch: true
        },
        {
            title: '操作', key: 'action', width: 160, align: 'center', hideInSearch: true,
            fixed: 'right',
            render: (_, record) => (
                <Space size="small">
                    {record.auditStatus === 'PENDING' ? (
                        <>
                            <Button type="link" onClick={() => toAuditView(record)}>审核</Button>
                        </>
                    ) : (
                        <Tag color={auditStatusColorMap[record.auditStatus]}>
                            {auditStatusTextMap[record.auditStatus] || record.auditStatus}
                        </Tag>
                    )}
                </Space>
            ),
        },
    ];

    return (
        <CustomPageContainer title={false}>
            <CustomProTable<AuditRecord>
                actionRef={actionRef}
                dateFormatter="string"
                rowKey="id"
                cardBordered
                pagination={{pageSize: 10}}
                request={(params: any) => pageQueryAudit({...params})}
                columns={listColumns}
            />
        </CustomPageContainer>
    );
};

export default AuditIndex;
