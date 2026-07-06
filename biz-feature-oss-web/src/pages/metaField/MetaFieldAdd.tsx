import {Button, Card, Form, Input, message, Select, Space} from 'antd';
import {history} from '@umijs/max';
import React from 'react';
import {addMetaField} from '@/services/srv/metaFieldApi';
import {PageContainer} from '@ant-design/pro-layout';
import {getObjectReturnDefaultValue, ObjectReturnTypeEnum, ObjectReturnTypeLabel} from '@/components/consts/AppEnum';
import GroovyEditor from '@/components/common/GroovyEditor';
import {getDictCodeOptions} from '@/services/srv/dictCodeApi';
import MetaFieldTestDrawer from './MetaFieldTestDrawer';

const {TextArea} = Input;

/**
 * 新增元字段页面
 */
const MetaFieldAdd: React.FC = () => {
    const [form] = Form.useForm();
    const [submitting, setSubmitting] = React.useState(false);
    const [script, setScript] = React.useState('');
    const [categoryOptions, setCategoryOptions] = React.useState<{ label: string; value: string }[]>([]);
    const [showTest, setShowTest] = React.useState(false);
    const [record, setRecord] = React.useState();
    // 加载分类标签字典
    React.useEffect(() => {
        getDictCodeOptions('ttd', 'meta_tag').then((opts) => {
            setCategoryOptions(opts);
        });
    }, []);

    const OBJECT_RETURN_TYPE = Object.values(ObjectReturnTypeEnum).map((code) => ({
        value: code,
        label: ObjectReturnTypeLabel[code],
    }));


    const handleSubmit = () => {
        form.validateFields().then(values => {
            setSubmitting(true);
            const params = {
                ...values,
            };
            addMetaField(params)
                .then(res => {
                    if (res.code === '0000') {
                        message.success('添加成功');
                        history.push('/meta-field/index');
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

    const handleTest = () => {
        setShowTest(true)
        setRecord(form.getFieldsValue);
    };


    const onObectChange = (value: any) => {
        form.setFieldsValue({'defaultValue': getObjectReturnDefaultValue(value)});
        form.setFieldsValue({'exceptionValue': getObjectReturnDefaultValue(value)});
    };

    const handleBack = () => {
        history.push('/meta-field/index');
    };

    return (
        <PageContainer
            title={false}
        >
            <Card
                title={false}>
                <Form form={form} layout="horizontal" style={{maxWidth: 720}}
                      initialValues={{'language': 'groovy'}}
                >
                    <Form.Item
                        name="resourceKey"
                        label="元字段编码"
                        rules={[
                            {required: true, message: '请输入元字段编码'},
                            {max: 128, message: '不能超过100个字符'}
                        ]}
                    >
                        <Input placeholder="请输入元字段编码"
                               prefix={`${localStorage.getItem('selectedProjectCode')}_M_`}/>
                    </Form.Item>

                    <Form.Item
                        name="resourceName"
                        label="元字段名称"
                        rules={[
                            {required: true, message: '请输入元字段名称'},
                            {max: 200, message: '不能超过200个字符'}
                        ]}
                    >
                        <Input placeholder="请输入元字段名称"/>
                    </Form.Item>

                    <Form.Item
                        name="version"
                        label="版本号"
                    >
                        <Input disabled/>
                    </Form.Item>
                    <Form.Item
                        name="returnType"
                        label="返回值类型"
                        rules={[{required: true, message: '请选择返回值类型'}]}
                    >
                        <Select placeholder="请选择返回值类型" options={OBJECT_RETURN_TYPE} onChange={onObectChange}/>
                    </Form.Item>

                    <Form.Item
                        name="defaultValue"
                        label="默认值"
                        rules={[{max: 100, message: '不能超过100个字符'}]}
                    >
                        <Input placeholder="请输入默认值"/>
                    </Form.Item>

                    <Form.Item
                        name="exceptionValue"
                        label="异常值"
                        rules={[{max: 100, message: '不能超过100个字符'}]}
                    >
                        <Input placeholder="请输入异常值"/>
                    </Form.Item>

                    <Form.Item
                        name="categoryTag"
                        label="分类标签"
                        initialValue="public"
                        rules={[{max: 64, message: '不能超过64个字符'}]}
                    >
                        <Select placeholder="请选择分类标签" options={categoryOptions}/>
                    </Form.Item>
                    <Form.Item
                        name="language"
                        label="语言"
                        rules={[{required: true, message: '请选择脚本语言'}]}
                    >
                        <Select placeholder="请选择脚本语言">
                            {/* <Select.Option value="aviator">Aviator</Select.Option> */}
                            <Select.Option value="groovy">Groovy</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item
                        name="script"
                        label="脚本"
                        rules={[{required: true, message: '请输入脚本'}]}
                    >
                        <GroovyEditor val={script} editable={true}
                                      onChange={(value: React.SetStateAction<string>) => setScript(value)}/>
                    </Form.Item>

                    <Form.Item>
                        <Space>
                            <Button type="primary" onClick={handleSubmit} loading={submitting}>
                                提交
                            </Button>

                            <Button type="primary" onClick={handleTest} loading={submitting}>
                                测试一下
                            </Button>
                        </Space>


                    </Form.Item>
                </Form>
            </Card>

            {showTest &&
                <MetaFieldTestDrawer
                    visible={showTest}
                    record={record}
                    onCancel={() => setShowTest(false)}/>

            }

        </PageContainer>
    );
};

export default MetaFieldAdd;
