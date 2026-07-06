import {Card, Col, Form, Input, InputNumber, message, Row, Select, Tag, Typography} from 'antd';
import React, {useEffect, useMemo, useState} from 'react';
import {ConnectorParamField, DerivativeFactorDetailRes, getDerivativeFactorAuditDetail} from '@/services/srv/auditApi';
import {RETURN_TYPE_OPTIONS} from '@/components/consts/AppCommons';
import {history} from '@umijs/max';

const {Text} = Typography;

interface Props {
    visible: boolean;
    id: number;
}

const isDiff = (a: any, b: any) => {
    if (Array.isArray(a) && Array.isArray(b)) return JSON.stringify(a) !== JSON.stringify(b);
    return String(a ?? '') !== String(b ?? '');
};

const DIFF_ROW_BG = '#fff2f0';
const DIFF_LABEL: React.CSSProperties = {color: '#f5222d', fontWeight: 600};

const factorTypeLabelMap: Record<string, string> = {
    META: '基础指标',
    DERIVATIVE: '衍生指标',
    FEATURE: '特征指标',
};

const languageOptions = [
    {value: 'aviator', label: 'Aviator'},
    {value: 'groovy', label: 'Groovy'},
];

const renderParams = (params?: ConnectorParamField[]) => {
    if (!params || params.length === 0) return <Text type="secondary">无</Text>;
    return (
        <div style={{display: 'flex', flexWrap: 'wrap', gap: 4}}>
            {params.map((p, i) => (
                <Tag key={i} color="blue">{p.fieldCode} → {p.sourceCode || '(空)'}</Tag>
            ))}
        </div>
    );
};

const renderFactorCodes = (codes?: string[]) => {
    if (!codes || codes.length === 0) return <Text type="secondary">无</Text>;
    return (
        <div style={{display: 'flex', flexWrap: 'wrap', gap: 4}}>
            {codes.map((c, i) => <Tag key={i}>{c}</Tag>)}
        </div>
    );
};

const DerivativeFactorCompareView: React.FC<Props> = ({visible, id}) => {
    const [preForm] = Form.useForm();
    const [nextForm] = Form.useForm();
    const [loading, setLoading] = useState(false);
    const [pre, setPre] = useState<DerivativeFactorDetailRes>();
    const [next, setNext] = useState<DerivativeFactorDetailRes>();

    useEffect(() => {
        setLoading(true);
        getDerivativeFactorAuditDetail(id)
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

    const diffKeys = useMemo(() => {
        if (!pre && !next) return new Set<string>();
        const keys = new Set<string>();
        const p = pre || {} as any;
        const n = next || {} as any;
        [
            'resourceKey', 'resourceName', 'version', 'projectId', 'factorType',
            'returnType', 'defaultValue', 'exceptionValue', 'timeout',
            'connectorType', 'connectorCode', 'params', 'factorCodes',
            'language', 'conditionScript', 'script',
            'crtUser', 'crtTime',
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
    const labelStyle = (name: string): React.CSSProperties =>
        isDiffField(name) ? DIFF_LABEL : {};

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

    const renderCustomRow = (
        label: string,
        diffName: string,
        renderPre: () => React.ReactNode,
        renderNext: () => React.ReactNode,
    ) => (
        <Row gutter={16} style={{marginBottom: 4}}>
            <Col span={12}>
                <div style={{
                    padding: '4px 8px',
                    background: isDiffField(diffName) ? DIFF_ROW_BG : undefined,
                    borderRadius: 4,
                    minHeight: 32
                }}>
                    <Text type="secondary" style={{fontSize: 12}}>{label}</Text>
                    <div style={{marginTop: 2}}>{renderPre()}</div>
                </div>
            </Col>
            <Col span={12}>
                <div style={{
                    padding: '4px 8px',
                    background: isDiffField(diffName) ? DIFF_ROW_BG : undefined,
                    borderRadius: 4,
                    minHeight: 32
                }}>
                    <Text style={{
                        fontSize: 12,
                        color: isDiffField(diffName) ? '#f5222d' : undefined,
                        fontWeight: isDiffField(diffName) ? 600 : undefined
                    }}>{label}</Text>
                    <div style={{marginTop: 2}}>{renderNext()}</div>
                </div>
            </Col>
        </Row>
    );

    const renderTextAreaRow = (name: string, label: string, rows = 4) =>
        renderRow(name, label, () => <Input.TextArea disabled rows={rows}/>);

    return (
        <Card size="small">
            <Row gutter={16} style={{marginBottom: 8}}>
                <Col span={12}><Text strong type="secondary">变更前</Text></Col>
                <Col span={12}><Text strong type="secondary">变更后</Text></Col>
            </Row>

            {renderRow('resourceKey', '资源标识键', () => <Input disabled/>)}
            {renderRow('resourceName', '资源名称', () => <Input disabled/>)}
            {renderRow('version', '版本', () => <Input disabled/>)}
            {renderRow('factorType', '指标类型', () => <Select disabled
                                                               options={Object.entries(factorTypeLabelMap).map(([value, label]) => ({
                                                                   value,
                                                                   label
                                                               }))}/>)}
            {renderRow('projectId', '项目ID', () => <Input disabled/>)}

            {renderRow('returnType', '返回值类型', () => <Select disabled options={RETURN_TYPE_OPTIONS}/>)}
            {renderRow('defaultValue', '默认值', () => <Input disabled/>)}
            {renderRow('exceptionValue', '异常值', () => <Input disabled/>)}
            {renderRow('timeout', '超时时间(ms)', () => <InputNumber disabled style={{width: '100%'}}/>)}

            {renderRow('connectorType', '连接器类型', () => <Input disabled/>)}
            {renderRow('connectorCode', '连接编码', () => <Input disabled/>)}

            {renderCustomRow('连接参数', 'params',
                () => renderParams(pre?.params),
                () => renderParams(next?.params),
            )}

            {renderCustomRow('关联指标编码', 'factorCodes',
                () => renderFactorCodes(pre?.factorCodes),
                () => renderFactorCodes(next?.factorCodes),
            )}

            {renderRow('language', '脚本语言', () => <Select disabled options={languageOptions}/>)}
            {renderTextAreaRow('conditionScript', '前置脚本', 4)}
            {renderTextAreaRow('script', '计算脚本', 6)}

        </Card>
    );
};

export default DerivativeFactorCompareView;
