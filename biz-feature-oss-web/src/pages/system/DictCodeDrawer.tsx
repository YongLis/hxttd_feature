import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {Button, Drawer, Form, Input, message, Modal, Popconfirm, Space, Tag} from 'antd';
import React, {useEffect, useRef, useState} from 'react';
import {DeleteOutlined, EditOutlined, PlusOutlined} from '@ant-design/icons';
import {CustomProTable} from '@/components';
import {
    addDictCode,
    deleteDictCode,
    DictCodeDetail,
    pageQueryDictCode,
    updateDictCode,
} from '@/services/srv/dictCodeApi';
import {DictDetail, pageQueryDict} from '@/services/srv/dictApi';
import {DeletedEnum} from '@/components/consts/AppEnum';

interface Props {
    open: boolean;
    dictId: number | undefined;
    onClose: () => void;
}


const DictCodeDrawer: React.FC<Props> = ({open, dictId, onClose}) => {
    const actionRef = useRef<ActionType | null>(null);
    const [modalVisible, setModalVisible] = useState(false);
    const [editMode, setEditMode] = useState<'add' | 'edit'>('add');
    const [currentRow, setCurrentRow] = useState<DictCodeDetail | null>(null);
    const [dictOptions, setDictOptions] = useState<{ label: string; value: number }[]>([]);
    const [form] = Form.useForm();

    // 加载字典下拉选项
    useEffect(() => {
        pageQueryDict({current: 1, pageSize: 1000, dictId: dictId}).then((res: any) => {
            if (res?.data) {
                setDictOptions(
                    res.data.map((d: DictDetail) => ({
                        label: `${d.systemCode} / ${d.dictCode}${d.dictName ? ' - ' + d.dictName : ''}`,
                        value: d.id,
                    })),
                );
            }
        });
    }, []);

    useEffect(() => {
        if (modalVisible) {
            if (editMode === 'edit' && currentRow) {
                form.setFieldsValue({
                    dictId: currentRow.dictId,
                    dictKey: currentRow.dictKey,
                    dictValue: currentRow.dictValue,
                });
            } else {
                form.resetFields();
            }
        }
    }, [modalVisible, editMode, currentRow, form]);

    /** 删除 */
    const handleDelete = (id: number) => {
        deleteDictCode(id)
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
    const handleEdit = (record: DictCodeDetail) => {
        setEditMode('edit');
        setCurrentRow(record);
        setModalVisible(true);
    };

    /** 提交 */
    const handleSubmit = () => {
        form.validateFields().then((values) => {
            const apiCall =
                editMode === 'add'
                    ? addDictCode(values)
                    : updateDictCode({...values, id: currentRow!.id});

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

    const columns: ProColumns<DictCodeDetail>[] = [
        // { title: 'ID', dataIndex: 'id', key: 'id', width: '10%', align: 'center' },
        {
            title: '字典ID',
            dataIndex: 'dictId',
            key: 'dictId',
            width: '10%',
            align: 'center',
            search: false,
        },
        {
            title: '字典键',
            dataIndex: 'dictKey',
            key: 'dictKey',
            width: '20%',
            ellipsis: true,
            search: false,
        },
        {
            title: '字典值',
            dataIndex: 'dictValue',
            key: 'dictValue',
            width: '30%',
            ellipsis: true,
            search: false,
        },
        {
            title: '状态',
            dataIndex: 'deleted',
            key: 'deleted',
            width: '12%',
            align: 'center',
            valueEnum: DeletedEnum,
            render: (_, record) => {
                return record.deleted ? <Tag color={'red'}>无效</Tag> : <Tag color={'green'}>有效</Tag>
            }
        },

        {
            title: '创建人',
            dataIndex: 'crtUser',
            key: 'crtUser',
            width: '12%',
            align: 'center',
            search: false,

        },
        {
            title: '创建时间',
            dataIndex: 'crtTime',
            key: 'crtTime',
            width: '18%',
            valueType: 'dateTime',
            align: 'center',
            search: false,
        },
        {
            title: '操作',
            key: 'action',
            width: '16%',
            align: 'center',
            search: false,
            render: (_, record) => (
                <Space size="small">
                    <Button type="text" icon={<EditOutlined/>} onClick={() => handleEdit(record)}/>
                    <Popconfirm
                        title="确定删除？"
                        onConfirm={() => handleDelete(record.id!)}
                        okText="确定"
                        cancelText="取消"
                    >
                        <Button type="text" danger icon={<DeleteOutlined/>}/>
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <Drawer title="字典键值列表" width={'80%'} open={open} onClose={onClose}>
            <CustomProTable<DictCodeDetail>
                actionRef={actionRef}
                dateFormatter="string"
                rowKey="id"
                cardBordered
                // search={false}
                toolBarRender={() => [
                    <Button key="add-btn" type="primary" icon={<PlusOutlined/>} onClick={handleAdd}>
                        添加键值
                    </Button>,
                ]}
                request={(params: any, sorter: any, filter: any) => {
                    return pageQueryDictCode({...{dictId: dictId}, ...params, sorter, filter});
                }}
                columns={columns}
            />

            <Modal
                title={editMode === 'add' ? '添加字典键值' : '编辑字典键值'}
                open={modalVisible}
                onOk={handleSubmit}
                onCancel={() => setModalVisible(false)}
                destroyOnClose
                width={520}
            >
                <Form form={form} layout="horizontal" initialValues={{dictId: dictId}}>
                    <Form.Item name="dictId" label="所属字典" rules={[{required: true, message: '请选择所属字典'}]}>
                        <input disabled/>
                    </Form.Item>
                    <Form.Item name="dictKey" label="字典键" rules={[{required: true, message: '请输入Key'}]}>
                        <Input placeholder="请输入Key"/>
                    </Form.Item>
                    <Form.Item name="dictValue" label="字典值" rules={[{required: true, message: '请输入Value'}]}>
                        <Input placeholder="请输入Value"/>
                    </Form.Item>
                </Form>
            </Modal>
        </Drawer>
    );
};

export default DictCodeDrawer;
