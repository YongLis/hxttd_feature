import {Button, Card, Form, Input, message, Select} from 'antd';
import {history} from '@umijs/max';
import React from 'react';
import {addTestCase} from '@/services/srv/testCaseApi';
import {PageContainer} from '@ant-design/pro-layout';
import {ArrowLeftOutlined} from '@ant-design/icons';

const {TextArea} = Input;

/**
 * 新增测试用例页面
 */
const TestCaseAdd: React.FC = () => {
    const [form] = Form.useForm();
    const [submitting, setSubmitting] = React.useState(false);

    const handleSubmit = () => {
        form.validateFields().then(values => {
            setSubmitting(true);
            addTestCase(values)
                .then(res => {
                    if (res.code === '0000') {
                        message.success('添加成功');
                        history.push('/meta-field/test-case');
                    } else {
                        message.error(res.message || '添加失败');
                    }
                })
                .catch(e => {
                    message.error(e.message || '添加失败');
                })
                .finally(() => {
                    setSubmitting(false);
                });
        });
    };

    return (
        <PageContainer title={false}>
            <Card
                title={
                    <Button type="text" icon={<ArrowLeftOutlined/>}
                            onClick={() => history.push('/meta-field/test-case')}>
                        返回列表
                    </Button>
                }
            >
                <Form form={form} layout="vertical" style={{maxWidth: 720}}>
                    <Form.Item
                        name="metaFieldCode"
                        label="元字段编码"
                        rules={[{required: true, message: '请输入元字段编码'}]}
                    >
                        <Input placeholder="请输入元字段编码"/>
                    </Form.Item>
                    <Form.Item
                        name="caseType"
                        label="案例类型"
                        rules={[{required: true, message: '请选择案例类型'}]}
                    >
                        <Select placeholder="请选择案例类型">
                            <Select.Option value="NORMAL">普通用例</Select.Option>
                            <Select.Option value="ERROR">异常用例</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item
                        name="bizOrderNo"
                        label="交易号"
                        rules={[{required: true, message: '请输入交易号'}]}
                    >
                        <Input placeholder="请输入交易号"/>
                    </Form.Item>
                    <Form.Item name="caseContent" label="请求数据">
                        <TextArea rows={6} placeholder="请输入请求数据"/>
                    </Form.Item>
                    <Form.Item name="targetValue" label="期望值">
                        <Input placeholder="请输入期望值"/>
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" onClick={handleSubmit} loading={submitting}>
                            提交
                        </Button>
                    </Form.Item>
                </Form>
            </Card>
        </PageContainer>
    );
};

export default TestCaseAdd;
