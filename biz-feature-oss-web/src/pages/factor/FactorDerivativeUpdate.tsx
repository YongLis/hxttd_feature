import {Button, Card, Col, Form, Input, InputNumber, message, Modal, Row, Select, Tag} from 'antd';
import {history, useLocation} from '@umijs/max';
import React, {useEffect, useRef, useState} from 'react';
import {
    DerivativeFactorModel,
    FactorDetail,
    pageQueryFactor,
    SelectedConnector,
    updateDerivativeFactor,
} from '@/services/srv/factorApi';
import {PageContainer} from '@ant-design/pro-layout';
import {RETURN_TYPE_OPTIONS} from '@/components/consts/AppCommons';
import {CustomProTable} from '@/components';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import ConnectorQueryModal from '../connector/ConnectorQueryModal';
import GroovyEditor from '@/components/common/GroovyEditor';

const FactorDerivativeUpdate: React.FC = () => {
    const {Search} = Input;
    const location = useLocation();
    const record = (location.state as any)?.record as FactorDetail | undefined;

    const [form] = Form.useForm();
    const [submitting, setSubmitting] = useState(false);
    const [showConnectorModal, setShowConnectorModal] = useState(false);
    const [selectConnector, setSelectConnector] = useState<SelectedConnector>();

    const [showFactorModal, setShowFactorModal] = useState(false);
    const [selectedFactors, setSelectedFactors] = useState<FactorDetail[]>([]);
    const [tempSelectedKeys, setTempSelectedKeys] = useState<string[]>([]);
    const [tempSelectedRows, setTempSelectedRows] = useState<FactorDetail[]>([]);

    const [conditionScript, setConditionScript] = useState<string>();
    const [script, setScript] = useState<string>();
    const factorActionRef = useRef<ActionType | null>(null);

    // Parse resourceJson and populate form on mount
    useEffect(() => {
        if (!record) {
            message.error('未获取到指标数据');
            history.push('/factor/index');
            return;
        }
        const derivative: DerivativeFactorModel | null = record.resourceJson
            ? JSON.parse(record.resourceJson)
            : null;

        form.setFieldsValue({
            id: record.id,
            factorType: record.factorType,
            resourceKey: record.resourceKey,
            resourceName: record.resourceName,
            returnType: record.returnType,
            timeout: record.timeout,
            defaultValue: record.defaultValue,
            exceptionValue: record.exceptionValue,
            connectorCode: derivative?.connectorCode || '',
            connectorType: derivative?.connectorType || '',
            params: derivative?.params || [],
            factorCodes: derivative?.factorCodes || [],
            language: derivative?.language || 'groovy',
            conditionScript: derivative?.conditionScript || '',
            script: derivative?.script || '',
        });

        if (derivative?.connectorCode) {
            setSelectConnector({
                connectorCode: derivative.connectorCode,
                connectorType: derivative.connectorType || '',
                connectorParam: (derivative.params || []).map(p => p.fieldCode),
            });
        }

        setConditionScript(derivative?.conditionScript || '');
        setScript(derivative?.script || '');

        // Pre-populate selected factor tags from factorCodes
        if (derivative?.factorCodes?.length) {
            pageQueryFactor({current: 1, pageSize: 100, resourceKeys: derivative.factorCodes})
                .then(res => {
                    if (res.data) setSelectedFactors(res.data);
                })
                .catch(() => {
                });
        }
    }, []);

    const handleSubmit = () => {
        form.validateFields().then(values => {
            setSubmitting(true);
            updateDerivativeFactor({...values, id: record?.id})
                .then(res => {
                    if (res.code === '0000') {
                        message.success('更新成功');
                        history.push('/factor/index');
                    } else {
                        message.error(res.message || '更新失败');
                    }
                })
                .catch(e => message.error(e.message || '更新失败'))
                .finally(() => setSubmitting(false));
        });
    };

    const onConnectorSelect = (selected: SelectedConnector) => {
        const existingParams: { fieldCode: string; sourceCode: string }[] = form.getFieldValue('params') || [];
        setSelectConnector(selected);
        form.setFieldValue('connectorCode', selected.connectorCode);
        form.setFieldValue('connectorType', selected.connectorType);
        const params = (selected.connectorParam || []).map(fieldCode => {
            const existing = existingParams.find(p => p.fieldCode === fieldCode);
            return {fieldCode, sourceCode: existing?.sourceCode || ''};
        });
        form.setFieldValue('params', params);
        setShowConnectorModal(false);
    };

    const handleFactorModalOpen = () => {
        setTempSelectedKeys(selectedFactors.map(f => f.resourceKey));
        setTempSelectedRows([...selectedFactors]);
        setShowFactorModal(true);
    };

    const handleFactorModalConfirm = () => {
        setSelectedFactors(tempSelectedRows);
        form.setFieldValue('factorCodes', tempSelectedRows.map(f => f.resourceKey));
        setShowFactorModal(false);
    };

    const handleRemoveFactor = (resourceKey: string) => {
        const updated = selectedFactors.filter(f => f.resourceKey !== resourceKey);
        setSelectedFactors(updated);
        form.setFieldValue('factorCodes', updated.map(f => f.resourceKey));
    };

    const factorColumns: ProColumns<FactorDetail>[] = [
        {title: '资源标识键', dataIndex: 'resourceKey', key: 'resourceKey', width: 160, ellipsis: true},
        {title: '资源名称', dataIndex: 'resourceName', key: 'resourceName', width: 160, ellipsis: true},
        {title: '指标类型', dataIndex: 'factorType', key: 'factorType', width: 100, align: 'center'},
        {title: '返回值类型', dataIndex: 'returnType', key: 'returnType', width: 100, align: 'center'},
        {
            title: '创建时间',
            dataIndex: 'crtTime',
            key: 'crtTime',
            width: 170,
            valueType: 'dateTime',
            hideInSearch: true
        },
    ];

    if (!record) return null;

    return (
        <PageContainer title={false}>
            <Card>
                <Form form={form} layout="horizontal" style={{maxWidth: 720}}
                      initialValues={{factorType: 'DERIVATIVE', language: 'groovy'}}>
                    <Form.Item name="id" hidden>
                        <Input/>
                    </Form.Item>

                    <Form.Item name="factorType" hidden label="指标类型" rules={[{required: true, message: '请选择'}]}>
                        <Input disabled/>
                    </Form.Item>

                    <Row gutter={16}>
                        <Col span={12}>
                            <Form.Item name="resourceKey" label="资源标识键"
                                       rules={[{required: true, message: '请输入'}, {max: 128}]}>
                                <Input placeholder="请输入资源标识键" disabled/>
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item name="resourceName" label="资源名称"
                                       rules={[{required: true, message: '请输入'}, {max: 256}]}>
                                <Input placeholder="请输入资源名称"/>
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={16}>
                        <Col span={12}>
                            <Form.Item name="returnType" label="返回值类型"
                                       rules={[{required: true, message: '请选择'}]}>
                                <Select placeholder="请选择返回值类型" options={RETURN_TYPE_OPTIONS}/>
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item name="timeout" label="超时时间(ms)">
                                <InputNumber style={{width: '100%'}} placeholder="请输入超时时间" min={0}/>
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={16}>
                        <Col span={12}>
                            <Form.Item name="defaultValue" label="默认值" rules={[{max: 32}]}>
                                <Input placeholder="请输入默认值"/>
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item name="exceptionValue" label="异常值" rules={[{max: 32}]}>
                                <Input placeholder="请输入异常值"/>
                            </Form.Item>
                        </Col>
                    </Row>

                    <Form.Item label="关联指标">
                        <Button type="primary" onClick={handleFactorModalOpen}>指标查询</Button>
                        {selectedFactors.length > 0 && (
                            <div style={{marginTop: 8}}>
                                {selectedFactors.map(f => (
                                    <Tag
                                        key={f.resourceKey}
                                        closable
                                        onClose={() => handleRemoveFactor(f.resourceKey)}
                                        style={{marginBottom: 4}}
                                    >
                                        {f.resourceKey} - {f.resourceName}
                                    </Tag>
                                ))}
                            </div>
                        )}
                    </Form.Item>

                    <Form.Item name="factorCodes" label="关联指标" hidden>
                        <Input/>
                    </Form.Item>

                    <Form.Item name="conditionScript" label="前置条件" layout="vertical" rules={[{max: 2048}]}>
                        <GroovyEditor val={conditionScript} onChange={v => {
                            setConditionScript(v);
                            form.setFieldValue('conditionScript', v);
                        }} editable={true} height="200px"/>
                    </Form.Item>

                    <Form.Item name="connectorCode" label="连接编码">
                        <Search
                            enterButton="选择"
                            size="large"
                            onSearch={() => setShowConnectorModal(true)}
                            readOnly
                            value={selectConnector?.connectorCode || ''}
                        />
                    </Form.Item>

                    {selectConnector?.connectorParam && selectConnector.connectorParam.length > 0 && (
                        <Form.List name="params">
                            {() => (
                                <>
                                    {selectConnector!.connectorParam!.map((fieldCode, index) => (
                                        <Row key={fieldCode} gutter={8} align="middle">
                                            <Col span={10}>
                                                <Form.Item
                                                    name={[index, 'fieldCode']}
                                                    label="参数字段"
                                                    initialValue={fieldCode}
                                                >
                                                    <Input disabled/>
                                                </Form.Item>
                                            </Col>
                                            <Col span={14}>
                                                <Form.Item
                                                    name={[index, 'sourceCode']}
                                                    label="源编码"
                                                    rules={[{required: true, message: '请输入源编码'}]}
                                                >
                                                    <Input placeholder="请输入源编码"/>
                                                </Form.Item>
                                            </Col>
                                        </Row>
                                    ))}
                                </>
                            )}
                        </Form.List>
                    )}

                    <Form.Item name="language" label="脚本语言" hidden>
                        <Input/>
                    </Form.Item>

                    <Form.Item name="script" label="计算脚本" layout="vertical" rules={[{max: 4096}]}>
                        <GroovyEditor val={script} onChange={v => {
                            setScript(v);
                            form.setFieldValue('script', v);
                        }} editable={true} height="300px"/>
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" onClick={handleSubmit} loading={submitting}>提交</Button>
                    </Form.Item>
                </Form>

                <ConnectorQueryModal
                    visible={showConnectorModal}
                    onSelect={onConnectorSelect}
                    onClose={() => setShowConnectorModal(false)}
                />

                <Modal
                    title="选择关联指标"
                    width="80%"
                    open={showFactorModal}
                    onOk={handleFactorModalConfirm}
                    onCancel={() => setShowFactorModal(false)}
                    destroyOnClose
                >
                    <CustomProTable<FactorDetail>
                        actionRef={factorActionRef}
                        headerTitle="指标列表"
                        dateFormatter="string"
                        rowKey="resourceKey"
                        cardBordered
                        toolBarRender={false}
                        request={(params: any) => pageQueryFactor({...params})}
                        columns={factorColumns}
                        rowSelection={{
                            selectedRowKeys: tempSelectedKeys,
                            onSelect: (record, selected) => {
                                if (selected) {
                                    setTempSelectedKeys(prev => [...prev, record.resourceKey]);
                                    setTempSelectedRows(prev => [...prev, record]);
                                } else {
                                    setTempSelectedKeys(prev => prev.filter(k => k !== record.resourceKey));
                                    setTempSelectedRows(prev => prev.filter(r => r.resourceKey !== record.resourceKey));
                                }
                            },
                            onSelectAll: (selected, _selectedRows, changeRows) => {
                                if (selected) {
                                    const newKeys = changeRows.map(r => r.resourceKey);
                                    setTempSelectedKeys(prev => [...prev, ...newKeys]);
                                    setTempSelectedRows(prev => [...prev, ...changeRows]);
                                } else {
                                    const removeKeys = new Set(changeRows.map(r => r.resourceKey));
                                    setTempSelectedKeys(prev => prev.filter(k => !removeKeys.has(k)));
                                    setTempSelectedRows(prev => prev.filter(r => !removeKeys.has(r.resourceKey)));
                                }
                            },
                        }}
                        tableAlertRender={({selectedRowKeys, onCleanSelected}) => (
                            <span>
                已选择 {selectedRowKeys.length} 项
                <a style={{marginLeft: 8}} onClick={onCleanSelected}>清空</a>
              </span>
                        )}
                    />
                </Modal>
            </Card>
        </PageContainer>
    );
};

export default FactorDerivativeUpdate;
