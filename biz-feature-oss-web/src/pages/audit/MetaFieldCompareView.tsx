import {Card, Col, Form, Input, InputNumber, message, Row, Select, Typography} from 'antd';
import React, {useEffect, useMemo, useState} from 'react';
import {getMetaAuditDetail, MetaAuditDetail} from '@/services/srv/auditApi';
import {history} from '@umijs/max';

const {Text} = Typography;

interface Props {
    visible: boolean;
    id: number;
}

/** 计算两个值是否不同 */
const isDiff = (a: any, b: any) => String(a ?? '') !== String(b ?? '');

/** 差异行背景色 */
const DIFF_ROW_BG = '#fff2f0';
const DIFF_LABEL: React.CSSProperties = {color: '#f5222d', fontWeight: 600};

/**
 * 元字段对比视图 — 每行一个字段，左列变更前 / 右列变更后，差异行红色高亮
 */
const MetaFieldCompareView: React.FC<Props> = ({visible, id}) => {
    const [preForm] = Form.useForm();
    const [nextForm] = Form.useForm();
    const [loading, setLoading] = useState(false);
    const [pre, setPre] = useState<MetaAuditDetail>();
    const [next, setNext] = useState<MetaAuditDetail>();

    useEffect(() => {
        setLoading(true);
        getMetaAuditDetail(id)
            .then(res => {
                if (res.code === '0000' && res.data) {
                    setPre(res.data.before);
                    setNext(res.data.after);

                } else {
                    message.error(res.message || '查询失败');
                    history.push('/audit/index');
                }
            })
            .catch(() => {
                message.error('查询审核详情失败');
                history.push('/audit/index');
            })
            .finally(() => setLoading(false));
    }, [id]);


    /** 差异字段集合 */
    const diffKeys = useMemo(() => {
        if (!pre && !next) return new Set<string>();
        const keys = new Set<string>();
        const p = pre || {} as any;
        const n = next || {} as any;
        [
            'resourceKey', 'resourceName', 'version', 'projectId', 'language',
            'script', 'returnType', 'defaultValue', 'exceptionValue', 'timeout',
            'categoryTag', 'crtUser', 'crtTime',
        ].forEach(k => {
            if (isDiff(p[k], n[k])) keys.add(k);
        });
        return keys;
    }, [pre, next]);

    useEffect(() => {
        if (visible && pre) preForm.setFieldsValue(pre);
    }, [visible, pre]);

    useEffect(() => {
        if (visible && next) nextForm.setFieldsValue(next);
    }, [visible, next]);

    if (!visible) return null;

    const isDiffField = (name: string) => diffKeys.has(name);
    const rowStyle = (name: string): React.CSSProperties =>
        isDiffField(name) ? {background: DIFF_ROW_BG, borderRadius: 4, marginBottom: 0} : {marginBottom: 0};
    const labelStyle = (name: string): React.CSSProperties =>
        isDiffField(name) ? DIFF_LABEL : {};

    /** 渲染一行对比：label | pre输入 | next输入 */
    const renderRow = (
        name: string,
        label: string,
        renderInput: () => React.ReactNode,
    ) => (
        <Row gutter={16}>
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

    /** 渲染一行对比（TextArea） */
    const renderTextAreaRow = (name: string, label: string, rows = 6) =>
        renderRow(name, label, () => <Input.TextArea disabled rows={rows}/>);

    // 语言选项
    const LANGUAGE_OPTIONS = [
        {value: 'GROOVY', label: 'Groovy'},
    ];

    // 返回类型选项
    const RETURN_TYPE_OPTIONS = [
        {value: 'STRING', label: '字符串'},
        {value: 'INTEGER', label: '整数'},
        {value: 'DOUBLE', label: '浮点数'},
        {value: 'BOOLEAN', label: '布尔值'},
        {value: 'DATE', label: '日期'},
        {value: 'LIST', label: '列表'},
        {value: 'MAP', label: '映射'},
    ];

    return (
        <Card size="small">
            {/* ===== 表头 ===== */}
            <Row gutter={16} style={{marginBottom: 8}}>
                <Col span={12}><Text strong type="secondary">变更前</Text></Col>
                <Col span={12}><Text strong type="secondary">变更后</Text></Col>
            </Row>

            {/* ===== 基本信息 ===== */}
            {renderRow('resourceKey', '资源键', () => <Input disabled/>)}
            {renderRow('resourceName', '资源名称', () => <Input disabled/>)}
            {renderRow('version', '版本', () => <Input disabled/>)}
            {renderRow('projectId', '项目ID', () => <Input disabled/>)}
            {renderRow('categoryTag', '分类标签', () => <Input disabled/>)}
            {renderRow('crtUser', '创建人', () => <Input disabled/>)}
            {renderRow('crtTime', '创建时间', () => <Input disabled/>)}

            {/* ===== 脚本配置 ===== */}
            <Row style={{margin: '8px 0 4px'}}><Col span={24}><Text strong style={{fontSize: 13}}>脚本配置</Text></Col></Row>
            {renderRow('language', '脚本语言', () => <Select disabled options={LANGUAGE_OPTIONS}/>)}
            {renderTextAreaRow('script', '脚本内容', 8)}

            {/* ===== 返回值配置 ===== */}
            <Row style={{margin: '8px 0 4px'}}><Col span={24}><Text strong
                                                                    style={{fontSize: 13}}>返回值配置</Text></Col></Row>
            {renderRow('returnType', '返回值类型', () => <Select disabled options={RETURN_TYPE_OPTIONS}/>)}
            {renderRow('defaultValue', '默认值', () => <Input disabled/>)}
            {renderRow('exceptionValue', '异常值', () => <Input disabled/>)}
            {renderRow('timeout', '超时时间(ms)', () => <InputNumber disabled style={{width: '100%'}}/>)}

        </Card>
    );
};

export default MetaFieldCompareView;
