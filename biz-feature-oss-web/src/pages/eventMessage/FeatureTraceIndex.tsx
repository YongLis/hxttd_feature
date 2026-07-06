import React, {useRef} from 'react';
import {Tag, Typography} from 'antd';
import {EyeOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {FeatureTraceDetail, pageQueryFeatureTrace} from '@/services/srv/featureTraceApi';
import {CustomPageContainer, CustomProTable} from '@/components';

const {Text} = Typography;

const statusColorMap: Record<string, string> = {
    PENDING: 'orange',
    PROCESSING: 'blue',
    SUCCESS: 'green',
    FAILED: 'red',
};

const operationColorMap: Record<string, string> = {
    DEPLOY: 'blue',
    UPDATE: 'orange',
    OFFLINE: 'red',
};

/**
 * 特征溯源列表页(只读)
 */
const FeatureTraceIndex: React.FC = () => {
    const actionRef = useRef<ActionType | null>(null);

    const columns: ProColumns<FeatureTraceDetail>[] = [
        {title: 'ID', dataIndex: 'id', key: 'id', width: 60, align: 'center'},
        {
            title: '事件ID', dataIndex: 'eventId', key: 'eventId', width: 180, ellipsis: true,
            render: (_, r) => <Text code>{r.eventId}</Text>,
        },
        {title: '事件类型', dataIndex: 'eventType', key: 'eventType', width: 100, align: 'center'},
        {
            title: '操作类型', dataIndex: 'operationType', key: 'operationType', width: 100, align: 'center',
            render: (_, r) => <Tag
                color={operationColorMap[r.operationType] || 'default'}>{r.operationType || '-'}</Tag>,
        },
        {
            title: '状态', dataIndex: 'status', key: 'status', width: 100, align: 'center',
            render: (_, r) => <Tag color={statusColorMap[r.status] || 'default'}>{r.status || '-'}</Tag>,
        },
        {title: '重试次数', dataIndex: 'retryCount', key: 'retryCount', width: 80, align: 'center'},
        {title: '错误信息', dataIndex: 'errorMessage', key: 'errorMessage', width: 180, ellipsis: true},
        {title: '创建人', dataIndex: 'crtUser', key: 'crtUser', width: 100, align: 'center'},
        {
            title: '创建时间',
            dataIndex: 'crtTime',
            key: 'crtTime',
            width: 170,
            valueType: 'dateTime',
            align: 'center',
            sorter: true,
        },
        {
            title: '操作', key: 'action', width: 80, align: 'center',
            render: (_, record) => (
                <a onClick={() => alert(`事件体:\n${record.body || '无'}`)}>
                    <EyeOutlined/> 查看详情
                </a>
            ),
        },
    ];

    return (
        <CustomPageContainer title={false}>
            <CustomProTable<FeatureTraceDetail>
                actionRef={actionRef}
                headerTitle="特征溯源"
                dateFormatter="string"
                rowKey="id"
                cardBordered
                search={{labelWidth: 'auto'}}
                request={(params: any, sorter: any, filter: any) => pageQueryFeatureTrace({...params, sorter, filter})}
                columns={columns}
            />
        </CustomPageContainer>
    );
};

export default FeatureTraceIndex;
