import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {Button, Form, Input, message, Modal, Popconfirm, Space, Tag} from 'antd';
import React, {useEffect, useRef, useState} from 'react';
import {PlusOutlined} from '@ant-design/icons';
import {CustomProTable} from '@/components';
import {addDict, deleteDict, DictDetail, pageQueryDict, updateDict,} from '@/services/srv/dictApi';
import DictCodeDrawer from './DictCodeDrawer';
import {DeletedEnum} from '@/components/consts/AppEnum';

const DictIndex: React.FC = () => {
    const actionRef = useRef<ActionType | null>(null);
    const [modalVisible, setModalVisible] = useState(false);
    const [dictCodeVisible, setDictCodeVisible] = useState(false);
    const [editMode, setEditMode] = useState<'add' | 'edit'>('add');
    const [currentRow, setCurrentRow] = useState<DictDetail | null>(null);
    const [form] = Form.useForm();

    useEffect(() => {
        if (modalVisible) {
            if (editMode === 'edit' && currentRow) {
                form.setFieldsValue({
                    systemCode: currentRow.systemCode,
                    dictCode: currentRow.dictCode,
                });
            } else {
                form.resetFields();
            }
        }
    }, [modalVisible, editMode, currentRow, form]);

    /** 删除 */
    const handleDelete = (id: number) => {
        deleteDict(id)
            .then((res: any) => {
                if (res.code === '0000') {
                    message.success('删除成功');
                    actionRef.current?.reload();
                } else {
                    message.error(res.message || '删除失败');
                }
            })
            .catch((e: any) => message.error(e.message || '删除失败'));
    };

    /** 新增 */
    const handleAdd = () => {
        setEditMode('add');
        setCurrentRow(null);
        setModalVisible(true);
    };

    /** 编辑 */
    const handleEdit = (record: DictDetail) => {
        setEditMode('edit');
        setCurrentRow(record);
        setModalVisible(true);
    };

    /** 提交 */
    const handleSubmit = () => {
        form.validateFields().then((values) => {
            const apiCall =
                editMode === 'add'
                    ? addDict(values)
                    : updateDict({...values, id: currentRow!.id});

            apiCall
                .then((res: any) => {
                    if (res.code === '0000') {
                        message.success(editMode === 'add' ? '添加成功' : '更新成功');
                        setModalVisible(false);
                        actionRef.current?.reload();
                    } else {
                        message.error(res.message || '操作失败');
                    }
                })
                .catch((e: any) => message.error(e.message || '操作失败'));
        });
    };

    const columns: ProColumns<DictDetail>[] = [
        // { title: 'ID', dataIndex: 'id', key: 'id', width: 80, align: 'center' },
        {
            title: '系统编码',
            dataIndex: 'systemCode',
            key: 'systemCode',
            width: 100,
            ellipsis: true,
        },
        {
            title: '字典编码',
            dataIndex: 'dictCode',
            key: 'dictCode',
            width: 100,
            ellipsis: true,
        },
        {
            title: '字典名称',
            dataIndex: 'dictName',
            key: 'dictName',
            width: 100,
            ellipsis: true,
        },
        {
            title: '状态',
            dataIndex: 'deleted',
            key: 'deleted',
            width: 100,
            valueEnum: DeletedEnum,
            render: (_, record) => {
                return record.deleted ? <Tag color={'red'}>无效</Tag> : <Tag color={'green'}>有效</Tag>
            }
        },

        {
            title: '创建人',
            dataIndex: 'crtUser',
            key: 'crtUser',
            width: 120,
            align: 'center',
            search: false,
        },
        {
            title: '创建时间',
            dataIndex: 'crtTime',
            key: 'crtTime',
            width: 180,
            valueType: 'dateTime',
            align: 'center',
            sorter: true,
            search: false
        },
        {
            title: '操作',
            key: 'action',
            width: 180,
            align: 'center',
            search: false,
            render: (_, record) => (
                <Space size="small">
                    <Button type="link" onClick={() => {
                        setDictCodeVisible(true)
                        setCurrentRow(record)
                    }}>
                        明细
                    </Button>
                    <Button type="link" onClick={() => handleEdit(record)}>
                        编辑
                    </Button>
                    <Popconfirm
                        title="确定删除？"
                        onConfirm={() => handleDelete(record.id!)}
                        okText="确定"
                        cancelText="取消"
                    >
                        <Button type="link" danger>
                            删除
                        </Button>
                    </Popconfirm>

                </Space>
            ),
        },
    ];

    return (
        <>
            <CustomProTable<DictDetail>
                actionRef={actionRef}
                dateFormatter="string"
                rowKey="id"
                cardBordered
                headerTitle="字典列表"
                toolBarRender={() => [
                    <Button key="add-btn" type="primary" icon={<PlusOutlined/>} onClick={handleAdd}>
                        添加字典
                    </Button>,
                ]}
                request={(params: any, sorter: any, filter: any) => {
                    return pageQueryDict({...params, sorter, filter});
                }}
                columns={columns}
            />

            <Modal
                title={editMode === 'add' ? '添加字典' : '编辑字典'}
                open={modalVisible}
                onOk={handleSubmit}
                onCancel={() => setModalVisible(false)}
                destroyOnClose
                width={480}
            >
                <Form form={form} layout="vertical">
                    <Form.Item name="systemCode" label="系统编码" rules={[{required: true, message: '请输入系统编码'}]}>
                        <Input placeholder="请输入系统编码"/>
                    </Form.Item>
                    <Form.Item name="dictCode" label="字典编码" rules={[{required: true, message: '请输入字典编码'}]}>
                        <Input placeholder="请输入字典编码"/>
                    </Form.Item>
                    <Form.Item name="dictName" label="字典名称" rules={[{required: true, message: '请输入字典名称'}]}>
                        <Input placeholder="请输入字典名称"/>
                    </Form.Item>
                </Form>
            </Modal>
            {
                dictCodeVisible && <DictCodeDrawer open={dictCodeVisible} dictId={currentRow?.id || undefined}
                                                   onClose={() => setDictCodeVisible(false)}/>
            }

        </>
    );
};

export default DictIndex;
