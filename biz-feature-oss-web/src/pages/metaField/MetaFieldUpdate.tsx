import {Button, Card, Form, Input, message, Select, Space} from 'antd';
import React from 'react';
import {getMetaFieldById, MetaFieldDetail, updateMetaField} from '@/services/srv/metaFieldApi';
import GroovyEditor from '@/components/common/GroovyEditor';
import {getObjectReturnDefaultValue, ObjectReturnTypeEnum, ObjectReturnTypeLabel} from '@/components/consts/AppEnum';
import {CustomPageContainer} from '@/components';
import {history, useLocation} from '@umijs/max';
import MetaFieldTestDrawer from './MetaFieldTestDrawer';

const {TextArea} = Input;


const MetaFieldUpdate: React.FC = ({}) => {
    const [form] = Form.useForm();
    const [loading, setLoading] = React.useState(false);
    const [script, setScript] = React.useState('');
    const [showTest, setShowTest] = React.useState(false);

    const [categoryOptions, setCategoryOptions] = React.useState<{ label: string; value: string }[]>([]);
    const [record, setRecord] = React.useState<MetaFieldDetail>();
    const location = useLocation() as { state?: { id: number } };
    const id = location.state?.id;
    console.log(location);


    React.useEffect(() => {
        if (undefined === id) {
            message.error('缺少元字段ID');
            history.push('/meta-field/index');
            return;
        }
        setLoading(true);
        getMetaFieldById(Number(id))
            .then((res) => {
                if (res.code === '0000' && res.data) {
                    setRecord(res.data);
                    setScript(res.data.script);
                    form.setFieldsValue(res.data);
                } else {
                    message.error(res.message || '查询失败');
                    history.push('/meta-field/index');
                }
            })
            .catch(() => {
                message.error('查询元字段详情失败');
                history.push('/meta-field/index');
            })
            .finally(() => {
                setLoading(false);
            });
    }, [id]);


    const handleOk = () => {
        form.validateFields().then(values => {
            updateMetaField({...values, id: record?.id})
                .then(res => {
                    if (res.code === '0000') {
                        message.success('更新成功');
                        form.resetFields();
                    } else {
                        message.error(res.message || '更新失败');
                    }
                })
                .catch(e => {
                    message.error(e.message || '更新失败');
                });
        });
    };


    const OBJECT_RETURN_TYPE = Object.values(ObjectReturnTypeEnum).map((code) => ({
        value: code,
        label: ObjectReturnTypeLabel[code],
    }));

    const onObectChange = (value: any) => {
        form.setFieldsValue({'defaultValue': getObjectReturnDefaultValue(value)});
        form.setFieldsValue({'exceptionValue': getObjectReturnDefaultValue(value)});
    };

    const handleTest = () => {
        setShowTest(true)
        setRecord(form.getFieldsValue);
    };


    return (
        <CustomPageContainer>
            <Card>
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
                        name="resourceKey"
                        label="元字段编码">
                        <Input disabled/>
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
                            <Button type="primary" onClick={handleOk}>
                                确定
                            </Button>

                            <Button type="primary" onClick={handleTest}>
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

        </CustomPageContainer>
    );
};

export default MetaFieldUpdate;
