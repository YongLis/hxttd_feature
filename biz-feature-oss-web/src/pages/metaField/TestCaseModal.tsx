import {Button, Form, Input, message, Modal, Select} from 'antd';
import React, {useEffect} from 'react';
import {TestCaseDetail, updateTestCase} from '@/services/srv/testCaseApi';

const {TextArea} = Input;

interface TestCaseModalProps {
    visible: boolean;
    record?: TestCaseDetail;
    onSuccess: () => void;
    onCancel: () => void;
}

const TestCaseModal: React.FC<TestCaseModalProps> = ({visible, record, onSuccess, onCancel}) => {
    const [form] = Form.useForm();

    useEffect(() => {
        if (visible && record) {
            form.setFieldsValue({
                metaFieldCode: record.metaFieldCode,
                caseType: record.caseType,
                bizOrderNo: record.bizOrderNo,
                caseContent: record.caseContent,
                targetValue: record.targetValue,
            });
        }
    }, [visible, record]);

    const handleOk = () => {
        form.validateFields().then(values => {
            updateTestCase({...values, id: record?.id})
                .then(res => {
                    if (res.code === '0000') {
                        message.success('更新成功');
                        form.resetFields();
                        onSuccess();
                    } else {
                        message.error(res.message || '更新失败');
                    }
                })
                .catch(e => {
                    message.error(e.message || '更新失败');
                });
        });
    };

    return (
        <Modal
            title="编辑测试用例"
            open={visible}
            onCancel={onCancel}
            footer={null}
            width={640}
        >
            <Form form={form} layout="vertical">
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
                    <Button type="primary" onClick={handleOk}>
                        确定
                    </Button>
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default TestCaseModal;
