import React, {useRef} from 'react';
import {Button, message, Popconfirm, Space} from 'antd';
import {PlusOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {history} from '@umijs/max';
import {deleteFeatureConfig, FeatureConfigDetail, pageQueryFeatureConfig} from '@/services/srv/featureConfigApi';
import {CustomPageContainer, CustomProTable} from '@/components';

const FeatureConfigIndex: React.FC = () => {
    const actionRef = useRef<ActionType | null>(null);

    const handleDelete = (id: number) => {
        deleteFeatureConfig(id)
            .then(res => {
                if (res.code === '0000') {
                    message.success('删除成功');
                    actionRef.current?.reload();
                } else {
                    message.error(res.message || '删除失败');
                }
            })
            .catch(e => message.error(e.message || '删除失败'));
    };

    const columns: ProColumns<FeatureConfigDetail>[] = [
        {
            title: '特征编码', dataIndex: 'featureCode', key: 'featureCode', width: 120, ellipsis: true, copyable: true
        },
        {title: '资源标识键', dataIndex: 'resourceKey', key: 'resourceKey', width: 140, ellipsis: true, copyable: true},
        {title: '资源名称', dataIndex: 'resourceName', key: 'resourceName', width: 150, ellipsis: true},
        {title: '版本', dataIndex: 'version', key: 'version', width: 80, align: 'center', ellipsis: true},
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
            title: '操作', key: 'action', width: 180, align: 'center',
            fixed: 'right',
            render: (_, record) => (
                <Space size="small">
                    <Button type="link"
                            onClick={() => history.push('/feature-config/detail?id=' + record.id)}>详情</Button>
                    <Button type="link"
                            onClick={() => history.push('/feature-config/update?id=' + record.id)}>编辑</Button>
                    <Popconfirm title="确定删除？" onConfirm={() => handleDelete(record.id!)} okText="确定"
                                cancelText="取消">
                        <Button type="link" danger>删除</Button>
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <CustomPageContainer title={false}>
            <CustomProTable<FeatureConfigDetail>
                actionRef={actionRef}
                dateFormatter="string"
                rowKey="id"
                cardBordered
                optionsRender={() => [
                    <Button key="add-btn" type="primary" icon={<PlusOutlined/>}
                            onClick={() => history.push('/feature-config/add')}>
                        添加特征
                    </Button>,
                ]}
                pagination={{
                    pageSize: 10,
                }}
                request={(params: any, sorter: any, filter: any) => pageQueryFeatureConfig({...params, sorter, filter})}
                columns={columns}
            />
        </CustomPageContainer>
    );
};

export default FeatureConfigIndex;
