import {Button, Card, Col, Form, Input, InputNumber, message, Row, Select, Spin, Typography} from 'antd';
import {history, useSearchParams} from '@umijs/max';
import React, {useEffect, useState} from 'react';
import {DeleteOutlined, PlusOutlined} from '@ant-design/icons';
import {addConnector, getConnectorDetail, updateConnector} from '@/services/srv/connectorApi';
import {getDictCodeOptions} from '@/services/srv/dictCodeApi';
import {RETURN_TYPE_OPTIONS} from '@/components/consts/AppCommons';
import {CustomPageContainer} from '@/components';
import GroovyEditor from '@/components/common/GroovyEditor';

const {Text} = Typography;
const {TextArea} = Input;

interface ConnectorEsFormValues {
    resourceKey: string;
    resourceName: string;
    version?: string;
    projectId?: number;
    connectorType: string;
    defaultValue?: string;
    exceptionValue?: string;
    returnType?: string;
    timeout?: number;
    dataSourceName?: string;
    sql?: string;
    fields?: { fieldCode: string; fieldName: string; fieldType: string }[];
    condition?: string;
}

/**
 * ES连接器新增/编辑页面
 * - 动态变量表格 + 前置条件脚本 + 数据源下拉 + DSL录入
 */
const ConnectorEs: React.FC = () => {
    const [form] = Form.useForm<ConnectorEsFormValues>();
    const [searchParams] = useSearchParams();
    const editId = searchParams.get('id');
    const isEdit = !!editId;

    const [submitting, setSubmitting] = useState(false);
    const [loading, setLoading] = useState(false);
    const [condition, setCondition] = useState('');
    const [esDataSourceOptions, setEsDataSourceOptions] = useState<{ label: string; value: string }[]>([]);

    /** 加载字典选项 */
    useEffect(() => {
        getDictCodeOptions('ttd', 'esDataSource').then(setEsDataSourceOptions);
    }, []);

    /** 编辑模式加载详情 */
    useEffect(() => {
        if (editId) {
            setLoading(true);
            getConnectorDetail(Number(editId))
                .then(res => {
                    if (res.code === '0000' && res.data) {
                        const d = res.data;
                        form.setFieldsValue({
                            resourceKey: d.resourceKey,
                            resourceName: d.resourceName,
                            version: d.version,
                            projectId: d.projectId,
                            connectorType: d.connectorType,
                            defaultValue: d.defaultValue,
                            exceptionValue: d.exceptionValue,
                            timeout: d.timeout,
                        });
                        if (d.resourceJson) {
                            try {
                                const json = JSON.parse(d.resourceJson);
                                setCondition(json.condition || '');
                                form.setFieldsValue({
                                    dataSourceName: json.dataSourceName,
                                    sql: json.sql,
                                    fields: json.fields || [],
                                    returnType: json.returnType,
                                });
                            } catch { /* ignore */
                            }
                        }
                    }
                })
                .finally(() => setLoading(false));
        }
    }, [editId]);

    const handleSubmit = () => {
        form.validateFields().then(values => {
            setSubmitting(true);
            const submitData = {
                ...values,
                condition: condition,
                projectId: values.projectId || Number(localStorage.getItem('selectedProjectId')) || 0,
            };
            const apiCall = isEdit
                ? updateConnector({...submitData, id: Number(editId)})
                : addConnector(submitData);
            apiCall
                .then(res => {
                    if (res.code === '0000') {
                        message.success(isEdit ? '更新成功' : '添加成功');
                        history.push('/connector/index');
                    } else {
                        message.error((res as any).message || '操作失败');
                    }
                })
                .catch(e => message.error(e.message || '操作失败'))
                .finally(() => setSubmitting(false));
        });
    };

    if (loading) {
        return (
            <CustomPageContainer title={false}>
                <Card><Spin/></Card>
            </CustomPageContainer>
        );
    }

    return (
        <CustomPageContainer title={false}>
            <Card>
                <Form form={form} layout="horizontal" style={{maxWidth: 900}}
                      initialValues={{
                          connectorType: 'ES',
                          timeout: 200,
                          defaultValue: '0',
                          exceptionValue: '-999999',
                          projectId: Number(localStorage.getItem('selectedProjectId'))
                      }}
                >
                    {/* 基础信息 */}
                    <Row gutter={16}>
                        <Col span={12}>
                            <Form.Item name="resourceKey" label="资源标识键"
                                       rules={[{required: true, message: '请输入'}]}>
                                <Input placeholder="请输入资源标识键"
                                       prefix={`${localStorage.getItem('selectedProjectCode')}_C_`} disabled={isEdit}/>
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item name="resourceName" label="资源名称"
                                       rules={[{required: true, message: '请输入'}]}>
                                <Input placeholder="请输入资源名称"/>
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <Form.Item name="version" label="版本号">
                                <Input disabled/>
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item name="connectorType" label="连接类型">
                                <Input disabled/>
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <Form.Item name="timeout" label="超时时间(ms)">
                                <InputNumber style={{width: '100%'}} placeholder="超时时间" min={0}/>
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item name="returnType" label="返回值类型">
                                <Select placeholder="请选择返回值类型" options={RETURN_TYPE_OPTIONS}/>
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <Form.Item name="defaultValue" label="默认值">
                                <Input placeholder="请输入默认值"/>
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item name="exceptionValue" label="异常值">
                                <Input placeholder="请输入异常值"/>
                            </Form.Item>
                        </Col>
                    </Row>

                    {/* 变量参数 */}
                    <Card
                        title="变量参数"
                        size="small"
                        style={{marginBottom: 16}}
                        extra={
                            <Button
                                type="dashed"
                                size="small"
                                icon={<PlusOutlined/>}
                                onClick={() => {
                                    const fields = form.getFieldValue('fields') || [];
                                    form.setFieldsValue({
                                        fields: [...fields, {
                                            fieldCode: '',
                                            fieldName: '',
                                            fieldType: 'STRING'
                                        }]
                                    });
                                }}
                            >
                                添加变量
                            </Button>
                        }
                    >
                        <Form.List name="fields">
                            {(fields, {remove}) => (
                                <>
                                    {fields.length === 0 && (
                                        <Text type="secondary">暂无变量，点击上方按钮添加</Text>
                                    )}
                                    {fields.map((field, index) => (
                                        <Row key={field.key} gutter={8} align="middle" style={{marginBottom: 8}}>
                                            <Col span={2}><Text type="secondary">{index + 1}</Text></Col>
                                            <Col span={7}>
                                                <Form.Item
                                                    {...field}
                                                    name={[field.name, 'fieldCode']}
                                                    rules={[{required: true, message: '变量编码'}]}
                                                    style={{marginBottom: 0}}
                                                >
                                                    <Input placeholder="变量编码"/>
                                                </Form.Item>
                                            </Col>
                                            <Col span={7}>
                                                <Form.Item
                                                    {...field}
                                                    name={[field.name, 'fieldName']}
                                                    rules={[{required: true, message: '变量名称'}]}
                                                    style={{marginBottom: 0}}
                                                >
                                                    <Input placeholder="变量名称"/>
                                                </Form.Item>
                                            </Col>
                                            <Col span={5}>
                                                <Form.Item
                                                    {...field}
                                                    name={[field.name, 'fieldType']}
                                                    initialValue="STRING"
                                                    style={{marginBottom: 0}}
                                                >
                                                    <Select placeholder="类型" options={RETURN_TYPE_OPTIONS}/>
                                                </Form.Item>
                                            </Col>
                                            <Col span={3}>
                                                <Button
                                                    type="text"
                                                    danger
                                                    icon={<DeleteOutlined/>}
                                                    onClick={() => remove(field.name)}
                                                />
                                            </Col>
                                        </Row>
                                    ))}
                                </>
                            )}
                        </Form.List>
                    </Card>

                    {/* 前置条件脚本 */}
                    <Card title="前置条件脚本" size="small" style={{marginBottom: 16}}>
                        <Form.Item name="condition" style={{marginBottom: 0}}>
                            <GroovyEditor height="200px" editable={true} val={condition} onChange={setCondition}/>
                        </Form.Item>
                    </Card>

                    {/* 数据源 + DSL */}
                    <Card title="数据源 & DSL" size="small">
                        <Form.Item name="dataSourceName" label="数据源"
                                   rules={[{required: true, message: '请选择数据源'}]} layout='vertical'>
                            <Select
                                placeholder="请选择数据源"
                                options={esDataSourceOptions}
                                showSearch
                                filterOption={(input, option) =>
                                    (option?.label as string)?.toLowerCase().includes(input.toLowerCase())
                                }
                            />
                        </Form.Item>
                        <Form.Item name="sql" label="DSL语句" rules={[{required: true, message: '请输入DSL'}]}
                                   layout='vertical'>
                            <TextArea
                                rows={12}
                                placeholder="输入 DSL 查询语句，使用 #{fieldCode} 引用变量"
                                style={{fontFamily: 'monospace', backgroundColor: '#1b1b1bff', color: '#e6e6e6'}}
                            />
                        </Form.Item>
                    </Card>

                    {/* 提交按钮 */}
                    <Form.Item style={{marginTop: 24}}>
                        <Button type="primary" onClick={handleSubmit} loading={submitting}>
                            {isEdit ? '保存修改' : '提交'}
                        </Button>
                    </Form.Item>
                </Form>
            </Card>
        </CustomPageContainer>
    );
};

export default ConnectorEs;