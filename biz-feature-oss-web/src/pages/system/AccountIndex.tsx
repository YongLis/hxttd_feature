import type {ActionType, ProColumns,} from '@ant-design/pro-components';
import {Button, message, Popconfirm, Space} from 'antd';
import React, {useRef, useState} from 'react';
import {DeleteOutlined, EditOutlined, PlusOutlined} from '@ant-design/icons';
import {AccountDetail, deleteAccount, pageQueryAccount} from '@/services/srv/accountApi';
import AccountModal from './AccountModal';
import {CustomPageContainer, CustomProTable} from '@/components';

const AccountIndex: React.FC = () => {
    const actionRef = useRef<ActionType | null>(null);
    const [showModal, setShowModal] = useState<boolean>(false);
    const [modalMode, setModalMode] = useState<'add' | 'edit'>('add');
    const [currentRow, setCurrentRow] = useState<AccountDetail>();

    // 删除账户
    const handleDelete = (id: number) => {
        deleteAccount(id)
            .then(res => {
                if (res.code === '0000') {
                    message.success('删除成功');
                    actionRef.current?.reload();
                } else {
                    message.error(res.message || '删除失败');
                }
            })
            .catch(e => {
                message.error(e.message || '删除失败');
            });
    };

    // 编辑账户
    const handleEdit = (record: AccountDetail) => {
        setCurrentRow(record);
        setModalMode('edit');
        setShowModal(true);
    };

    const columns: ProColumns<AccountDetail>[] = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
            width: 80,
            align: 'center'
        },
        {
            title: '账户名称',
            dataIndex: 'userAccount',
            key: 'userAccount',
            width: 300,
        },
        {
            title: '创建人',
            dataIndex: 'crtUser',
            key: 'crtUser',
            width: 150,
            align: 'center'
        },
        {
            title: '创建时间',
            dataIndex: 'crtTime',
            key: 'crtTime',
            width: 200,
            valueType: 'dateTime',
            align: 'center',
            sorter: true,
        },
        {
            title: '操作',
            key: 'action',
            width: 200,
            align: 'center',
            render: (_, record) => (
                <Space size="small">
                    <Button
                        type="text"
                        icon={<EditOutlined/>}
                        onClick={() => handleEdit(record)}
                    >
                        编辑
                    </Button>
                    <Popconfirm
                        title="确定要删除该账户吗？"
                        onConfirm={() => handleDelete(record.id!)}
                        okText="确定"
                        cancelText="取消"
                    >
                        <Button
                            type="text"
                            danger
                            icon={<DeleteOutlined/>}
                        >
                            删除
                        </Button>
                    </Popconfirm>
                </Space>
            )
        }
    ];

    return (
        <CustomPageContainer title={false}>
            <CustomProTable<AccountDetail>
                actionRef={actionRef}
                dateFormatter='string'
                rowKey="id"
                cardBordered={true}
                search={false}
                toolBarRender={() => [
                    <Button
                        key='account-add-btn'
                        type='primary'
                        icon={<PlusOutlined/>}
                        onClick={() => {
                            setModalMode('add');
                            setCurrentRow(undefined);
                            setShowModal(true);
                        }}
                    >
                        添加账户
                    </Button>
                ]}
                request={(params: any, sorter: any, filter: any) => {
                    return pageQueryAccount({...params, sorter, filter});
                }}
                columns={columns}
            />

            {/* 新增/编辑账户 Modal */}
            {
                showModal &&
                <AccountModal
                    visible={showModal}
                    mode={modalMode}
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
            }
        </CustomPageContainer>
    );
};

export default AccountIndex;
