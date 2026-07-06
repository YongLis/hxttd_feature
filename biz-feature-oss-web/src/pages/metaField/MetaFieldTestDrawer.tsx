import {Button, Col, Drawer, Form, Input, message, Row} from 'antd';
import React from 'react';
import {MetaFieldDetail, queryTestData, testMetaField} from '@/services/srv/metaFieldApi';
import {Search} from '@ant-design/pro-components';

const {TextArea} = Input;

interface MetaFieldModalProps {
    visible: boolean;
    record?: MetaFieldDetail;
    onCancel: () => void;
}

const MetaFieldTestDrawer: React.FC<MetaFieldModalProps> = ({visible, record, onCancel}) => {
    const [form] = Form.useForm();
    const [script, setScript] = React.useState('');
    const [testResult, setTestResult] = React.useState('');
    const {Search} = Input;


    const handleOk = () => {
        const param = {
            resourceKey: record?.resourceKey,
            script: record?.script,
            language: record?.language,
            jsonData: form.getFieldValue('jsonData'),
            defaultValue: record?.defaultValue,
            exceptionValue: record?.exceptionValue,
            timeout: record?.timeout
        }

        testMetaField(param)
            .then(res => {
                const data = res.data;
                if (res.code === '0000') {
                    setTestResult(data.result)
                } else {
                    setTestResult(data.errorMessage)

                }
            })
            .catch(e => {
                message.error(e.message || '测试失败');
                setTestResult(e.message)
            });
    };

    const handleCancel = () => {
        form.resetFields();
        onCancel();
    };


    const onSearch = () => {

        const param = {
            pointCode: form.getFieldValue('pointCode'),
            txnId: form.getFieldValue('txnId'),
        }

        queryTestData(param)
            .then(res => {
                const data = res.data;
                if (res.code === '0000') {
                    form.setFieldValue('jsonData', data)
                }
            })
            .catch(e => {
                message.error(e.message || '测试失败');
                setTestResult(e.message)
            });

    }


    return (
        <Drawer
            title="测试一下"
            open={visible}
            footer={null}
            width={640}
            onClose={onCancel}
        >
            <Form form={form} layout="vertical" initialValues={{
                resourceKey: record?.resourceKey,
                resourceName: record?.resourceName,
                language: record?.language,
                script: record?.script,
                returnType: record?.returnType,
                defaultValue: record?.defaultValue,
                exceptionValue: record?.exceptionValue,
                version: record?.version,
                categoryTag: record?.categoryTag || 'public',
            }}>
                <Form.Item
                    name="txnId"
                    label="交易号">
                    <Search
                        placeholder="输入交易号"
                        enterButton="Search"
                        size="large"
                        onSearch={onSearch}
                    />

                </Form.Item>

                <Form.Item
                    name="jsonData"
                    label="测试数据"
                    rules={[
                        {required: true, message: '请输入元字段名称'},
                    ]}
                >
                    <TextArea rows={5}/>
                </Form.Item>


                <Form.Item>
                    <Button type="primary" onClick={handleOk}>
                        测试一下
                    </Button>
                </Form.Item>
            </Form>

            <Row gutter={16}>
                <Col>执行结果:</Col>
            </Row>
            <Row gutter={16}>
                {testResult != undefined && <Col>{testResult}</Col>}
            </Row>
        </Drawer>
    );
};

export default MetaFieldTestDrawer;
