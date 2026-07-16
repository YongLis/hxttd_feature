import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {Button, Form, Input, InputNumber, message, Modal, Popconfirm, Select, Space, Tag} from 'antd';
import React, {useEffect, useRef, useState} from 'react';
import {PlusOutlined} from '@ant-design/icons';
import {CustomProTable} from '@/components';
import {
    addKafkaTopic,
    deleteKafkaTopic,
    KafkaTopicDetail,
    pageQueryKafkaTopic,
    updateKafkaTopic,
} from '@/services/srv/kafkaTopicApi';

const {TextArea} = Input;

const OFFSET_STRATEGY_OPTIONS = [
    {label: 'latest', value: 'latest'},
    {label: 'earliest', value: 'earliest'},
    {label: 'none', value: 'none'},
];

const DATA_FORMAT_OPTIONS = [
    {label: 'JSON', value: 'JSON'},
    {label: 'Avro', value: 'Avro'},
    {label: 'Protobuf', value: 'Protobuf'},
    {label: 'Custom', value: 'Custom'},
];

const STATUS_MAP: Record<string, { color: string; text: string }> = {
    init: {color: 'default', text: '待创建'},
    created: {color: 'processing', text: '已创建'},
    active: {color: 'success', text: '运行中'},
};

const KafkaTopicIndex: React.FC = () => {
    const actionRef = useRef<ActionType | null>(null);
    const [modalVisible, setModalVisible] = useState(false);
    const [editMode, setEditMode] = useState<'add' | 'edit'>('add');
    const [currentRow, setCurrentRow] = useState<KafkaTopicDetail | null>(null);
    const [form] = Form.useForm();

    useEffect(() => {
        if (modalVisible) {
            if (editMode === 'edit' && currentRow) {
                form.setFieldsValue({
                    name: currentRow.name,
                    partitions: currentRow.partitions,
                    replicas: currentRow.replicas,
                    consumerGroup: currentRow.consumerGroup,
                    offsetStrategy: currentRow.offsetStrategy,
                    dataFormat: currentRow.dataFormat,
                    serializer: currentRow.serializer,
                    valueDeserializer: currentRow.valueDeserializer,
                    remark: currentRow.remark,
                });
            } else {
                form.resetFields();
            }
        }
    }, [modalVisible, editMode, currentRow, form]);

    /** 删除 */
    const handleDelete = (id: string) => {
        deleteKafkaTopic(id)
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
    const handleEdit = (record: KafkaTopicDetail) => {
        setEditMode('edit');
        setCurrentRow(record);
        setModalVisible(true);
    };

    /** 提交 */
    const handleSubmit = () => {
        form.validateFields().then((values) => {
            const apiCall =
                editMode === 'add'
                    ? addKafkaTopic(values)
                    : updateKafkaTopic({...values, id: currentRow!.id});

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

    const columns: ProColumns<KafkaTopicDetail>[] = [
        {
            title: 'Topic名称',
            dataIndex: 'name',
            key: 'name',
            width: 180,
            ellipsis: true,
        },
        {
            title: '分区数',
            dataIndex: 'partitions',
            key: 'partitions',
            width: 80,
            align: 'center',
            search: false,
        },
        {
            title: '副本数',
            dataIndex: 'replicas',
            key: 'replicas',
            width: 80,
            align: 'center',
            search: false,
        },
        {
            title: '消费者组',
            dataIndex: 'consumerGroup',
            key: 'consumerGroup',
            width: 120,
            ellipsis: true,
            search: false,
        },
        {
            title: '数据格式',
            dataIndex: 'dataFormat',
            key: 'dataFormat',
            width: 100,
            align: 'center',
            valueEnum: {
                JSON: {text: 'JSON'},
                Avro: {text: 'Avro'},
                Protobuf: {text: 'Protobuf'},
                Custom: {text: 'Custom'},
            },
        },
        {
            title: '状态',
            dataIndex: 'status',
            key: 'status',
            width: 100,
            align: 'center',
            render: (_, record) => {
                const info = STATUS_MAP[record.status] || {color: 'default', text: record.status};
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
            sorter: true,
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
            <CustomProTable<KafkaTopicDetail>
                actionRef={actionRef}
                dateFormatter="string"
                rowKey="id"
                cardBordered
                headerTitle="Kafka Topic 列表"
                toolBarRender={() => [
                    <Button key="add-btn" type="primary" icon={<PlusOutlined/>} onClick={handleAdd}>
                        添加Topic
                    </Button>,
                ]}
                request={(params: any, sorter: any, filter: any) => {
                    return pageQueryKafkaTopic({...params, sorter, filter});
                }}
                columns={columns}
            />

            <Modal
                title={editMode === 'add' ? '添加Topic' : '编辑Topic'}
                open={modalVisible}
                onOk={handleSubmit}
                onCancel={() => setModalVisible(false)}
                destroyOnClose
                width={600}
            >
                <Form form={form} layout="vertical">
                    <Form.Item name="name" label="Topic名称" rules={[{required: true, message: '请输入Topic名称'}]}>
                        <Input placeholder="请输入Topic名称"/>
                    </Form.Item>
                    <Space style={{width: '100%'}} align="start">
                        <Form.Item name="partitions" label="分区数" rules={[{required: true, message: '请输入分区数'}]}>
                            <InputNumber min={1} placeholder="分区数" style={{width: 180}}/>
                        </Form.Item>
                        <Form.Item name="replicas" label="副本数" rules={[{required: true, message: '请输入副本数'}]}>
                            <InputNumber min={1} placeholder="副本数" style={{width: 180}}/>
                        </Form.Item>
                    </Space>
                    <Form.Item name="consumerGroup" label="消费者组">
                        <Input placeholder="请输入消费者组"/>
                    </Form.Item>
                    <Space style={{width: '100%'}} align="start">
                        <Form.Item name="offsetStrategy" label="消费偏移策略" initialValue="latest">
                            <Select options={OFFSET_STRATEGY_OPTIONS} style={{width: 180}}/>
                        </Form.Item>
                        <Form.Item name="dataFormat" label="数据格式" initialValue="JSON">
                            <Select options={DATA_FORMAT_OPTIONS} style={{width: 180}}/>
                        </Form.Item>
                    </Space>
                    <Form.Item name="serializer" label="Key序列化类">
                        <Input placeholder="如 org.apache.kafka.common.serialization.StringSerializer"/>
                    </Form.Item>
                    <Form.Item name="valueDeserializer" label="Value反序列化类">
                        <Input placeholder="如 org.apache.kafka.common.serialization.StringDeserializer"/>
                    </Form.Item>
                    <Form.Item name="remark" label="备注">
                        <TextArea rows={2} placeholder="请输入备注"/>
                    </Form.Item>
                </Form>
            </Modal>
        </>
    );
};

export default KafkaTopicIndex;
