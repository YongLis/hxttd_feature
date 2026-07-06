import {Button, Card, Form, Input, message, Popconfirm, Select, Space, Table, Tabs} from 'antd';
import {history, useSearchParams} from '@umijs/max';
import {PageContainer} from '@ant-design/pro-layout';
import {DeleteOutlined, PlusOutlined} from '@ant-design/icons';
import React, {useCallback, useEffect, useState} from 'react';
import {addAccessPoint, getAccessPointDetail, updateAccessPoint} from '@/services/srv/accessPointApi';
import {ObjectReturnTypeLabel} from '@/components/consts/AppEnum';

const {TextArea} = Input;

const PARAM_TYPE_OPTIONS = Object.entries(ObjectReturnTypeLabel).map(([value, label]) => ({label, value}));

/** 递归拍平树为带 parentId 的扁平列表 */
const flattenParams = (tree: any[], parentParamCode?: string | null): any[] => {
    let result: any[] = [];
    for (const node of tree) {
        const {children, _key, ...rest} = node;
        result.push({...rest, parentParamCode: parentParamCode || null});
        if (children && children.length > 0) {
            result = result.concat(flattenParams(children, node.paramCode));
        }
    }
    return result;
};

/**
 * 新增/编辑接入点页面
 */
const AccessPointAdd: React.FC = () => {
    const [searchParams] = useSearchParams();
    const editId = searchParams.get('id');
    const isEdit = !!editId;

    const [form] = Form.useForm();
    const [submitting, setSubmitting] = useState(false);
    const [loading, setLoading] = useState(false);

    // 请求入参 — 扁平可编辑列表
    const [params, setParams] = useState<any[]>([]);

    // 加载编辑数据
    useEffect(() => {
        if (isEdit && editId) {
            setLoading(true);
            getAccessPointDetail(Number(editId))
                .then((res) => {
                    if (res.code === '0000' && res.data) {
                        const detail = res.data;
                        form.setFieldsValue({
                            code: detail.code,
                            name: detail.name,
                            description: detail.description,
                            sourceType: detail.sourceType,
                            status: detail.status,
                            dataStructCode: detail.dataStructCode,
                        });
                        // 树转扁平
                        const flat = flattenParams(detail.params || []);
                        setParams(
                            flat.map((p: any, idx: number) => ({
                                ...p,
                                _key: `k_${p.id || Date.now() + idx}_${Math.random().toString(36).slice(2, 8)}`,
                            })),
                        );
                    }
                })
                .finally(() => setLoading(false));
        }
    }, [editId]);

    // 重新计算所有行的 paramLevel（依据 parentParamCode 链）
    const recalcLevels = useCallback((list: any[]): any[] => {
        const computeChain = (code: string, visited: Set<string>): number => {
            if (!code || visited.has(code)) return 1;
            visited.add(code);
            const parent = list.find((p) => p.paramCode === code);
            if (!parent?.parentParamCode) return 1;
            return computeChain(parent.parentParamCode, visited) + 1;
        };
        return list.map((p) => ({
            ...p,
            paramLevel: p.parentParamCode ? computeChain(p.parentParamCode, new Set()) : 1,
        }));
    }, []);

    // 获取父参数可选项（排除自身及所有子孙）
    const getParentOptions = useCallback(
        (excludeCode?: string): { label: string; value: string }[] => {
            if (!excludeCode) return params.filter((p) => p.paramCode).map((p) => ({
                label: `${p.paramName} (${p.paramCode})`,
                value: p.paramCode
            }));
            // 收集所有自身为根的子孙编码
            const descendants = new Set<string>();
            const collectDescendants = (code: string) => {
                params.forEach((p) => {
                    if (p.parentParamCode === code) {
                        descendants.add(p.paramCode);
                        collectDescendants(p.paramCode);
                    }
                });
            };
            collectDescendants(excludeCode);
            descendants.add(excludeCode); // 排除自身
            return params
                .filter((p) => p.paramCode && !descendants.has(p.paramCode))
                .map((p) => ({label: `${p.paramName} (${p.paramCode})`, value: p.paramCode}));
        },
        [params],
    );

    const handleSubmit = () => {
        form.validateFields().then((values) => {
            setSubmitting(true);
            // 摘除 _key 前端临时字段后提交
            const cleanParams = params.map(({_key, ...rest}) => rest);
            const payload = {...values, params: cleanParams};

            const apiCall = isEdit
                ? updateAccessPoint({...payload, id: Number(editId)})
                : addAccessPoint(payload);

            apiCall
                .then((res) => {
                    if (res.code === '0000') {
                        message.success(isEdit ? '更新成功' : '添加成功');
                        history.push('/access-point/index');
                    } else {
                        message.error(res.message || '操作失败');
                    }
                })
                .catch((e) => message.error(e.message || '操作失败'))
                .finally(() => setSubmitting(false));
        });
    };

    // --- 参数行操作 ---
    const addParam = () => {
        const newRow = {
            _key: `p_${Date.now()}`,
            paramName: '',
            paramCode: '',
            paramType: 'STRING',
            required: 0,
            defaultValue: '',
            description: '',
            sortOrder: params.length,
            parentParamCode: null,
            paramLevel: 1,
        };
        setParams((prev) => recalcLevels([...prev, newRow]));
    };

    /** 在指定父行后插入下级参数 */
    const addChildParam = (parentKey: string) => {
        const parent = params.find((p) => p._key === parentKey);
        if (!parent || !parent.paramCode) {
            message.warning('请先填写父参数的编码');
            return;
        }
        const parentLevel = parent.paramLevel || 1;
        if (parentLevel >= 3) {
            message.warning('最多支持三级参数');
            return;
        }
        const newRow = {
            _key: `p_${Date.now()}`,
            paramName: '',
            paramCode: '',
            paramType: 'STRING',
            required: 1,
            defaultValue: '',
            description: '',
            sortOrder: params.length + 1,
            parentParamCode: parent.paramCode,
            paramLevel: parentLevel + 1,
        };
        setParams((prev) => {
            const idx = prev.findIndex((p) => p._key === parentKey);
            const updated = [...prev];
            updated.splice(idx + 1, 0, newRow);
            return recalcLevels(updated);
        });
    };

    const updateParamField = (key: string, field: string, value: any) => {
        setParams((prev) => {
            const updated = prev.map((p) =>
                p._key === key ? {...p, [field]: value} : p,
            );
            // 若修改了 paramCode 或 parentParamCode，重算层级
            if (field === 'parentParamCode' || field === 'paramCode') {
                return recalcLevels(updated);
            }
            return updated;
        });
    };

    const deleteParam = (key: string) => {
        setParams((prev) => {
            const filtered = prev.filter((p) => p._key !== key);
            return recalcLevels(filtered);
        });
    };

    const levelTagColors: Record<number, string> = {1: 'blue', 2: 'green', 3: 'orange'};

    // 参数表格列定义
    const paramColumns = [
        {
            title: '参数编码',
            dataIndex: 'paramCode',
            key: 'paramCode',
            width: 120,
            render: (val: string, record: any) => (
                <Input
                    size="small"
                    value={val}
                    placeholder="参数编码"
                    onChange={(e) => updateParamField(record._key, 'paramCode', e.target.value)}
                    style={{border: '1px solid #d9d9d9'}}
                />
            ),
        },
        {
            title: '参数名',
            dataIndex: 'paramName',
            key: 'paramName',
            width: 120,
            render: (val: string, record: any) => (
                <Input
                    size="small"
                    value={val}
                    placeholder="参数名"
                    onChange={(e) => updateParamField(record._key, 'paramName', e.target.value)}
                    style={{border: '1px solid #d9d9d9'}}
                />
            ),
        },

        {
            title: '父参数',
            dataIndex: 'parentParamCode',
            key: 'parentParamCode',
            width: 150,
            render: (val: string | null, record: any) => (
                <Select
                    size="small"
                    value={val || undefined}
                    placeholder="无（根参数）"
                    allowClear
                    style={{width: '100%'}}
                    onChange={(v) => updateParamField(record._key, 'parentParamCode', v || null)}
                    options={getParentOptions(record.paramCode || undefined)}
                />
            ),
        },
        {
            title: '类型',
            dataIndex: 'paramType',
            key: 'paramType',
            width: 100,
            render: (val: string, record: any) => (
                <Select
                    size="small"
                    value={val}
                    style={{width: '100%'}}
                    onChange={(v) => updateParamField(record._key, 'paramType', v)}
                    options={PARAM_TYPE_OPTIONS}
                />
            ),
        },
        {
            title: '必填',
            dataIndex: 'required',
            key: 'required',
            width: 65,
            align: 'center' as const,
            render: (val: number, record: any) => (
                <Select
                    size="small"
                    value={val}
                    style={{width: '100%'}}
                    onChange={(v) => updateParamField(record._key, 'required', v)}
                    options={[
                        {label: '是', value: 1},
                        {label: '否', value: 0},
                    ]}
                />
            ),
        },
        {
            title: '默认值',
            dataIndex: 'defaultValue',
            key: 'defaultValue',
            width: 100,
            render: (val: string, record: any) => (
                <Input
                    size="small"
                    value={val}
                    placeholder="默认值"
                    onChange={(e) => updateParamField(record._key, 'defaultValue', e.target.value)}
                    style={{border: '1px solid #d9d9d9'}}
                />
            ),
        },
        {
            title: '描述',
            dataIndex: 'description',
            key: 'description',
            width: 120,
            render: (val: string, record: any) => (
                <Input
                    size="small"
                    value={val}
                    placeholder="描述"
                    onChange={(e) => updateParamField(record._key, 'description', e.target.value)}
                    style={{border: '1px solid #d9d9d9'}}
                />
            ),
        },
        {
            title: '排序',
            dataIndex: 'sortOrder',
            key: 'sortOrder',
            width: 65,
            align: 'center' as const,
            render: (val: number, record: any) => (
                <Input
                    size="small"
                    type="number"
                    value={val ?? ''}
                    placeholder="0"
                    onChange={(e) => updateParamField(record._key, 'sortOrder', Number(e.target.value) || 0)}
                    style={{border: '1px solid #d9d9d9', textAlign: 'center'}}
                />
            ),
        },
        {
            title: '操作',
            key: 'action',
            width: 100,
            align: 'center' as const,
            render: (_: any, record: any) => (
                <Space size={0}>
                    <Button
                        type="link"
                        size="small"
                        icon={<PlusOutlined/>}
                        title="添加下级参数"
                        disabled={(record.paramLevel || 1) >= 3 || !record.paramCode}
                        onClick={() => addChildParam(record._key)}
                    />
                    <Popconfirm title="确定删除该参数？" onConfirm={() => deleteParam(record._key)}>
                        <Button type="link" size="small" danger icon={<DeleteOutlined/>}/>
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    const tabItems = [
        {
            key: 'basic',
            label: '基本信息',
            children: (
                <Form form={form} layout="vertical" style={{maxWidth: 720}}>
                    <Form.Item name="code" label="接入点编码">
                        <Input disabled/>
                    </Form.Item>
                    <Form.Item
                        name="name"
                        label="接入点名称"
                        rules={[
                            {required: true, message: '请输入接入点名称'},
                            {max: 256, message: '不能超过256个字符'},
                        ]}
                    >
                        <Input placeholder="请输入接入点名称"/>
                    </Form.Item>
                    <Form.Item name="deleted" label="状态" initialValue={false}>
                        <Select placeholder="请选择状态">
                            <Select.Option value={false}>有效</Select.Option>
                            <Select.Option value={true}>无效</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item name="remark" label="描述">
                        <TextArea rows={4} placeholder="请输入接入点描述"/>
                    </Form.Item>
                </Form>
            ),
        },
        {
            key: 'params',
            label: '请求入参',
            children: (
                <div>
                    <Button
                        type="dashed"
                        icon={<PlusOutlined/>}
                        onClick={addParam}
                        style={{marginBottom: 12}}
                    >
                        添加参数
                    </Button>
                    <Table
                        size="small"
                        dataSource={params}
                        rowKey="_key"
                        pagination={false}
                        scroll={{x: 1100}}
                        columns={paramColumns}
                        locale={{emptyText: '暂无参数，点击上方"添加参数"开始配置'}}
                    />
                </div>
            ),
        },
    ];

    return (
        <PageContainer title={false} loading={loading}>
            <Card
                extra={
                    <Button type="primary" onClick={handleSubmit} loading={submitting}>
                        {isEdit ? '更新' : '提交'}
                    </Button>
                }
            >
                <Tabs items={tabItems}/>
            </Card>
        </PageContainer>
    );
};

export default AccessPointAdd;
