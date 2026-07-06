import React, {useRef, useState} from 'react';
import {Button, message, Popconfirm, Space, Tag, Typography} from 'antd';
import {PlusOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {history} from '@umijs/max';
import {deleteFactor, FactorDetail, pageQueryFactor} from '@/services/srv/factorApi';
import FactorModal from './FactorModal';
import {CustomPageContainer, CustomProTable} from '@/components';
import {FactorTypeColorMap, FactorTypeEnum, FactorTypeLabelMap} from '@/components/consts/AppEnum';

const {Text} = Typography;

const FactorIndex: React.FC = () => {
    const actionRef = useRef<ActionType | null>(null);
    const [showModal, setShowModal] = useState(false);
    const [currentRow, setCurrentRow] = useState<FactorDetail>();

    const handleDelete = (id: number, factorType: string) => {
        deleteFactor(id, factorType)
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

    const columns: ProColumns<FactorDetail>[] = [
        {
            title: '资源标识键',
            dataIndex: 'resourceKey',
            key: 'resourceKey',
            width: 140,
            ellipsis: true,
            copyable: true,
            search: true,
        },
        {title: '资源名称', dataIndex: 'resourceName', key: 'resourceName', width: 160, ellipsis: true, search: true},
        {
            title: '版本',
            dataIndex: 'version',
            key: 'version',
            width: 80,
            align: 'center',
            search: false,
            ellipsis: true
        },
        {
            title: '指标类型', dataIndex: 'factorType', key: 'factorType', width: 100, align: 'center',
            search: true, valueEnum: FactorTypeEnum,
            render: (_, r) => <Tag
                color={FactorTypeColorMap[r.factorType]}>{FactorTypeLabelMap[r.factorType] || r.factorType}</Tag>,
        },
        {
            title: '返回值', dataIndex: 'returnType', key: 'returnType', width: 100, align: 'center',
            search: false,
            render: (_, r) => <Tag color={'blue'}>{r.returnType}</Tag>,
        },
        {title: '超时(ms)', dataIndex: 'timeout', key: 'timeout', width: 90, align: 'center', search: false},
        {title: '创建人', dataIndex: 'crtUser', key: 'crtUser', width: 100, align: 'center', search: false},
        {
            title: '创建时间',
            dataIndex: 'crtTime',
            key: 'crtTime',
            width: 170,
            valueType: 'dateTime',
            align: 'center',
            search: false
        },
        {
            title: '操作', key: 'action', width: 160, align: 'center',
            search: false,
            render: (_, record) => (
                <Space size="small">
                    <Button type="link" onClick={() => {
                        if (record.factorType === 'DERIVATIVE') {
                            history.push('/factor/derivative/update', {record});
                        } else {
                            setCurrentRow(record);
                            setShowModal(true);
                        }
                    }}>编辑</Button>
                    <Popconfirm title="确定删除？" onConfirm={() => handleDelete(record.id!, record.factorType)}
                                okText="确定" cancelText="取消">
                        <Button type="link" danger>删除</Button>
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <CustomPageContainer title={false}>
            <CustomProTable<FactorDetail>
                actionRef={actionRef}
                headerTitle="指标列表"
                dateFormatter="string"
                rowKey="id"
                cardBordered
                toolBarRender={() => [
                    <Button key="add-btn" type="primary" icon={<PlusOutlined/>}
                            onClick={() => history.push('/factor/meta/add')}>
                        元字段指标
                    </Button>,
                    <Button key="add-btn" type="primary" icon={<PlusOutlined/>}
                            onClick={() => history.push('/factor/derivative/add')}>
                        衍生指标
                    </Button>,
                    <Button key="add-btn" type="primary" icon={<PlusOutlined/>}
                            onClick={() => history.push('/factor/feature/add')}>
                        实时指标
                    </Button>,
                ]}
                request={(params: any, sorter: any, filter: any) => pageQueryFactor({...params, sorter, filter})}
                columns={columns}
            />
            {showModal && (
                <FactorModal
                    visible={showModal}
                    record={currentRow}
                    onSuccess={() => {
                        setShowModal(false);
                        setCurrentRow(undefined);
                        actionRef.current?.reload();
                    }}
                    onCancel={() => {
                        setShowModal(false);
                        setCurrentRow(undefined);
                    }}
                />
            )}
        </CustomPageContainer>
    );
};

export default FactorIndex;
