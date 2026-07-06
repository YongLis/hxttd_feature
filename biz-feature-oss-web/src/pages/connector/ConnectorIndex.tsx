import React, {useRef} from 'react';
import {Button, message, Popconfirm, Space, Tag} from 'antd';
import {PlusOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {history} from '@umijs/max';
import {ConnectorDetail, deleteConnector, pageQueryConnector} from '@/services/srv/connectorApi';
import {getDictCodeOptions} from '@/services/srv/dictCodeApi';
import {CustomPageContainer, CustomProTable} from '@/components';

const connectorTypeColorMap: Record<string, string> = {
    JDBC: 'blue',
    ES: 'green',
    HBASE: 'purple',
    HTTP: 'orange',
};

const ConnectorIndex: React.FC = () => {
    const actionRef = useRef<ActionType | null>(null);

    const handleDelete = (id: number, connectorType: string) => {
        deleteConnector(id, connectorType)
            .then(res => {
                if (res.code === '0000') {
                    message.success('删除成功');
                    actionRef.current?.reload();
                } else {
                    message.error((res as any).message || '删除失败');
                }
            })
            .catch(e => message.error(e.message || '删除失败'));
    };

    const columns: ProColumns<ConnectorDetail>[] = [
        {
            title: '资源标识键',
            dataIndex: 'resourceKey',
            key: 'resourceKey',
            width: 160,
            ellipsis: true,
            copyable: true,
        },
        {title: '资源名称', dataIndex: 'resourceName', key: 'resourceName', width: 160, ellipsis: true},
        {
            title: '版本',
            dataIndex: 'version',
            key: 'version',
            width: 80,
            align: 'center',
            hideInSearch: true,
            ellipsis: true
        },
        {
            title: '连接器类型', dataIndex: 'connectorType', key: 'connectorType', width: 120, align: 'center',
            valueType: 'select',
            request: async () => {
                return getDictCodeOptions('ttd', 'connectorType');
            },
            render: (_, r) => <Tag color={connectorTypeColorMap[r.connectorType] || 'default'}>{r.connectorType}</Tag>,
        },
        {title: '超时(ms)', dataIndex: 'timeout', key: 'timeout', width: 90, align: 'center', hideInSearch: true},
        {title: '创建人', dataIndex: 'crtUser', key: 'crtUser', width: 100, align: 'center', hideInSearch: true},
        {
            title: '创建时间',
            dataIndex: 'crtTime',
            key: 'crtTime',
            width: 170,
            valueType: 'dateTime',
            align: 'center',
            hideInSearch: true,
        },
        {
            title: '操作', key: 'action', width: 160, align: 'center', hideInSearch: true,
            render: (_, record) => (
                <Space size="small">
                    <Button type="link"
                            onClick={() => history.push(`/connector/${record.connectorType}/add?id=${record.id}`)}>编辑</Button>
                    <Popconfirm title="确定删除？" onConfirm={() => handleDelete(record.id!, record.connectorType)}
                                okText="确定" cancelText="取消">
                        <Button type="link" danger>删除</Button>
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <CustomPageContainer title={false}>
            <CustomProTable<ConnectorDetail>
                actionRef={actionRef}
                headerTitle="连接器列表"
                dateFormatter="string"
                rowKey="id"
                cardBordered
                toolBarRender={() => [
                    <Button key="add-btn-jdbc" type="primary" icon={<PlusOutlined/>}
                            onClick={() => history.push('/connector/jdbc/add')}>
                        JDBC连接器
                    </Button>,
                    <Button key="add-btn-es" type="primary" icon={<PlusOutlined/>}
                            onClick={() => history.push('/connector/es/add')}>
                        ES连接器
                    </Button>,
                    <Button key="add-btn-http" type="primary" icon={<PlusOutlined/>}
                            onClick={() => history.push('/connector/http/add')}>
                        Http连接器
                    </Button>
                ]}
                request={(params: any) => pageQueryConnector({...params})}
                columns={columns}
            />
        </CustomPageContainer>
    );
};

export default ConnectorIndex;
