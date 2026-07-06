import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {Button, message, Popconfirm, Space, Tag} from 'antd';
import React, {useRef} from 'react';
import {PlusOutlined} from '@ant-design/icons';
import {history} from '@umijs/max';
import {AccessPointDetail, deleteAccessPoint, pageQueryAccessPoint} from '@/services/srv/accessPointApi';
import {CustomProTable} from '@/components';

const AccessPointIndex: React.FC = () => {
    const actionRef = useRef<ActionType | null>(null);

    /** 删除 */
    const handleDelete = (id: number) => {
        deleteAccessPoint(id)
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

    /** 新增 */
    const handleAdd = () => {
        history.push('/access-point/add');
    };

    /** 编辑 */
    const handleEdit = (record: AccessPointDetail) => {
        history.push('/access-point/edit?id=' + record.id);
    };

    const handleDownLoadApi = (record: AccessPointDetail) => {
        history.push('/access-point/api?id=' + record.id);
    };


    const columns: ProColumns<AccessPointDetail>[] = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
            width: '5%',
            align: 'center',
            search: false,
        },
        {
            title: '接入点编码',
            dataIndex: 'code',
            key: 'code',
            width: '10%',
            ellipsis: true,

        },
        {
            title: '接入点名称',
            dataIndex: 'name',
            key: 'name',
            width: '15%',
            ellipsis: true,
        },
        {
            title: '状态',
            dataIndex: 'status',
            key: 'status',
            width: '10%',
            align: 'center',
            render: (_, record) => (
                <Tag color={record.deleted ? 'red' : 'green'}>
                    {record.deleted ? '无效' : '有效'}
                </Tag>
            ),
        },
        {
            title: '创建人',
            dataIndex: 'crtUser',
            key: 'crtUser',
            width: '10%',
            align: 'center',
        },
        {
            title: '创建时间',
            dataIndex: 'crtTime',
            key: 'crtTime',
            width: '15%',
            valueType: 'dateTime',
            align: 'center',
            sorter: true,
            search: false,
        },
        {
            title: '操作',
            key: 'action',
            width: '20%',
            align: 'center',
            fixed: 'right',
            search: false,
            render: (_, record) => (
                <Space>
                    <Button size="small" type="link" onClick={() => handleEdit(record)}>
                        编辑
                    </Button>
                    <Popconfirm
                        title="确定要删除该接入点吗？"
                        onConfirm={() => handleDelete(record.id!)}
                        okText="确定"
                        cancelText="取消"
                    >
                        <Button size="small" type="link" danger>
                            删除
                        </Button>
                    </Popconfirm>

                    <Button size="small" type="link" onClick={() => handleDownLoadApi(record)}>
                        文档
                    </Button>
                </Space>
            ),
        },
    ];

    return (
        <CustomProTable<AccessPointDetail>
            actionRef={actionRef}
            dateFormatter="string"
            rowKey="id"
            cardBordered={true}
            search={false}
            style={{overflow: 'auto'}}
            toolBarRender={() => [
                <Button
                    key="add-btn"
                    type="primary"
                    icon={<PlusOutlined/>}
                    onClick={handleAdd}
                >
                    添加接入点
                </Button>,
            ]}
            request={(params: any, sorter: any, filter: any) => {
                return pageQueryAccessPoint({...params, sorter, filter});
            }}
            columns={columns}
        />
    );
};

export default AccessPointIndex;
