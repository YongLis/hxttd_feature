import {Card, Col, Form, Input, InputNumber, message, Row, Select, Typography} from 'antd';
import React, {useEffect, useMemo, useState} from 'react';
import {getKafkaTopicAuditDetail, KafkaTopicDetailRes} from '@/services/srv/auditApi';
import {history} from '@umijs/max';

const {Text} = Typography;

interface Props {
    visible: boolean;
    id: number;
}

const isDiff = (a: any, b: any) => String(a ?? '') !== String(b ?? '');

const DIFF_ROW_BG = '#fff2f0';
const DIFF_LABEL: React.CSSProperties = {color: '#f5222d', fontWeight: 600};

const TOPIC_STATUS_MAP: Record<string, string> = {
    INIT: '待创建',
    AUDIT: '审核中',
    AUDIT_PASS: '审核通过',
    AUDIT_REJECT: '审核驳回',
};

/**
 * Kafka Topic 对比视图 — 左右对比模式，差异行红色高亮
 */
const KafkaTopicCompareView: React.FC<Props> = ({visible, id}) => {
    const [preForm] = Form.useForm();
    const [nextForm] = Form.useForm();
    const [loading, setLoading] = useState(false);
    const [pre, setPre] = useState<KafkaTopicDetailRes>();
    const [next, setNext] = useState<KafkaTopicDetailRes>();

    useEffect(() => {
        setLoading(true);
        getKafkaTopicAuditDetail(id)
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
            'name', 'partitions', 'replicas', 'consumerGroup',
            'topicStatus', 'remark', 'crtUser',
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
            <Row gutter={16} style={{marginBottom: 8}}>
                <Col span={12}><Text strong type="secondary">变更前</Text></Col>
                <Col span={12}><Text strong type="secondary">变更后</Text></Col>
            </Row>

            {renderRow('name', 'Topic名称', () => <Input disabled/>)}
            {renderRow('partitions', '分区数', () => <InputNumber disabled style={{width: '100%'}}/>)}
            {renderRow('replicas', '副本数', () => <InputNumber disabled style={{width: '100%'}}/>)}
            {renderRow('consumerGroup', '消费者组', () => <Input disabled/>)}
            {renderRow('topicStatus', '状态', () => (
                <Select disabled
                        options={Object.entries(TOPIC_STATUS_MAP).map(([value, label]) => ({value, label}))}/>
            ))}
            {renderRow('remark', '备注', () => <Input disabled/>)}
            {renderRow('crtUser', '创建人', () => <Input disabled/>)}

        </Card>
    );
};

export default KafkaTopicCompareView;
