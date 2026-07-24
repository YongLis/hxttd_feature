import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {App, Button, Form, Input, Modal, Popconfirm, Select, Space, Table, Tag} from 'antd';
import React, {useEffect, useRef, useState} from 'react';
import {PlusOutlined, MinusCircleOutlined} from '@ant-design/icons';
import {CustomProTable} from '@/components';
import {
    addTableDef,
    deleteTableDef,
    type TableDefDetail,
    pageQueryTableDef,
    updateTableDef,
} from '@/services/srv/tableDefApi';
import {getAvailableTopics, type KafkaTopicItem} from '@/services/srv/kafkaTopicApi';
import FactorSelectModal from './FactorSelectModal';
import TableDefDetailView from './TableDefDetailView';

const TableDefIndex: React.FC = () => {
    const {message} = App.useApp();
    const actionRef = useRef<ActionType | null>(null);
    const [modalVisible, setModalVisible] = useState(false);
    const [editMode, setEditMode] = useState<'add' | 'edit'>('add');
    const [currentRow, setCurrentRow] = useState<TableDefDetail | null>(null);
    const [form] = Form.useForm();
    const [detailVisible, setDetailVisible] = useState(false);
    const [detailTableName, setDetailTableName] = useState<string>('');
    const [availableTopics, setAvailableTopics] = useState<KafkaTopicItem[]>([]);
    const [factorModalVisible, setFactorModalVisible] = useState(false);
    const [editingFactorFieldIndex, setEditingFactorFieldIndex] = useState<number | null>(null);

    useEffect(() => {
        if (modalVisible) {
            // 加载可用Topic列表供Select选择
            getAvailableTopics().then((res: any) => {
                if (res.code === '0000' && res.data) {
                    setAvailableTopics(res.data);
                }
            }).catch(() => {});

            if (editMode === 'edit' && currentRow) {
                form.setFieldsValue({
                    tableName: currentRow.tableName,
                    dataSource: currentRow.dataSource,
                    topic: currentRow.topic,
                    columns: currentRow.columns?.map((col) => ({
                        ...col,
                    })) || [],
                });
            } else {
                form.resetFields();
            }
        }
    }, [modalVisible, editMode, currentRow, form]);

    /** 删除 */
    const handleDelete = (id: string) => {
        deleteTableDef(id)
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
    const handleEdit = (record: TableDefDetail) => {
        setEditMode('edit');
        setCurrentRow(record);
        setModalVisible(true);
    };

    /** 详情 */
    const handleDetail = (record: TableDefDetail) => {
        setDetailTableName(record.tableName);
        setDetailVisible(true);
    };

    /** 提交 */
    const handleSubmit = () => {
        form.validateFields().then((values) => {
            const params = {
                tableName: values.tableName,
                dataSource: values.dataSource,
                topic: values.topic,
                columns: (values.columns || []).map((col: any) => ({
                    columnName: col.columnName,
                    columnType: col.columnType,
                    nullAble: col.nullAble,
                    factorCode: col.factorCode,
                })),
            };

            const apiCall =
                editMode === 'add'
                    ? addTableDef(params)
                    : updateTableDef({...params, id: currentRow!.id});

            apiCall
                .then((res: any) => {
                    if (res.code === '0000') {
                        message.success(editMode === 'add' ? '提交审核成功' : '更新已提交审核');
                        setModalVisible(false);
                        actionRef.current?.reload();
                    } else {
                        message.error(res.message || '操作失败');
                    }
                })
                .catch((e: any) => message.error(e.message || '操作失败'));
        });
    };

    const columns: ProColumns<TableDefDetail>[] = [
        {
            title: '表名',
            dataIndex: 'tableName',
            key: 'tableName',
            width: 180,
            ellipsis: true,
        },
        {
            title: '所属库',
            dataIndex: 'dataSource',
            key: 'dataSource',
            width: 160,
            ellipsis: true,
            search: false,
        },
        {
            title: '字段数量',
            dataIndex: 'columnCount',
            key: 'columnCount',
            width: 100,
            align: 'center',
            search: false,
            render: (_, record) => (
                <Tag color={record.columnCount && record.columnCount > 0 ? 'blue' : 'default'}>
                    {record.columnCount || 0}
                </Tag>
            ),
        },
        {
            title: '关联Topic',
            dataIndex: 'topic',
            key: 'topic',
            width: 160,
            ellipsis: true,
            search: false,
            render: (text) => text || '-',
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
            search: false,
        },
        {
            title: '操作',
            key: 'action',
            width: 160,
            align: 'center',
            search: false,
            render: (_, record) => (
                <Space size="small">
                    <Button type="link" onClick={() => handleDetail(record)}>
                        详情
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

    /** 字段编辑子表 */
    const renderColumnTable = (
        fields: {name: number; key: number}[],
        _add: () => void,
        remove: (index: number | number[]) => void,
    ) => {
        interface SubRow {
            name: number;
            key: number;
            [key: string]: any;
        }
        const subColumns: any[] = [
            {
                title: '列名',
                dataIndex: 'columnName',
                width: 160,
                render: (_: any, record: any) => (
                    <Form.Item
                        name={[record.name, 'columnName']}
                        rules={[{required: true, message: '请输入列名'}]}
                        style={{margin: 0}}
                    >
                        <Input placeholder="列名" size="small"/>
                    </Form.Item>
                ),
            },
            {
                title: '数据类型',
                dataIndex: 'columnType',
                width: 140,
                render: (_: any, record: any) => (
                    <Form.Item
                        name={[record.name, 'columnType']}
                        rules={[{required: true, message: '请选择类型'}]}
                        style={{margin: 0}}
                    >
                        <Select
                            size="small"
                            placeholder="数据类型"
                            options={[
                                {label: 'String', value: 'String'},
                                {label: 'Int', value: 'Int'},
                                {label: 'Long', value: 'Long'},
                                {label: 'Double', value: 'Double'},
                                {label: 'Boolean', value: 'Boolean'},
                                {label: 'Date', value: 'Date'},
                                {label: 'Timestamp', value: 'Timestamp'},
                                {label: 'Decimal', value: 'Decimal'},
                            ]}
                        />
                    </Form.Item>
                ),
            },
            {
                title: '是否为空',
                dataIndex: 'nullAble',
                width: 100,
                align: 'center',
                render: (_: any, record: any) => (
                    <Form.Item
                        name={[record.name, 'nullAble']}
                        rules={[{required: true, message: '请选择'}]}
                        style={{margin: 0}}
                    >
                        <Select
                            size="small"
                            options={[
                                {label: '是', value: 'Y'},
                                {label: '否', value: 'N'},
                            ]}
                            style={{width: 80}}
                        />
                    </Form.Item>
                ),
            },
            {
               title: '指标编码',
                dataIndex: 'factorCode',
                width: 200,
                render: (_: any, record: any) => (
                    <Form.Item name={[record.name, 'factorCode']} style={{margin: 0}}>
                        <Input
                            placeholder="可选"
                            size="small"
                            addonAfter={
                                <span
                                    style={{cursor: 'pointer', color: '#1890ff'}}
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        setEditingFactorFieldIndex(record.name);
                                        setFactorModalVisible(true);
                                    }}
                                >
                                    选择
                                </span>
                            }
                        />
                    </Form.Item>
                ),
            },
            {
                title: '操作',
                key: 'action',
                width: 60,
                align: 'center',
                render: (_: any, record: any) => (
                    <MinusCircleOutlined
                        style={{color: '#ff4d4f', cursor: 'pointer'}}
                        onClick={() => remove(record.name)}
                    />
                ),
            },
        ];

        return (
            <Table<any>
                dataSource={fields}
                columns={subColumns}
                pagination={false}
                size="small"
                bordered
                rowKey="key"
            />
        );
    };

    return (
        <>
            <CustomProTable<TableDefDetail>
                actionRef={actionRef}
                dateFormatter="string"
                rowKey="id"
                cardBordered
                headerTitle="数据表定义列表"
                toolBarRender={() => [
                    <Button key="add-btn" type="primary" icon={<PlusOutlined/>} onClick={handleAdd}>
                        新增表定义
                    </Button>,
                ]}
                request={(params: any, sorter: any, filter: any) => {
                    return pageQueryTableDef({...params, sorter, filter});
                }}
                columns={columns}
            />

            <Modal
                title={editMode === 'add' ? '新增表定义' : '编辑表定义'}
                open={modalVisible}
                onOk={handleSubmit}
                onCancel={() => setModalVisible(false)}
                destroyOnClose
                width={'90vw'}
                style={{top: 40}}
            >
                <Form form={form} layout="vertical" initialValues={{columns: []}}>
                    {/* 表基本信息 */}
                    <div style={{marginBottom: 16, padding: '12px 16px', background: '#fafafa', borderRadius: 6}}>
                        <div style={{fontWeight: 600, marginBottom: 12, fontSize: 14}}>表基本信息</div>
                        <Space style={{width: '100%'}} align="start" wrap>
                            <Form.Item
                                name="tableName"
                                label="表名"
                                rules={[{required: true, message: '请输入表名'}]}
                                style={{width: 280, marginBottom: 8}}
                            >
                                <Input placeholder="请输入表名"/>
                            </Form.Item>
                            <Form.Item
                                name="dataSource"
                                label="所属库"
                                rules={[{required: true, message: '请输入库名'}]}
                                style={{width: 280, marginBottom: 8}}
                            >
                                <Input placeholder="请输入库名"/>
                            </Form.Item>
                            <Form.Item
                                name="topic"
                                label="关联Topic"
                                style={{width: 280, marginBottom: 8}}
                            >
                                <Select
                                    allowClear
                                    placeholder="请选择关联Topic（可选）"
                                    options={availableTopics.map(t => ({label: t.name, value: t.name}))}
                                />
                            </Form.Item>
                        </Space>
                    </div>

                    {/* 字段定义区域 */}
                    <div style={{marginBottom: 8}}>
                        <Space style={{marginBottom: 8, justifyContent: 'space-between', width: '100%'}}>
                            <span style={{fontWeight: 600, fontSize: 14}}>字段定义</span>
                            <Button
                                type="primary"
                                // size="small"
                                icon={<PlusOutlined/>}
                                onClick={() => {
                                    const currentColumns = form.getFieldValue('columns') || [];
                                    form.setFieldsValue({
                                        columns: [
                                            ...currentColumns,
                                            {columnName: '', columnType: 'String', nullAble: 'Y', factorCode: ''},
                                        ],
                                    });
                                }}
                            >
                                添加字段
                            </Button>
                        </Space>
                        <Form.List name="columns">
                            {(fields) => renderColumnTable(fields, () => {
                                const currentColumns = form.getFieldValue('columns') || [];
                                form.setFieldsValue({
                                    columns: [
                                        ...currentColumns,
                                        {columnName: '', columnType: 'String', nullAble: 'Y', factorCode: ''},
                                    ],
                                });
                            }, (index) => {
                                const currentColumns = form.getFieldValue('columns') || [];
                                const updatedColumns = currentColumns.filter((_: any, i: number) => i !== index);
                                form.setFieldsValue({columns: updatedColumns});
                            })}
                        </Form.List>
                    </div>
                </Form>
            </Modal>

            <TableDefDetailView
                visible={detailVisible}
                tableName={detailTableName}
                onClose={() => setDetailVisible(false)}
            />

            <FactorSelectModal
                visible={factorModalVisible}
                onCancel={() => setFactorModalVisible(false)}
                onSelect={(resourceKey) => {
                    if (editingFactorFieldIndex !== null) {
                        const columns = form.getFieldValue('columns') || [];
                        columns[editingFactorFieldIndex] = {
                            ...(columns[editingFactorFieldIndex] || {}),
                            factorCode: resourceKey,
                        };
                        form.setFieldsValue({columns});
                    }
                    setFactorModalVisible(false);
                    setEditingFactorFieldIndex(null);
                }}
            />
        </>
    );
};

export default TableDefIndex;
