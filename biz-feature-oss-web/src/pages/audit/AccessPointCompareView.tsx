import {Card, Col, Form, Input, message, Row, Typography} from 'antd';
import React, {useEffect, useMemo, useState} from 'react';
import {AccessPointDetail, ParamItem} from '@/services/srv/accessPointApi';
import {getPointAuditDetail} from '@/services/srv/auditApi';
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
 * 接入点对比视图 — 每行一个字段，左列变更前 / 右列变更后，差异行红色高亮
 */
const AccessPointCompareView: React.FC<Props> = ({visible, id}) => {
    const [preForm] = Form.useForm();
    const [nextForm] = Form.useForm();

    const [pre, setPre] = useState<AccessPointDetail>();
    const [next, setNext] = useState<AccessPointDetail>();

    useEffect(() => {
        getPointAuditDetail(id)
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
        ;
    }, [id]);


    /** 差异字段集合 */
    const diffKeys = useMemo(() => {
        if (!pre && !next) return new Set<string>();
        const keys = new Set<string>();
        const p = pre || {} as any;
        const n = next || {} as any;
        [
            'code', 'name', 'remark', 'projectId', 'crtUser', 'crtTime',
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

    /** 渲染参数列表 */
    const renderParams = (params?: ParamItem[]) => {
        if (!params || params.length === 0) {
            return <Text type="secondary">无参数</Text>;
        }

        const renderParamItem = (item: ParamItem, level = 0) => (
            <div key={item.id} style={{paddingLeft: level * 16, marginBottom: 4}}>
                <Row gutter={8} align="middle">
                    <Col flex="1">
                        <Input disabled value={item.paramCode} placeholder="参数编码"/>
                    </Col>
                    <Col flex="1">
                        <Input disabled value={item.paramName} placeholder="参数名称"/>
                    </Col>
                    <Col flex="100px">
                        <Input disabled value={item.paramType} placeholder="类型"/>
                    </Col>
                    <Col flex="60px">
                        <Input disabled value={item.required === 1 ? '必填' : '选填'} placeholder="必填"/>
                    </Col>
                </Row>
                {item.children && item.children.length > 0 && (
                    <div style={{marginTop: 4}}>
                        {item.children.map(child => renderParamItem(child, level + 1))}
                    </div>
                )}
            </div>
        );

        return (
            <div>
                {params.map(item => renderParamItem(item))}
            </div>
        );
    };

    return (
        <Card size="small">
            {/* ===== 表头 ===== */}
            <Row gutter={16} style={{marginBottom: 8}}>
                <Col span={12}><Text strong type="secondary">变更前</Text></Col>
                <Col span={12}><Text strong type="secondary">变更后</Text></Col>
            </Row>

            {/* ===== 基本信息 ===== */}
            {renderRow('code', '接入点编码', () => <Input disabled/>)}
            {renderRow('name', '接入点名称', () => <Input disabled/>)}
            {renderRow('remark', '备注', () => <Input disabled/>)}

            {/* ===== 参数列表 ===== */}
            <Row style={{margin: '8px 0 4px'}}><Col span={24}><Text strong style={{fontSize: 13}}>参数列表</Text></Col></Row>
            <Row gutter={16}>
                <Col span={12}>
                    <Card size="small" type="inner" title={<Text type="secondary">变更前参数</Text>}>
                        {renderParams((pre as any)?.params)}
                    </Card>
                </Col>
                <Col span={12}>
                    <Card size="small" type="inner" title={<Text type="secondary">变更后参数</Text>}>
                        {renderParams((next as any)?.params)}
                    </Card>
                </Col>
            </Row>

        </Card>
    );
};

export default AccessPointCompareView;
