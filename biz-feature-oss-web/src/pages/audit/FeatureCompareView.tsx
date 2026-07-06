import {Card, Col, Form, Input, InputNumber, message, Row, Select, Typography} from 'antd';
import React, {useEffect, useMemo, useState} from 'react';
import {ColumnCondition, FeatureConfigDetail} from '@/services/srv/featureConfigApi';
import {AGGREGATE_OPTIONS, OP_OPTIONS, RETURN_TYPE_OPTIONS, TIME_WINDOW_OPTIONS} from '@/components/consts/AppCommons';
import {getFeatureAuditDetail} from '@/services/srv/auditApi';
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
 * 特征配置对比视图 — 每行一个字段，左列变更前 / 右列变更后，差异行红色高亮
 */
const FeatureCompareView: React.FC<Props> = ({visible, id}) => {
    const [preForm] = Form.useForm();
    const [nextForm] = Form.useForm();

    const [loading, setLoading] = useState(false);
    const [pre, setPre] = useState<FeatureConfigDetail>();
    const [next, setNext] = useState<FeatureConfigDetail>();


    useEffect(() => {
        setLoading(true);
        getFeatureAuditDetail(id)
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
            'resourceKey', 'resourceName', 'featureCode', 'defaultValue', 'exceptionValue', 'timeout',
            'returnType', 'valueType', 'fixValue', 'aggregateMode',
            'timeMode', 'timeUnit', 'timeWindow',
            'version', 'conditionScript', 'mainDimension', 'slaveDimension', 'value',
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


    const renderDynamicRow = (name: string, label: string, renderInput: () => React.ReactNode) => (
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
                    <Form.Item name={name} label={label}>
                        {renderInput()}
                    </Form.Item>
                </Form>
            </Col>
        </Row>
    );


    /** 只读渲染条件列表 — 参考 FeatureConfigAdd 的 Form 布局 */
    const renderConditions = (conditions?: ColumnCondition[]) => {
        if (!conditions || conditions.length === 0) {
            return <Text type="secondary">无条件</Text>;
        }

        return (
            <>
                {conditions.map((group, gIdx) => (
                    <Card
                        key={gIdx}
                        size="small"
                        type="inner"
                        title={<Text type="secondary">条件组 {gIdx + 1}</Text>}
                        style={{marginBottom: 12}}
                    >
                        {(group.columns || []).map((col, colIdx) => (
                            <Row
                                key={colIdx}
                                gutter={12}
                                align="middle"
                                style={{marginBottom: 8}}
                                wrap={false}
                            >
                                {/* 左值 — 元字段 */}
                                <Col flex="1">
                                    <Input disabled value={col.left} placeholder="左值(元字段)"/>
                                </Col>

                                {/* 操作符 */}
                                <Col flex="160px">
                                    <Select disabled value={col.op} options={OP_OPTIONS} style={{width: '100%'}}/>
                                </Col>

                                {/* 右值类型 */}
                                <Col flex="110px">
                                    <Select disabled value={col.rightType} style={{width: '100%'}}>
                                        <Select.Option value="DYNAMIC">动态值</Select.Option>
                                        <Select.Option value="FIX">固定值</Select.Option>
                                    </Select>
                                </Col>

                                {/* 右值 */}
                                <Col flex="1">
                                    {col.rightType === 'DYNAMIC' ? (
                                        <Input disabled value={col.right} placeholder="右值(元字段)"/>
                                    ) : (
                                        <Input disabled value={col.right} placeholder="右值"/>
                                    )}
                                </Col>
                            </Row>
                        ))}
                    </Card>
                ))}
            </>
        );
    };

    /** 渲染一行对比（TextArea） */
    const renderTextAreaRow = (name: string, label: string, rows = 3) =>
        renderRow(name, label, () => <Input.TextArea disabled rows={rows}/>);

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
            {renderRow('featureCode', '特征编码', () => <Input disabled/>)}
            {renderRow('version', '特征版本', () => <Input disabled/>)}
            {renderRow('returnType', '返回值类型', () => <Select disabled options={RETURN_TYPE_OPTIONS}/>)}
            {renderRow('defaultValue', '默认值', () => <Input disabled/>)}
            {renderRow('exceptionValue', '异常值', () => <Input disabled/>)}
            {renderRow('timeout', '超时时间', () => <Input disabled/>)}


            {/* ===== 前置条件 ===== */}
            <Row style={{margin: '8px 0 4px'}}><Col span={24}><Text strong style={{fontSize: 13}}>前置条件</Text></Col></Row>
            <Row gutter={16}>
                <Col span={12}>{renderConditions((pre as any)?.configForm?.conditions)}</Col>
                <Col span={12}>{renderConditions((next as any)?.configForm?.conditions)}</Col>
            </Row>

            {renderRow('mainDimension', '主维度', () => <Input disabled/>)}
            {renderRow('slaveDimension', '从维度', () => <Input disabled/>)}


            {/* ===== 特征值 ===== */}
            <Row style={{margin: '8px 0 4px'}}><Col span={24}><Text strong
                                                                    style={{fontSize: 13}}>特征值</Text></Col></Row>
            {renderRow('valueType', '值类型', () => <Input disabled/>)}
            {renderRow('fixValue', '固定值', () => <Input disabled/>)}

            {/* ===== 聚合与时间窗口 ===== */}
            <Row style={{margin: '8px 0 4px'}}><Col span={24}><Text strong style={{fontSize: 13}}>聚合 & 时间窗口</Text></Col></Row>
            {renderRow('aggregateMode', '聚合函数', () => <Select disabled options={AGGREGATE_OPTIONS}/>)}
            {renderRow('timeMode', '时间模式', () => <Select disabled options={TIME_WINDOW_OPTIONS}/>)}
            {renderRow('timeWindow', '时间窗口', () => <InputNumber disabled style={{width: '100%'}}/>)}
            {renderRow('timeUnit', '时间单位', () => <Input disabled/>)}

        </Card>
    );
};

export default FeatureCompareView;
