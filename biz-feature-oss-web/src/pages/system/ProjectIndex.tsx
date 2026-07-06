import type {ActionType, ProColumns,} from '@ant-design/pro-components';
import {Button, message, Popconfirm, Space} from 'antd';
import React, {useRef, useState} from 'react';
import {deleteProject, pageQueryProject, ProjectDetail} from '@/services/srv/projectApi';
import ProjectModal from './ProjectModal';
import {CustomPageContainer, CustomProTable} from '@/components';
import {history} from '@umijs/max';

const ProjectIndex: React.FC = () => {
    const actionRef = useRef<ActionType | null>(null);
    const [showModal, setShowModal] = useState<boolean>(false);
    const [modalMode, setModalMode] = useState<'add' | 'edit'>('add');
    const [currentRow, setCurrentRow] = useState<ProjectDetail>();

    // 删除项目
    const handleDelete = (id: number) => {
        deleteProject(id)
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

    // 编辑项目
    const handleEdit = (record: ProjectDetail) => {
        setCurrentRow(record);
        setModalMode('edit');
        setShowModal(true);
    };


    const toProjectMemberView = (projectId: number) => {
        history.push(`/system/project-member`, {projectId: projectId});
    };

    const columns: ProColumns<ProjectDetail>[] = [
        {
            title: '项目代码',
            dataIndex: 'projectCode',
            key: 'projectCode',
            width: 80,
            align: 'center'
        },
        {
            title: '项目名称',
            dataIndex: 'name',
            key: 'name',
            width: 100,
            ellipsis: true,
        },
        {
            title: '项目成员',
            dataIndex: 'userCount',
            key: 'userCount',
            width: 100,
            render: (_, record) => {
                return <Button type='link'
                               onClick={() => toProjectMemberView(record.id)}>{record.userCount > 0 ? record.userCount : 0}</Button>
            }
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
            width: 150,
            valueType: 'dateTime',
            align: 'center',
            sorter: true,
        },
        {
            title: '操作',
            key: 'action',
            width: 150,
            align: 'center',
            render: (_, record) => (
                <Space size="small">
                    <Button
                        type="link"
                        onClick={() => handleEdit(record)}
                    >
                        编辑
                    </Button>
                    <Popconfirm
                        title="确定要删除该项目吗？"
                        onConfirm={() => handleDelete(record.id!)}
                        okText="确定"
                        cancelText="取消"
                    >
                        <Button
                            type="link"
                            danger
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
            <CustomProTable<ProjectDetail>
                actionRef={actionRef}
                dateFormatter='string'
                rowKey="id"
                cardBordered={true}
                toolBarRender={() => [
                    <Button
                        key='project-add-btn'
                        type='primary'
                        onClick={() => {
                            setModalMode('add');
                            setCurrentRow(undefined);
                            setShowModal(true);
                        }}
                    >
                        添加项目
                    </Button>
                ]}
                request={(params: any, sorter: any, filter: any) => {
                    return pageQueryProject({...params, sorter, filter});
                }}
                columns={columns}
            />

            {/* 新增/编辑项目 Modal */}
            {
                showModal &&
                <ProjectModal
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

export default ProjectIndex;
