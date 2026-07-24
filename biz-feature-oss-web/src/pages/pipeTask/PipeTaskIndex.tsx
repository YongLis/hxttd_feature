import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {App, Button, Form, Input, InputNumber, Modal, Popconfirm, Select, Space, Tag} from 'antd';
import React, {useEffect, useRef, useState} from 'react';
import {PlusOutlined} from '@ant-design/icons';
import {CustomProTable} from '@/components';
import {
    addPipeTask,
    deletePipeTask,
    pageQueryPipeTask,
    type PipeTaskDetail,
    updatePipeTask,
} from '@/services/srv/pipeTaskApi';
import {getAllAccessPoint, type AccessPointOption} from '@/services/srv/accessPointApi';
import {getAllTableDef, type TableDefOption} from '@/services/srv/tableDefApi';
import {getAvailableTopics, type KafkaTopicItem} from '@/services/srv/kafkaTopicApi';

const TASK_STATUS_MAP: Record<string, { color: string; text: string }> = {
    '0': {color: 'success', text: '开启'},
    '1': {color: 'default', text: '未开启'},
};

const PipeTaskIndex: React.FC = () => {
    const {message} = App.useApp();
    const actionRef = useRef<ActionType | null>(null);
    const [modalVisible, setModalVisible] = useState(false);
    const [editMode, setEditMode] = useState<'add' | 'edit'>('add');
    const [currentRow, setCurrentRow] = useState<PipeTaskDetail | null>(null);
    const [form] = Form.useForm();
    const [accessPointOptions, setAccessPointOptions] = useState<AccessPointOption[]>([]);
    const [tableDefOptions, setTableDefOptions] = useState<TableDefOption[]>([]);
    const [kafkaTopicOptions, setKafkaTopicOptions] = useState<KafkaTopicItem[]>([]);

    useEffect(() => {
        if (modalVisible) {
            // 加载接入点列表供Select选择
            getAllAccessPoint().then((res: any) => {
                if (res.code === '0000' && res.data) {
                    setAccessPointOptions(res.data);
                }
            }).catch(() => {});
            // 加载表定义列表供Select选择
            getAllTableDef().then((res: any) => {
                if (res.code === '0000' && res.data) {
                    setTableDefOptions(res.data);
                }
            }).catch(() => {});
            // 加载Kafka Topic列表供Select选择
            getAvailableTopics().then((res: any) => {
                if (res.code === '0000' && res.data) {
                    setKafkaTopicOptions(res.data);
                }
            }).catch(() => {});

            if (editMode === 'edit' && currentRow) {
                form.setFieldsValue({
                    pointCode: currentRow.pointCode,
                    taskCode: currentRow.taskCode,
                    taskName: currentRow.taskName,
                    tableName: currentRow.tableName,
                    kafkaTopic: currentRow.kafkaTopic,
                    taskStatus: currentRow.taskStatus,
                    taskPriority: currentRow.taskPriority,
                });
            } else {
                form.resetFields();
            }
        }
    }, [modalVisible, editMode, currentRow, form]);

    /** 删除 */
    const handleDelete = (id: string) => {
        deletePipeTask(id)
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
    const handleEdit = (record: PipeTaskDetail) => {
        setEditMode('edit');
        setCurrentRow(record);
        setModalVisible(true);
    };

    /** 提交 */
    const handleSubmit = () => {
        form.validateFields().then((values) => {
            const apiCall =
                editMode === 'add'
                    ? addPipeTask(values)
                    : updatePipeTask({...values, id: currentRow!.id});

            apiCall
                .then((res: any) => {
                    if (res.code === '0000') {
                        message.success(editMode === 'add' ? '添加成功，待审核' : '更新已提交审核');
                        setModalVisible(false);
                        actionRef.current?.reload();
                    } else {
                        message.error(res.message || '操作失败');
                    }
                })
                .catch((e: any) => message.error(e.message || '操作失败'));
        });
    };

    const columns: ProColumns<PipeTaskDetail>[] = [
        {
            title: '任务编码',
            dataIndex: 'taskCode',
            key: 'taskCode',
            width: 180,
            ellipsis: true,
            search:false
        },
        {
            title: '任务名称',
            dataIndex: 'taskName',
            key: 'taskName',
            width: 200,
            ellipsis: true,
            search:true
        },
        {
            title: '接入点',
            dataIndex: 'pointCode',
            key: 'pointCode',
            width: 160,
            ellipsis: true,
            search: true,
        },
        {
            title: '表名',
            dataIndex: 'tableName',
            key: 'tableName',
            width: 160,
            ellipsis: true,
            search: true,
        },
        {
            title: 'Kafka Topic',
            dataIndex: 'kafkaTopic',
            key: 'kafkaTopic',
            width: 180,
            ellipsis: true,
            search: true,
        },
        {
            title: '优先级',
            dataIndex: 'taskPriority',
            key: 'taskPriority',
            width: 80,
            align: 'center',
            search: false,
            render: (_, record) => {
                const p = record.taskPriority;
                if (p == null) return '-';
                const color = p <= 30 ? 'red' : p <= 60 ? 'orange' : 'default';
                return <Tag color={color}>{p}</Tag>;
            },
        },
        {
            title: '状态',
            dataIndex: 'taskStatus',
            key: 'taskStatus',
            width: 100,
            align: 'center',
            search: false,
            render: (_, record) => {
                const info = TASK_STATUS_MAP[record.taskStatus || ''] || {color: 'default', text: record.taskStatus || '-'};
                return <Tag color={info.color}>{info.text}</Tag>;
            },
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
            <CustomProTable<PipeTaskDetail>
                actionRef={actionRef}
                dateFormatter="string"
                rowKey="id"
                cardBordered
                headerTitle="管道任务列表"
                toolBarRender={() => [
                    <Button key="add-btn" type="primary" icon={<PlusOutlined/>} onClick={handleAdd}>
                        新增管道任务
                    </Button>,
                ]}
                request={(params: any) => {
                    return pageQueryPipeTask(params);
                }}
                columns={columns}
            />

            <Modal
                title={editMode === 'add' ? '新增管道任务' : '编辑管道任务'}
                open={modalVisible}
                onOk={handleSubmit}
                onCancel={() => setModalVisible(false)}
                destroyOnClose
                width={'90vw'}
                style={{top: 40}}
            >
                <Form form={form} layout="vertical" initialValues={{taskStatus: '1', taskPriority: 50}}>
                    <Space style={{width: '100%'}} align="start" wrap>
                        <Form.Item
                            name="pointCode"
                            label="接入点"
                            style={{width: 280}}
                            rules={[{required: true, message: '请选择接入点'}]}
                        >
                            <Select
                                showSearch
                                allowClear
                                placeholder="请选择接入点"
                                optionFilterProp="label"
                                options={accessPointOptions.map(t => ({label: t.name, value: t.code}))}
                            />
                        </Form.Item>
                        <Form.Item
                            name="tableName"
                            label="表名"
                            style={{width: 280}}
                        >
                            <Select
                                showSearch
                                allowClear
                                placeholder="请选择表名"
                                optionFilterProp="label"
                                options={tableDefOptions.map(t => ({label: `${t.tableName} (${t.dataSource || '-'})`, value: t.tableName}))}
                            />
                        </Form.Item>
                    </Space>
                    <Space style={{width: '100%'}} align="start" wrap>
                        <Form.Item
                            name="taskCode"
                            label="任务编码"
                            // rules={[{required: true, message: '请输入任务编码'}]}
                            style={{width: 280}}
                        >
                            <Input placeholder="请输入任务编码" disabled />
                        </Form.Item>
                        <Form.Item
                            name="taskName"
                            label="任务名称"
                            style={{width: 280}}
                            rules={[{required: true}]}
                        >
                            <Input placeholder="请输入任务名称"/>
                        </Form.Item>
                    </Space>
                    
                    <Space style={{width: '100%'}} align="start" wrap>
                        <Form.Item
                            name="kafkaTopic"
                            label="Kafka Topic"
                            style={{width: 280}}
                        >
                            <Select
                                showSearch
                                allowClear
                                placeholder="请选择Kafka Topic"
                                optionFilterProp="label"
                                options={kafkaTopicOptions.map(t => ({label: t.name, value: t.name}))}
                            />
                        </Form.Item>
                        <Form.Item
                            name="taskPriority"
                            label="优先级"
                            style={{width: 280}}
                        >
                            <InputNumber
                                min={1}
                                max={100}
                                placeholder="1-100，1最高"
                                style={{width: '100%'}}
                            />
                        </Form.Item>
                    </Space>
                    <Form.Item
                        name="taskStatus"
                        label="状态"
                        style={{width: 280}}
                    >
                        <Select
                            options={[
                                {label: '开启', value: '0'},
                                {label: '未开启', value: '1'},
                            ]}
                        />
                    </Form.Item>
                </Form>
            </Modal>
        </>
    );
};

export default PipeTaskIndex;
