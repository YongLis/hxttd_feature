import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {Button, message, Popconfirm, Space, Spin, Tag} from 'antd';
import React, {useRef, useState} from 'react';
import {history} from '@umijs/max';
import {deleteMetaField, MetaFieldDetail, pageQueryMetaField} from '@/services/srv/metaFieldApi';
import {CustomPageContainer, CustomProTable, LineageGraph} from '@/components';
import {getDictCodeOptions} from '@/services/srv/dictCodeApi';

const MetaFieldIndex: React.FC = () => {
    const actionRef = useRef<ActionType | null>(null);
    const [showDepModal, setShowDepModal] = useState<boolean>(false);
    const [currentRow, setCurrentRow] = useState<MetaFieldDetail>();
    const [categoryOptions, setCategoryOptions] = React.useState<{ label: string; value: string }[]>([]);
    const [resourceKey, setResourceKey] = useState<string>('');

    // 加载分类标签字典
    React.useEffect(() => {
        getDictCodeOptions('ttd', 'meta_tag').then((opts) => {
            setCategoryOptions(opts);
        });
    }, []);


    /** 删除 */
    const handleDelete = (id: number) => {
        deleteMetaField(id)
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

    /** 新增 - 跳转到新增页面 */
    const handleAdd = () => {
        history.push('/meta-field/add');
    };

    const handleDetail = (record: MetaFieldDetail) => {
        history.push(`/meta-field/detail`, {id: record.id});
    };

    /** 编辑 */
    const handleEdit = (record: MetaFieldDetail) => {
        setCurrentRow(record);
        history.push('/meta-field/update', {id: record.id});
    };

    const returnTypeColorMap: Record<string, string> = {
        BOOLEAN: 'blue',
        STRING: 'green',
        NUMBER: 'orange',
    };

    const showDep = (resourceKey: string) => {
        setShowDepModal(true);
        setResourceKey(resourceKey);
    }


    const columns: ProColumns<MetaFieldDetail>[] = [
        {
            title: '字段编码',
            dataIndex: 'resourceKey',
            key: 'resourceKey',
            width: '10%',
            ellipsis: true,
            copyable: true,
        },
        {
            title: '字段名称',
            dataIndex: 'resourceName',
            key: 'resourceName',
            width: '10%',
            ellipsis: true,
            copyable: true,
        },
        {
            title: '值类型',
            dataIndex: 'returnType',
            key: 'returnType',
            width: '10%',
            align: 'center',
            search: false,
            render: (_, record) => (
                <Tag color={returnTypeColorMap[record.returnType] || 'default'}>{record.returnType}</Tag>
            ),
        },
        {
            title: '版本',
            dataIndex: 'version',
            key: 'version',
            width: '10%',
            align: 'center',
            ellipsis: true,
            search: false,
        },
        {
            title: '脚本语言',
            dataIndex: 'language',
            key: 'language',
            width: '10%',
            align: 'center',
            search: false,
            render: (_, record) => (
                <Tag color={record.language === 'aviator' ? 'purple' : 'cyan'}>{record.language}</Tag>
            ),
        },
        {
            title: '分类标签',
            dataIndex: 'categoryTag',
            key: 'categoryTag',
            width: '10%',
            align: 'center',
            valueType: 'select',
            valueEnum: categoryOptions.reduce((acc, cur) => {
                acc[cur.value] = cur.label;
                return acc;
            }, {} as Record<string, string>),
            render: (_, record) => (
                <Tag color={record.categoryTag === 'public' ? 'green' : 'blue'}>{record.categoryTag || 'public'}</Tag>
            ),
        },
        // {
        //   title: '创建人',
        //   dataIndex: 'crtUser',
        //   key: 'crtUser',
        //   width: '10%',
        //   align: 'center',
        // },
        // {
        //   title: '创建时间',
        //   dataIndex: 'crtTime',
        //   key: 'crtTime',
        //   width: '10%',
        //   valueType: 'dateTime',
        //   align: 'center',
        //   sorter: true,
        // },
        {
            title: '操作',
            key: 'action',
            width: '15%',
            align: 'center',
            fixed: 'right',
            search: false,
            render: (_, record) => (
                <Space size="small">
                    <Button type="link" size="small" key={`dep_${record.id}`}
                            onClick={() => showDep(record.resourceKey)}>
                        血缘
                    </Button>

                    <Button type="link" size="small" key='detail' onClick={() => handleDetail(record)}>
                        详情
                    </Button>
                    <Button type="link" size="small" key='edit' onClick={() => handleEdit(record)}>
                        编辑
                    </Button>
                    <Popconfirm
                        title="确定要删除该元字段吗？"
                        onConfirm={() => handleDelete(record.id!)}
                        okText="确定"
                        cancelText="取消"
                    >
                        <Button type="link" size="small" key='delete' danger>
                            删除
                        </Button>
                    </Popconfirm>

                </Space>
            ),
        },
    ];

    return (
        <CustomPageContainer title={false}>
            <CustomProTable<MetaFieldDetail>
                actionRef={actionRef}
                dateFormatter="string"
                rowKey="id"
                cardBordered={true}
                search={{
                    labelWidth: 'auto',
                }}
                style={{
                    maxWidth: '1500px',
                    overflowX: 'auto',
                }}
                optionsRender={() => [
                    <Button
                        key="add-btn"
                        type="primary"
                        onClick={handleAdd}
                    >
                        添加元字段
                    </Button>,
                ]}
                request={(params: any, sorter: any, filter: any) => {
                    return pageQueryMetaField({...params, sorter, filter});
                }}
                columns={columns}
            />

            {
                showDepModal &&
                <React.Suspense fallback={<Spin tip="加载血缘组件..."
                                                style={{display: 'flex', justifyContent: 'center', padding: 80}}>
                    <div/>
                </Spin>}>
                    <LineageGraph
                        resourceKey={resourceKey}
                        resourceType={'META'}
                        visible={showDepModal}
                        onCancel={() => setShowDepModal(false)}/>
                </React.Suspense>
            }


        </CustomPageContainer>
    );
};

export default MetaFieldIndex;


