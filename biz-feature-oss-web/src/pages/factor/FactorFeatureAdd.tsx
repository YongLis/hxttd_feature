import {Button, Card, Form, Input, InputNumber, message, Select, Spin} from 'antd';
import {history} from '@umijs/max';
import React, {useCallback, useRef, useState} from 'react';
import {addFeatureFactor} from '@/services/srv/factorApi';
import {MetaFieldDetail} from '@/services/srv/metaFieldApi';
import {FeatureConfigItem, listFeatureConfig} from '@/services/srv/featureConfigApi';
import {PageContainer} from '@ant-design/pro-layout';
import {RETURN_TYPE_OPTIONS} from '@/components/consts/AppCommons';


/**
 * 新增指标页面
 */
const FactorFeatureAdd: React.FC = () => {
    const [form] = Form.useForm();
    const [submitting, setSubmitting] = useState(false);
    const [metaFields, setMetaFields] = useState<MetaFieldDetail[]>([]);
    const [metaLoading, setMetaLoading] = useState(false);
    const [featureConfigs, setFeatureConfigs] = useState<FeatureConfigItem[]>([]);
    const [featureLoading, setFeatureLoading] = useState(false);
    const mountedRef = useRef(true);

    const factorType = Form.useWatch('factorType', form);

    /** 加载特征配置 */
    const loadFeatureConfigs = useCallback(async () => {
        setFeatureLoading(true);
        try {
            const res = await listFeatureConfig();
            if (!mountedRef.current) return;
            if (res.code === '0000' && res.data) {
                setFeatureConfigs(res.data);
            }
        } finally {
            if (mountedRef.current) setFeatureLoading(false);
        }
    }, []);


    /** 选中特征后同步值 */
    const handleFeatureSelect = (featureCode: string) => {
        const feat = featureConfigs.find(f => f.featureCode === featureCode);
        if (feat) {
            form.setFieldsValue({
                resourceKey: feat.featureCode,
                resourceName: feat.resourceName,
                returnType: feat.returnType,
                defaultValue: feat.defaultValue,
                exceptionValue: feat.exceptionValue,
                timeout: feat.timeout
            });
        }
    };

    /** 指标类型变更时清理 */
    const handleFactorTypeChange = () => {
        form.setFieldsValue({
            resourceKey: '',
            resourceName: '',
            returnType: undefined,
            defaultValue: '',
            exceptionValue: '',
            timeout: ''
        });
    };

    const handleSubmit = () => {
        form.validateFields().then(values => {
            setSubmitting(true);
            addFeatureFactor(values)
                .then(res => {
                    if (res.code === '0000') {
                        message.success('添加成功');
                        history.push('/factor/index');
                    } else {
                        message.error(res.message || '添加失败');
                    }
                })
                .catch(e => message.error(e.message || '添加失败'))
                .finally(() => setSubmitting(false));
        });
    };


    return (
        <PageContainer title={false}>
            <Card>
                <Form form={form} layout="horizontal" style={{maxWidth: 720}} initialValues={{factorType: 'FEATURE'}}>
                    <Form.Item name="factorType" label="指标类型" rules={[{required: true, message: '请选择'}]}>
                        <Input disabled/>
                    </Form.Item>

                    <Form.Item
                        name="featureCode"
                        label="关联特征编码"
                        rules={[{required: true, message: '请选择特征编码'}]}
                    >
                        <Select
                            showSearch
                            placeholder="搜索并选择特征"
                            filterOption={(input, option) =>
                                (option?.label as string)?.toLowerCase().includes(input.toLowerCase())
                            }
                            onFocus={() => featureConfigs.length === 0 && loadFeatureConfigs()}
                            onChange={handleFeatureSelect}
                            notFoundContent={featureLoading ? <Spin size="small"/> : '无数据'}
                            loading={featureLoading}
                            options={featureConfigs.map(f => ({
                                value: f.featureCode,
                                label: `${f.featureCode} — ${f.resourceName}`,
                            }))}
                        />
                    </Form.Item>

                    <Form.Item name="resourceName" label="资源名称"
                               rules={[{required: true, message: '请输入'}, {max: 256}]}>
                        <Input placeholder="请输入资源名称" disabled/>
                    </Form.Item>

                    <Form.Item name="returnType" label="返回值类型" rules={[{required: true, message: '请选择'}]}>
                        <Select placeholder="请选择返回值类型" disabled options={RETURN_TYPE_OPTIONS}/>
                    </Form.Item>

                    <Form.Item name="timeout" label="超时时间(ms)">
                        <InputNumber style={{width: '100%'}} placeholder="请输入超时时间" min={0} disabled/>
                    </Form.Item>

                    <Form.Item name="defaultValue" label="默认值" rules={[{max: 512}]}>
                        <Input placeholder="请输入默认值" disabled/>
                    </Form.Item>

                    <Form.Item name="exceptionValue" label="异常值" rules={[{max: 512}]}>
                        <Input placeholder="请输入异常值" disabled/>
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" onClick={handleSubmit} loading={submitting}>提交</Button>
                    </Form.Item>
                </Form>
            </Card>
        </PageContainer>
    );
};

export default FactorFeatureAdd;
