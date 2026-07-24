import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {App, Button, Form, Input, InputNumber, Modal, Popconfirm, Space, Tag} from 'antd';
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

const STATUS_MAP: Record<string, { color: string; text: string }> = {
    INIT: {color: 'default', text: '待创建'},
    AUDIT: {color: 'processing', text: '审核中'},
    AUDIT_PASS: {color: 'success', text: '审核通过'},
    AUDIT_REJECT: {color: 'error', text: '审核驳回'},
};

const KafkaTopicIndex: React.FC = () => {
    const {message} = App.useApp();
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
                    console.log(res)
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
            title: '状态',
            dataIndex: 'topicStatus',
            key: 'topicStatus',
            width: 100,
            align: 'center',
            render: (_, record) => {
                const info = STATUS_MAP[record.topicStatus] || {color: 'default', text: record.topicStatus};
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
                <Form form={form} layout="vertical" initialValues={{partitions: '3', replicas: '3'}}>
                    <Form.Item name="name" label="Topic名称" rules={[{required: true, message: '请输入Topic名称'}]}>
                            <Input placeholder="请输入Topic名称" addonBefore={`${localStorage.getItem('selectedProjectCode')}_T_`}/>
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
                    <Form.Item name="remark" label="备注">
                        <TextArea rows={2} placeholder="请输入备注"/>
                    </Form.Item>
                </Form>
            </Modal>
        </>
    );
};

export default KafkaTopicIndex;
