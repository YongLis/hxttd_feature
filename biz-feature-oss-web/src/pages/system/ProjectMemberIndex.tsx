import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {Button, Form, Input, message, Modal, Popconfirm, Tag} from 'antd';
import React, {useEffect, useRef, useState} from 'react';
import {DeleteOutlined, PlusOutlined} from '@ant-design/icons';
import {
    addProjectUser,
    deleteProjectUser,
    pageQueryProjectUser,
    ProjectMemberDetail,
} from '@/services/srv/projectUserApi';
import {getAllProject, ProjectDetail} from '@/services/srv/projectApi';
import {CustomPageContainer, CustomProTable} from '@/components';
import {useLocation} from '@umijs/max';
import {DeletedEnum} from '@/components/consts/AppEnum';

const ProjectMemberIndex: React.FC = () => {
    const actionRef = useRef<ActionType | null>(null);
    const [showModal, setShowModal] = useState<boolean>(false);
    const [projects, setProjects] = useState<ProjectDetail[]>([]);
    const [addForm] = Form.useForm();

    const location = useLocation() as { state?: { projectId: number } };
    const projectId = location.state?.projectId;


    useEffect(() => {
        getAllProject().then((res) => {
            if (res.code === '0000' && res.data) {
                setProjects(res.data);
            }
        });
    }, []);

    const handleDelete = (record: ProjectMemberDetail) => {
        deleteProjectUser(record.projectId!, record.userName!)
            .then((res) => {
                if (res.code === '0000') {
                    message.success('删除成功');
                    actionRef.current?.reload();
                } else {
                    message.error(res.message || '删除失败');
                }
            })
            .catch((e) => {
                message.error(e.message || '删除失败');
            });
    };

    const handleAdd = () => {
        addForm.resetFields();
        setShowModal(true);
    };

    const handleAddOk = () => {
        addForm.validateFields().then((values) => {
            addProjectUser(values)
                .then((res) => {
                    if (res.code === '0000') {
                        message.success('添加成功');
                        setShowModal(false);
                        actionRef.current?.reload();
                    } else {
                        message.error(res.message || '添加失败');
                    }
                })
                .catch((e) => {
                    message.error(e.message || '添加失败');
                });
        });
    };

    const columns: ProColumns<ProjectMemberDetail>[] = [
        {
            title: '用户账户',
            dataIndex: 'userAccount',
            key: 'userAccount',
            width: 200,
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
            width: 150,
            align: 'center',
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
            width: 120,
            align: 'center',
            render: (_, record) => (
                <Popconfirm
                    title="确定要删除该成员吗？"
                    onConfirm={() => handleDelete(record)}
                    okText="确定"
                    cancelText="取消"
                >
                    <Button type="text" danger icon={<DeleteOutlined/>}>
                        删除
                    </Button>
                </Popconfirm>
            ),
        },
    ];

    return (
        <CustomPageContainer title={false}>
            <CustomProTable<ProjectMemberDetail>
                actionRef={actionRef}
                dateFormatter="string"
                rowKey="id"
                cardBordered={true}
                search={false}
                toolBarRender={() => [
                    <Button key="add-btn" type="primary" icon={<PlusOutlined/>} onClick={handleAdd}>
                        添加成员
                    </Button>,
                ]}
                request={(params: any, sorter: any, filter: any) => {
                    return pageQueryProjectUser({...params, projectId: projectId, sorter, filter});
                }}
                columns={columns}
            />

            <Modal
                title="添加项目成员"
                open={showModal}
                onOk={handleAddOk}
                onCancel={() => setShowModal(false)}
                okText="确定"
                cancelText="取消"
                destroyOnClose
            >
                <Form form={addForm} layout="horizontal" initialValues={{projectId: projectId}}>
                    <Form.Item
                        name="projectId"
                        label="所属项目"
                        rules={[{required: true, message: '请选择项目'}]}
                    >
                        <Input disabled/>
                    </Form.Item>
                    <Form.Item
                        name="userAccount"
                        label="用户账号"
                        rules={[
                            {required: true, message: '请输入用户名称'},
                            {max: 256, message: '用户名称不能超过256个字符'},
                        ]}
                    >
                        <Input placeholder="请输入用户名称"/>
                    </Form.Item>
                </Form>
            </Modal>
        </CustomPageContainer>
    );
};

export default ProjectMemberIndex;
