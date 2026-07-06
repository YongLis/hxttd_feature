import {Button, Card, Col, Form, Input, InputNumber, message, Row, Select, Typography} from 'antd';
import {history} from '@umijs/max';
import React, {useEffect, useState} from 'react';
import {ArrowLeftOutlined, DeleteOutlined, PlusOutlined} from '@ant-design/icons';
import {addFeatureConfig} from '@/services/srv/featureConfigApi';
import {getAllMetaField, MetaFieldOption} from '@/services/srv/metaFieldApi';
import {PageContainer} from '@ant-design/pro-layout';
import {
    AGGREGATE_OPTIONS,
    OP_OPTIONS,
    RETURN_TYPE_OPTIONS,
    TIME_UNIT_OPTIONS,
    TIME_WINDOW_OPTIONS
} from '@/components/consts/AppCommons';

const {Text} = Typography;

/**
 * 新增特征配置页面 — 动态条件表单 + left/op/rightType/right 联动
 */
const FeatureConfigAdd: React.FC = () => {
    const [form] = Form.useForm();
    const [submitting, setSubmitting] = useState(false);
    const [valueType, setValueType] = useState('DYNAMIC');
    const [timeMode, setTimeMode] = useState('TTL');
    const [metaFieldOptions, setMetaFieldOptions] = useState<{ value: string; label: string }[]>([]);

    /** 初始化：获取元字段列表 */
    useEffect(() => {
        getAllMetaField().then(res => {
            if (res.code === '0000' && res.data) {
                setMetaFieldOptions(
                    res.data.map((item: MetaFieldOption) => ({
                        value: item.resourceKey,
                        label: `${item.resourceName} (${item.resourceKey})`,
                    })),
                );
            }
        }).catch(() => {
        });
    }, []);

    const handleSubmit = () => {
        form.validateFields().then(values => {
            // setSubmitting(true);
            addFeatureConfig(values)
                .then(res => {
                    if (res.code === '0000') {
                        message.success('添加成功');
                        history.push('/feature-config/index');
                    } else {
                        console.log(res.message)
                        message.error(res.message);
                        setSubmitting(false);
                    }
                })
                .catch(e => message.error(e.message || '添加失败'))
                .finally(() => setSubmitting(false));
        });
    };

    return (
        <PageContainer title={false}>
            <Card
                title={
                    <Button type="text" icon={<ArrowLeftOutlined/>}
                            onClick={() => history.push('/feature-config/index')}>
                        返回列表
                    </Button>
                }
            >
                <Form form={form} layout="horizontal" labelCol={{flex: '90px'}} style={{maxWidth: 1080}}
                      initialValues={{timeout: 100, valueType: 'DYNAMIC', timeMode: 'TTL'}}
                >
                    {/* ========== 基本信息 ========== */}
                    <Row gutter={16}>
                        <Col span={12}>
                            <Form.Item name="resourceKey" label="资源键"
                                       rules={[{required: true, message: '请输入'}, {max: 128}]}>
                                <Input placeholder="请输入资源键"
                                       prefix={`${localStorage.getItem('selectedProjectCode')}_V_`}/>
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
                        <Col span={8}>
                            <Form.Item name="featureCode" label="特征编码">
                                <Input disabled/>
                            </Form.Item>
                        </Col>
                        <Col span={8}>
                            <Form.Item name="version" label="版本号" rules={[{max: 32}]}>
                                <Input placeholder="请输入版本号" disabled/>
                            </Form.Item>
                        </Col>
                        <Col span={8}>
                            <Form.Item name="returnType" label="返回值类型"
                                       rules={[{required: true, message: '请选择'}]}>
                                <Select placeholder="请选择返回值类型" options={RETURN_TYPE_OPTIONS}>
                                </Select>
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={16}>
                        <Col span={8}>
                            <Form.Item name="defaultValue" label="默认值">
                                <Input placeholder="请输入默认值"/>
                            </Form.Item>
                        </Col>
                        <Col span={8}>
                            <Form.Item name="exceptionValue" label="异常值">
                                <Input placeholder="请输入异常值"/>
                            </Form.Item>
                        </Col>
                        <Col span={8}>
                            <Form.Item name="timeout" label="超时时间">
                                <Input placeholder="请输入超时时间"/>
                            </Form.Item>
                        </Col>
                    </Row>


                    {/* ========== 前置条件（动态表单） ========== */}
                    <Row gutter={16} style={{marginTop: 16, marginBottom: 8}}>
                        <Col span={24}>
                            <Text strong style={{fontSize: 14}}>前置条件</Text>
                        </Col>
                    </Row>

                    <Form.List name="conditions">
                        {(groupFields, {add: addGroup, remove: removeGroup}) => (
                            <>
                                {groupFields.map(({key: groupKey, name: groupIndex}) => (
                                    <Card
                                        key={groupKey}
                                        size="small"
                                        type="inner"
                                        title={<Text type="secondary">条件组 {groupIndex + 1}</Text>}
                                        extra={
                                            <Button
                                                type="link"
                                                danger
                                                size="small"
                                                icon={<DeleteOutlined/>}
                                                onClick={() => removeGroup(groupIndex)}
                                            >
                                                删除组
                                            </Button>
                                        }
                                        style={{marginBottom: 12}}
                                    >
                                        <Form.List name={[groupIndex, 'columns']}>
                                            {(colFields, {add: addCol, remove: removeCol}) => (
                                                <>
                                                    {colFields.map(({key: colKey, name: colIndex}) => (
                                                        <Row
                                                            key={colKey}
                                                            gutter={12}
                                                            align="middle"
                                                            style={{marginBottom: 8}}
                                                            wrap={false}
                                                        >
                                                            {/* 左值 — 元字段 Select */}
                                                            <Col flex="1">
                                                                <Form.Item
                                                                    name={[colIndex, 'left']}
                                                                    rules={[{required: true, message: '请选择左值'}]}
                                                                    style={{marginBottom: 0}}
                                                                >
                                                                    <Select
                                                                        placeholder="左值(元字段)"
                                                                        showSearch
                                                                        filterOption={(input, option) =>
                                                                            (option?.label as string)?.toLowerCase().includes(input.toLowerCase())
                                                                        }
                                                                        options={metaFieldOptions}
                                                                        style={{width: '100%'}}
                                                                    />
                                                                </Form.Item>
                                                            </Col>

                                                            {/* 操作符 — 本地枚举 */}
                                                            <Col flex="160px">
                                                                <Form.Item
                                                                    name={[colIndex, 'op']}
                                                                    rules={[{required: true, message: '请选择'}]}
                                                                    style={{marginBottom: 0}}
                                                                >
                                                                    <Select placeholder="操作符" options={OP_OPTIONS}
                                                                            style={{width: '100%'}}/>
                                                                </Form.Item>
                                                            </Col>

                                                            {/* 右值类型 — 动态值/固定值 */}
                                                            <Col flex="110px">
                                                                <Form.Item
                                                                    name={[colIndex, 'rightType']}
                                                                    initialValue="FIX"
                                                                    style={{marginBottom: 0}}
                                                                >
                                                                    <Select style={{width: '100%'}}>
                                                                        <Select.Option
                                                                            value="DYNAMIC">动态值</Select.Option>
                                                                        <Select.Option
                                                                            value="FIX">固定值</Select.Option>
                                                                    </Select>
                                                                </Form.Item>
                                                            </Col>

                                                            {/* 右值 — 根据 rightType 切换 */}
                                                            <Col flex="1">
                                                                <Form.Item
                                                                    noStyle
                                                                    shouldUpdate={(prev, cur) => {
                                                                        const prevRt = prev?.conditions?.[groupIndex]?.columns?.[colIndex]?.rightType;
                                                                        const curRt = cur?.conditions?.[groupIndex]?.columns?.[colIndex]?.rightType;
                                                                        return prevRt !== curRt;
                                                                    }}
                                                                >
                                                                    {({getFieldValue}) => {
                                                                        const rightType = getFieldValue(['conditions', groupIndex, 'columns', colIndex, 'rightType']);
                                                                        if (rightType === 'DYNAMIC') {
                                                                            return (
                                                                                <Form.Item
                                                                                    name={[colIndex, 'right']}
                                                                                    rules={[{
                                                                                        required: true,
                                                                                        message: '请选择右值'
                                                                                    }]}
                                                                                    style={{marginBottom: 0}}
                                                                                >
                                                                                    <Select
                                                                                        placeholder="右值(元字段)"
                                                                                        showSearch
                                                                                        filterOption={(input, option) =>
                                                                                            (option?.label as string)?.toLowerCase().includes(input.toLowerCase())
                                                                                        }
                                                                                        options={metaFieldOptions}
                                                                                        style={{width: '100%'}}
                                                                                    />
                                                                                </Form.Item>
                                                                            );
                                                                        }
                                                                        return (
                                                                            <Form.Item
                                                                                name={[colIndex, 'right']}
                                                                                style={{marginBottom: 0}}
                                                                            >
                                                                                <Input placeholder="右值"
                                                                                       style={{width: '100%'}}/>
                                                                            </Form.Item>
                                                                        );
                                                                    }}
                                                                </Form.Item>
                                                            </Col>

                                                            {/* 删除行 */}
                                                            <Col flex="40px">
                                                                <Button
                                                                    type="link"
                                                                    danger
                                                                    size="small"
                                                                    icon={<DeleteOutlined/>}
                                                                    onClick={() => removeCol(colIndex)}
                                                                />
                                                            </Col>
                                                        </Row>
                                                    ))}
                                                    <Button
                                                        type="dashed"
                                                        onClick={() => addCol({
                                                            left: '',
                                                            op: 'equals',
                                                            rightType: 'FIX',
                                                            right: ''
                                                        })}
                                                        block
                                                        icon={<PlusOutlined/>}
                                                        style={{marginTop: 8}}
                                                    >
                                                        添加条件
                                                    </Button>
                                                </>
                                            )}
                                        </Form.List>
                                    </Card>
                                ))}
                                <Button
                                    type="dashed"
                                    onClick={() => addGroup({
                                        columns: [{
                                            left: '',
                                            op: 'equals',
                                            rightType: 'FIX',
                                            right: ''
                                        }]
                                    })}
                                    block
                                    icon={<PlusOutlined/>}
                                >
                                    增加条件组
                                </Button>
                            </>
                        )}
                    </Form.List>

                    {/* ========== 其他配置区 ========== */}
                    <Row gutter={16} style={{marginTop: 24, marginBottom: 8}}>
                        <Col span={24}>
                            <Text strong style={{fontSize: 14}}>主维度</Text>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={8}>
                            <Form.Item name="mainDimension" label="主维度">
                                <Select placeholder="请选择主维度" allowClear options={metaFieldOptions}/>
                            </Form.Item>
                        </Col>
                    </Row>


                    <Row gutter={16} style={{marginBottom: 16}}>
                        <Col span={24}>
                            <Text strong style={{fontSize: 14}}>从维度</Text>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={8}>
                            <Form.Item name="slaveDimension" label="从维度">
                                <Select placeholder="请选择从维度" allowClear options={metaFieldOptions}/>
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={16} style={{marginTop: 24, marginBottom: 8}}>
                        <Col span={24}>
                            <Text strong style={{fontSize: 14}}>特征值</Text>
                        </Col>
                    </Row>

                    <Row gutter={16}>
                        <Col span={8}>
                            <Form.Item name="valueType" label="值类型" rules={[{required: true, message: '请选择'}]}>
                                <Select placeholder="请选择值类型" onChange={(val) => setValueType(val)}>
                                    <Select.Option value="FIX">固定值</Select.Option>
                                    <Select.Option value="DYNAMIC">动态值</Select.Option>
                                </Select>
                            </Form.Item>
                        </Col>
                        <Col span={8}>
                            {valueType === 'FIX' && (
                                <Form.Item name="fixValue" label="固定值" rules={[{max: 512}]}>
                                    <Input placeholder="请输入固定值"/>
                                </Form.Item>
                            )}
                            {valueType === 'DYNAMIC' && (
                                <Form.Item name="valueDimension" label="特征值">
                                    <Select placeholder="请选择特征值" allowClear options={metaFieldOptions}/>
                                </Form.Item>
                            )}
                        </Col>
                    </Row>


                    <Row gutter={16} style={{marginBottom: 16}}>
                        <Col span={24}>
                            <Text strong style={{fontSize: 14}}>时间窗口</Text>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={8}>
                            <Form.Item name="timeMode" label="时间模式">
                                <Select placeholder="请选择时间模式" allowClear options={TIME_WINDOW_OPTIONS}
                                        onChange={(val) => setTimeMode(val)}/>
                            </Form.Item>
                        </Col>
                        {timeMode === 'TTL' &&
                            <>
                                <Col span={8}>
                                    <Form.Item name="timeWindow" label="时间窗口">
                                        <InputNumber style={{width: '100%'}} placeholder="请输入时间窗口" min={1}/>
                                    </Form.Item>
                                </Col>
                                <Col span={8}>
                                    <Form.Item name="timeUnit" label="时间单位">
                                        <Select options={TIME_UNIT_OPTIONS}/>
                                    </Form.Item>
                                </Col>

                            </>
                        }

                    </Row>


                    <Row gutter={16} style={{marginBottom: 16}}>
                        <Col span={24}>
                            <Text strong style={{fontSize: 14}}>聚合函数</Text>
                        </Col>
                    </Row>

                    <Row gutter={16}>
                        <Col span={8}>
                            <Form.Item name="aggregateMode" label="聚合函数">
                                <Select placeholder="请选择聚合函数" allowClear options={AGGREGATE_OPTIONS}/>
                            </Form.Item>
                        </Col>
                    </Row>
                    <Form.Item>
                        <Button type="primary" onClick={handleSubmit} loading={submitting}>提交</Button>
                    </Form.Item>
                </Form>
            </Card>
        </PageContainer>
    );
};

export default FeatureConfigAdd;
