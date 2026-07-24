import {App, Card, Col, Form, Input, Row, Table, Tag, Typography} from 'antd';
import React, {useEffect, useMemo, useState} from 'react';
import type {ColumnsType} from 'antd/es/table';
import {getTableDefAuditDetail, TableDefAuditDetail, TableDefAuditDetailRes} from '@/services/srv/auditApi';
import type {TableColumnDetail} from '@/services/srv/tableDefApi';
import {history} from '@umijs/max';

const {Text} = Typography;

interface Props {
    visible: boolean;
    id: number;
}

const isDiff = (a: any, b: any) => String(a ?? '') !== String(b ?? '');

const DIFF_ROW_BG = '#fff2f0';
const DIFF_LABEL: React.CSSProperties = {color: '#f5222d', fontWeight: 600};

const columnSubColumns: ColumnsType<TableColumnDetail> = [
    {
        title: '列名',
        dataIndex: 'columnName',
        key: 'columnName',
        width: 140,
    },
    {
        title: '数据类型',
        dataIndex: 'columnType',
        key: 'columnType',
        width: 100,
    },
    {
        title: '是否为空',
        dataIndex: 'nullAble',
        key: 'nullAble',
        width: 80,
        align: 'center',
        render: (v: string) => v === 'Y' ? '是' : v === 'N' ? '否' : v,
    },
    {
        title: '指标编码',
        dataIndex: 'factorCode',
        key: 'factorCode',
        width: 140,
        render: (v: string) => v || '-',
    },
];

/**
 * 数据表定义 对比视图 — 左右对比模式，差异行红色高亮，含字段列表对比
 */
const TableDefCompareView: React.FC<Props> = ({visible, id}) => {
    const {message} = App.useApp();
    const [preForm] = Form.useForm();
    const [nextForm] = Form.useForm();
    const [pre, setPre] = useState<TableDefAuditDetailRes>();
    const [next, setNext] = useState<TableDefAuditDetailRes>();
    const [preColumns, setPreColumns] = useState<TableColumnDetail[]>([]);
    const [nextColumns, setNextColumns] = useState<TableColumnDetail[]>([]);

    useEffect(() => {
        getTableDefAuditDetail(id)
            .then(res => {
                if (res.code === '0000' && res.data) {
                    const data = res.data as TableDefAuditDetail;
                    setPre(data.before);
                    setNext(data.after);
                    setPreColumns(data.beforeColumns || []);
                    setNextColumns(data.afterColumns || []);
                } else {
                    message.error(res.message || '查询失败');
                    history.push('/audit/index');
                }
            })
            .catch(() => {
                message.error('查询审核详情失败');
                history.push('/audit/index');
            });
    }, [id]);

    const diffKeys = useMemo(() => {
        if (!pre && !next) return new Set<string>();
        const keys = new Set<string>();
        const p = pre || {} as any;
        const n = next || {} as any;
        ['tableName', 'dataSource', 'topic', 'crtUser'].forEach(k => {
            if (isDiff(p[k], n[k])) keys.add(k);
        });
        return keys;
    }, [pre, next]);

    /** 计算列差异：返回 before 中应标记为 diff 的行 key 集合 */
    const diffColumnNames = useMemo(() => {
        const set = new Set<string>();
        const allNames = new Set([
            ...preColumns.map(c => c.columnName),
            ...nextColumns.map(c => c.columnName),
        ]);
        allNames.forEach(name => {
            const pc = preColumns.find(c => c.columnName === name);
            const nc = nextColumns.find(c => c.columnName === name);
            if (!pc || !nc) {
                // 新增或删除的列都标红
                set.add(name);
            } else if (isDiff(pc.columnType, nc.columnType) || isDiff(pc.nullAble, nc.nullAble) || isDiff(pc.factorCode, nc.factorCode)) {
                set.add(name);
            }
        });
        return set;
    }, [preColumns, nextColumns]);

    useEffect(() => {
        if (visible && pre) preForm.setFieldsValue(pre);
    }, [visible, pre]);

    useEffect(() => {
        if (visible && next) nextForm.setFieldsValue(next);
    }, [visible, next]);

    if (!visible) return null;

    const isDiffField = (name: string) => diffKeys.has(name);
    const labelStyle = (name: string): React.CSSProperties =>
        isDiffField(name) ? DIFF_LABEL : {};

    const renderRow = (
        name: string,
        label: string,
        renderInput: () => React.ReactNode,
    ) => (
        <Row gutter={16} key={name}>
            <Col span={12}>
                <Form form={preForm} layout="horizontal" disabled>
                    <Form.Item name={name} label={label}>
                        {renderInput()}
                    </Form.Item>
                </Form>
            </Col>
            <Col span={12}>
                <Form form={nextForm} layout="horizontal" disabled>
                    <Form.Item name={name} label={<span style={labelStyle(name)}>{label}</span>}>
                        {renderInput()}
                    </Form.Item>
                </Form>
            </Col>
        </Row>
    );

    return (
        <Card size="small">
            {/* 表基本信息对比 */}
            <Row gutter={16} style={{marginBottom: 8}}>
                <Col span={12}><Text strong type="secondary">变更前</Text></Col>
                <Col span={12}><Text strong type="secondary">变更后</Text></Col>
            </Row>

            {renderRow('tableName', '表名', () => <Input disabled/>)}
            {renderRow('dataSource', '所属库', () => <Input disabled/>)}
            {renderRow('topic', '关联Topic', () => <Input disabled/>)}
            {renderRow('crtUser', '创建人', () => <Input disabled/>)}

            {/* 字段列表对比 */}
            <Row gutter={16} style={{marginTop: 20, marginBottom: 8}}>
                <Col span={12}>
                    <Text strong>字段列表</Text>
                    <Tag style={{marginLeft: 8}}>{preColumns.length} 个字段</Tag>
                </Col>
                <Col span={12}>
                    <Text strong>字段列表</Text>
                    <Tag style={{marginLeft: 8}}>{nextColumns.length} 个字段</Tag>
                </Col>
            </Row>
            <Row gutter={16}>
                <Col span={12}>
                    <Table<TableColumnDetail>
                        dataSource={preColumns}
                        columns={columnSubColumns}
                        rowKey="columnName"
                        pagination={false}
                        size="small"
                        bordered
                        onRow={(record) => ({
                            style: diffColumnNames.has(record.columnName)
                                ? {background: DIFF_ROW_BG}
                                : {},
                        })}
                    />
                </Col>
                <Col span={12}>
                    <Table<TableColumnDetail>
                        dataSource={nextColumns}
                        columns={columnSubColumns}
                        rowKey="columnName"
                        pagination={false}
                        size="small"
                        bordered
                        onRow={(record) => ({
                            style: diffColumnNames.has(record.columnName)
                                ? {background: DIFF_ROW_BG}
                                : {},
                        })}
                    />
                </Col>
            </Row>
        </Card>
    );
};

export default TableDefCompareView;
