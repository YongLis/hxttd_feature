import {Button, Card, Form, Input, InputNumber, message, Select, Spin} from 'antd';
import {history} from '@umijs/max';
import React, {useCallback, useMemo, useRef, useState} from 'react';
import {debounce} from 'lodash';
import {addMetaFactor} from '@/services/srv/factorApi';
import {MetaFieldDetail, pageQueryMetaField} from '@/services/srv/metaFieldApi';
import {FeatureConfigItem} from '@/services/srv/featureConfigApi';
import {PageContainer} from '@ant-design/pro-layout';
import {RETURN_TYPE_OPTIONS} from '@/components/consts/AppCommons';


/**
 * 新增指标页面
 */
const FactorMetaAdd: React.FC = () => {
    const [form] = Form.useForm();
    const [submitting, setSubmitting] = useState(false);
    const [metaFields, setMetaFields] = useState<MetaFieldDetail[]>([]);
    const [metaLoading, setMetaLoading] = useState(false);
    const [featureConfigs, setFeatureConfigs] = useState<FeatureConfigItem[]>([]);
    const [featureLoading, setFeatureLoading] = useState(false);
    const mountedRef = useRef(true);

    const factorType = Form.useWatch('factorType', form);

    /** 搜索元字段 */
    const searchMetaFields = useCallback(async (keyword?: string) => {
        setMetaLoading(true);
        try {
            const res = await pageQueryMetaField({
                current: 1, pageSize: 10000,
                resourceName: keyword || undefined,
            });
            if (!mountedRef.current) return;
            if (res.code === '0000' && res.data) {
                setMetaFields(res.data);
            }
        } finally {
            if (mountedRef.current) setMetaLoading(false);
        }
    }, []);

    const searchMetaFieldsDebounced = useMemo(
        () => debounce((keyword?: string) => searchMetaFields(keyword), 300),
        [searchMetaFields],
    );


    /** 选中元字段后同步值 */
    const handleMetaFieldSelect = (resourceKey: string) => {
        const meta = metaFields.find(m => m.resourceKey === resourceKey);
        if (meta) {
            form.setFieldsValue({
                resourceName: meta.resourceName,
                returnType: meta.returnType,
                defaultValue: meta.defaultValue || '',
                exceptionValue: meta.exceptionValue || '',
                timeout: meta.timeout || 100,
            });
        }
    };

    const handleSubmit = () => {
        form.validateFields().then(values => {
            setSubmitting(true);
            addMetaFactor(values)
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
                <Form form={form} layout="horizontal" style={{maxWidth: 720}} initialValues={{factorType: 'META'}}>
                    <Form.Item name="factorType" label="指标类型" rules={[{required: true, message: '请选择'}]}>
                        <Input disabled/>
                    </Form.Item>

                    <Form.Item
                        name="metaFieldCode"
                        label="关联元字段"
                        rules={[{required: true, message: '请选择元字段'}]}
                    >
                        <Select
                            showSearch
                            placeholder="搜索并选择元字段"
                            filterOption={false}
                            onSearch={searchMetaFieldsDebounced}
                            onFocus={() => metaFields.length === 0 && searchMetaFields()}
                            onChange={handleMetaFieldSelect}
                            notFoundContent={metaLoading ? <Spin size="small"/> : '无数据'}
                            options={metaFields.map(m => ({
                                value: m.resourceKey,
                                label: `${m.resourceKey} — ${m.resourceName}`,
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

export default FactorMetaAdd;
