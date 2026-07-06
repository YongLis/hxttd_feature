import React, {useRef, useState} from 'react';
import {Button, message, Popconfirm, Space, Tag, Tooltip, Typography} from 'antd';
import {DeleteOutlined, EditOutlined, PlusOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {history} from '@umijs/max';
import {deleteTestCase, pageQueryTestCase, TestCaseDetail,} from '@/services/srv/testCaseApi';
import TestCaseModal from './TestCaseModal';
import {CustomPageContainer, CustomProTable} from '@/components';

const {Text} = Typography;

const TestCaseIndex: React.FC = () => {
    const actionRef = useRef<ActionType | null>(null);

    const [showModal, setShowModal] = useState(false);
    const [currentRow, setCurrentRow] = useState<TestCaseDetail>();

    /** 删除 */
    const handleDelete = (id: number) => {
        deleteTestCase(id)
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

    const columns: ProColumns<TestCaseDetail>[] = [
        {title: 'ID', dataIndex: 'id', key: 'id', width: 60, align: 'center'},
        {
            title: '元字段编码',
            dataIndex: 'metaFieldCode',
            key: 'metaFieldCode',
            width: 140,
            ellipsis: true,
            render: (_, r) => <Text code>{r.metaFieldCode}</Text>,
        },
        {
            title: '案例类型',
            dataIndex: 'caseType',
            key: 'caseType',
            width: 100,
            align: 'center',
            render: (_, r) => (
                <Tag color={r.caseType === 'NORMAL' ? 'green' : 'red'}>
                    {r.caseType === 'NORMAL' ? '普通用例' : '异常用例'}
                </Tag>
            ),
        },
        {title: '交易号', dataIndex: 'bizOrderNo', key: 'bizOrderNo', width: 150, ellipsis: true},
        {
            title: '请求数据',
            dataIndex: 'caseContent',
            key: 'caseContent',
            width: 200,
            ellipsis: true,
            render: (_, r) => r.caseContent ? (
                <Tooltip title={r.caseContent}>
                    <Text style={{maxWidth: 180}} ellipsis>{r.caseContent}</Text>
                </Tooltip>
            ) : '-',
        },
        {
            title: '期望值',
            dataIndex: 'targetValue',
            key: 'targetValue',
            width: 120,
            ellipsis: true,
        },
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
            title: '操作',
            key: 'action',
            width: 160,
            align: 'center',
            render: (_, record) => (
                <Space size="small">
                    <Button type="text" icon={<EditOutlined/>} onClick={() => {
                        setCurrentRow(record);
                        setShowModal(true);
                    }}>
                        编辑
                    </Button>
                    <Popconfirm title="确定删除？" onConfirm={() => handleDelete(record.id!)} okText="确定"
                                cancelText="取消">
                        <Button type="text" danger icon={<DeleteOutlined/>}>删除</Button>
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <CustomPageContainer title={false}>
            <CustomProTable<TestCaseDetail>
                actionRef={actionRef}
                headerTitle="测试用例列表"
                dateFormatter="string"
                rowKey="id"
                cardBordered
                search={false}
                toolBarRender={() => [
                    <Button
                        key="add-btn"
                        type="primary"
                        icon={<PlusOutlined/>}
                        onClick={() => history.push('/meta-field/test-case/add')}
                    >
                        添加测试用例
                    </Button>,
                ]}
                request={(params: any, sorter: any, filter: any) => {
                    return pageQueryTestCase({...params, sorter, filter});
                }}
                columns={columns}
            />

            {showModal && (
                <TestCaseModal
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

export default TestCaseIndex;
