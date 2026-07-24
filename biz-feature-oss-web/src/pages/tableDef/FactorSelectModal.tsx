import {Button, Form, Input, Modal, Select, Space, Table, Tag} from 'antd';
import React, {useEffect, useState} from 'react';
import {pageQueryFactor, type FactorDetail} from '@/services/srv/factorApi';

interface Props {
    visible: boolean;
    onCancel: () => void;
    onSelect: (resourceKey: string, resourceName: string) => void;
}

/**
 * 指标选择弹窗 — 根据条件查询指标列表，选中后回填指标编码
 */
const FactorSelectModal: React.FC<Props> = ({visible, onCancel, onSelect}) => {
    const [queryForm] = Form.useForm();
    const [dataList, setDataList] = useState<FactorDetail[]>([]);
    const [total, setTotal] = useState(0);
    const [current, setCurrent] = useState(1);
    const [pageSize] = useState(10);
    const [loading, setLoading] = useState(false);
    const fetchData = (page: number) => {
        setLoading(true);
        const params = queryForm.getFieldsValue();
        pageQueryFactor({...params, current: page, pageSize})
            .then((res: any) => {
                if (res.code === '0000' && res.data) {
                    setDataList(res.data);
                    setTotal(res.total || 0);
                    setCurrent(res.current || page);
                }
            })
            .catch(() => {
            })
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        if (visible) {
            queryForm.resetFields();
            setDataList([]);
            setTotal(0);
            setCurrent(1);
            fetchData(1);
        }
    }, [visible]);

    const handleSearch = () => {
        setCurrent(1);
        fetchData(1);
    };

    const columns = [
        {title: '指标编码', dataIndex: 'resourceKey', width: 180},
        {title: '指标名称', dataIndex: 'resourceName', width: 200, ellipsis: true},
        {
            title: '指标类型',
            dataIndex: 'factorType',
            width: 100,
            render: (v: string) => {
                const map: Record<string, string> = {META: '元字段', DERIVATIVE: '衍生', FEATURE: '实时特征'};
                return map[v] || v;
            },
        },
        {title: '返回类型', dataIndex: 'returnType', width: 100},
        {title: '创建人', dataIndex: 'crtUser', width: 100},
    ];

    return (
        <Modal
            title="选择指标"
            open={visible}
            onCancel={onCancel}
            width={'90vw'}
            style={{top: 40}}
            footer={
                <Space>
                    <Button onClick={onCancel}>取消</Button>
                </Space>
            }
            destroyOnClose
        >
            <Form form={queryForm} layout="inline" style={{marginBottom: 16}}>
                <Form.Item name="resourceKey" label="指标编码">
                    <Input placeholder="模糊搜索" allowClear/>
                </Form.Item>
                <Form.Item name="resourceName" label="指标名称">
                    <Input placeholder="模糊搜索" allowClear/>
                </Form.Item>
                <Form.Item name="factorType" label="指标类型">
                    <Select
                        allowClear
                        placeholder="全部"
                        style={{width: 130}}
                        options={[
                            {label: '元字段', value: 'META'},
                            {label: '衍生', value: 'DERIVATIVE'},
                            {label: '实时特征', value: 'FEATURE'},
                        ]}
                    />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" onClick={handleSearch}>
                        查询
                    </Button>
                </Form.Item>
            </Form>

            <Table<FactorDetail>
                dataSource={dataList}
                columns={columns}
                rowKey="id"
                loading={loading}
                size="small"
                bordered
                pagination={{
                    current,
                    pageSize,
                    total,
                    showTotal: (t) => `共 ${t} 条`,
                    onChange: (p) => fetchData(p),
                }}
                rowSelection={{
                    type: 'radio',
                    onChange: (_, selectedRows) => {
                        if (selectedRows.length > 0) {
                            const row = selectedRows[0];
                            onSelect(row.resourceKey, row.resourceName || '');
                        }
                    },
                }}
            />
        </Modal>
    );
};

export default FactorSelectModal;
