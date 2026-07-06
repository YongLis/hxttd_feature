import {Button, Form, Input, InputNumber, message, Modal, Select, Spin} from 'antd';
import React, {useCallback, useEffect, useMemo, useRef, useState} from 'react';
import {debounce} from 'lodash';
import {FactorDetail, updateMetaFactor} from '@/services/srv/factorApi';
import {MetaFieldDetail, pageQueryMetaField} from '@/services/srv/metaFieldApi';
import {FeatureConfigItem, listFeatureConfig} from '@/services/srv/featureConfigApi';

interface FactorModalProps {
    visible: boolean;
    record?: FactorDetail;
    onSuccess: () => void;
    onCancel: () => void;
}

const FactorModal: React.FC<FactorModalProps> = ({visible, record, onSuccess, onCancel}) => {
    const [form] = Form.useForm();
    const [metaFields, setMetaFields] = useState<MetaFieldDetail[]>([]);
    const [metaLoading, setMetaLoading] = useState(false);
    const [featureConfigs, setFeatureConfigs] = useState<FeatureConfigItem[]>([]);
    const [featureLoading, setFeatureLoading] = useState(false);
    const [submitting, setSubmitting] = useState(false);
    const mountedRef = useRef(true);

    const factorType = Form.useWatch('factorType', form);

    useEffect(() => {
        if (visible && record) {
            form.setFieldsValue({
                resourceKey: record.resourceKey,
                resourceName: record.resourceName,
                factorType: record.factorType,
                returnType: record.returnType,
                timeout: record.timeout,
                defaultValue: record.defaultValue,
                exceptionValue: record.exceptionValue,
            });
            // META 模式下预置当前关联元字段到下拉选项，确保回显选中值
            if (record.factorType === 'META' && record.resourceKey) {
                setMetaFields([{
                    resourceKey: record.resourceKey,
                    resourceName: record.resourceName,
                    returnType: record.returnType,
                    defaultValue: record.defaultValue,
                    exceptionValue: record.exceptionValue,
                } as MetaFieldDetail]);
            }
            // FEATURE 模式下预置当前关联特征到下拉选项
            if (record.factorType === 'FEATURE' && record.resourceKey) {
                setFeatureConfigs([{
                    featureCode: record.resourceKey,
                    resourceName: record.resourceName,
                    returnType: record.returnType,
                    fixValue: record.defaultValue,
                } as FeatureConfigItem]);
            }
        }
        return () => {
            if (!visible) {
                form.resetFields();
                setMetaFields([]);
                setFeatureConfigs([]);
            }
        };
    }, [visible, record]);

    /** 搜索元字段 */
    const searchMetaFields = useCallback(async (keyword?: string) => {
        setMetaLoading(true);
        try {
            const res = await pageQueryMetaField({current: 1, pageSize: 50, resourceName: keyword || undefined});
            if (!mountedRef.current) return;
            if (res.code === '0000' && res.data) setMetaFields(res.data);
        } finally {
            if (mountedRef.current) setMetaLoading(false);
        }
    }, []);

    const searchMetaFieldsDebounced = useMemo(
        () => debounce((keyword?: string) => searchMetaFields(keyword), 300),
        [searchMetaFields],
    );

    /** 加载特征配置 */
    const loadFeatureConfigs = useCallback(async () => {
        setFeatureLoading(true);
        try {
            const res = await listFeatureConfig();
            if (!mountedRef.current) return;
            if (res.code === '0000' && res.data) setFeatureConfigs(res.data);
        } finally {
            if (mountedRef.current) setFeatureLoading(false);
        }
    }, []);

    /** 选中元字段后同步值 */
    const handleMetaFieldSelect = (resourceKey: string) => {
        const meta = metaFields.find(m => m.resourceKey === resourceKey);
        if (meta) {
            form.setFieldsValue({
                resourceName: meta.resourceName,
                returnType: meta.returnType,
                defaultValue: meta.defaultValue || '',
                exceptionValue: meta.exceptionValue || '',
            });
        }
    };

    /** 选中特征后同步值 */
    const handleFeatureSelect = (featureCode: string) => {
        const feat = featureConfigs.find(f => f.featureCode === featureCode);
        if (feat) {
            form.setFieldsValue({
                resourceKey: feat.featureCode,
                resourceName: feat.resourceName,
                returnType: feat.returnType,
                defaultValue: feat.fixValue || '',
                exceptionValue: '',
            });
        }
    };

    const handleFactorTypeChange = () => {
        form.setFieldsValue({
            resourceKey: '',
            resourceName: '',
            returnType: undefined,
            defaultValue: '',
            exceptionValue: '',
        });
    };

    const handleOk = () => {
        form.validateFields().then(values => {
            setSubmitting(true);
            updateMetaFactor({...values, id: record?.id})
                .then(res => {
                    if (res.code === '0000') {
                        message.success('更新成功');
                        form.resetFields();
                        onSuccess();
                    } else {
                        message.error(res.message || '更新失败');
                    }
                })
                .catch(e => message.error(e.message || '更新失败'))
                .finally(() => setSubmitting(false));
        });
    };

    const isAutoFilled = factorType === 'META' || factorType === 'FEATURE';

    return (
        <Modal title="编辑指标" open={visible} onCancel={onCancel} footer={null} width={640}>
            <Form form={form} layout="vertical">
                <Form.Item name="factorType" label="指标类型" rules={[{required: true, message: '请选择'}]}>
                    <Select placeholder="请选择指标类型" onChange={handleFactorTypeChange}>
                        <Select.Option value="META">基础指标</Select.Option>
                        <Select.Option value="DERIVATIVE">衍生指标</Select.Option>
                        <Select.Option value="FEATURE">特征指标</Select.Option>
                    </Select>
                </Form.Item>

                {factorType === 'META' ? (
                    <Form.Item name="resourceKey" label="关联元字段"
                               rules={[{required: true, message: '请选择元字段'}]}>
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
                ) : factorType === 'FEATURE' ? (
                    <Form.Item name="resourceKey" label="关联特征编码"
                               rules={[{required: true, message: '请选择特征编码'}]}>
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
                ) : (
                    <Form.Item name="resourceKey" label="资源标识键"
                               rules={[{required: true, message: '请输入'}, {max: 128}]}>
                        <Input placeholder="请输入资源标识键"/>
                    </Form.Item>
                )}

                <Form.Item name="resourceName" label="资源名称"
                           rules={[{required: true, message: '请输入'}, {max: 256}]}>
                    <Input placeholder="请输入资源名称" disabled={isAutoFilled}/>
                </Form.Item>

                <Form.Item name="returnType" label="返回值类型" rules={[{required: true, message: '请选择'}]}>
                    <Select placeholder="请选择返回值类型" disabled={isAutoFilled}>
                        <Select.Option value="BOOLEAN">BOOLEAN</Select.Option>
                        <Select.Option value="STRING">STRING</Select.Option>
                        <Select.Option value="NUMBER">NUMBER</Select.Option>
                    </Select>
                </Form.Item>

                <Form.Item name="timeout" label="超时时间(ms)">
                    <InputNumber style={{width: '100%'}} placeholder="请输入超时时间" min={0}/>
                </Form.Item>

                <Form.Item name="defaultValue" label="默认值" rules={[{max: 512}]}>
                    <Input placeholder="请输入默认值" disabled={isAutoFilled}/>
                </Form.Item>

                <Form.Item name="exceptionValue" label="异常值" rules={[{max: 512}]}>
                    <Input placeholder="请输入异常值" disabled={isAutoFilled}/>
                </Form.Item>

                <Form.Item>
                    <Button type="primary" onClick={handleOk} loading={submitting}>确定</Button>
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default FactorModal;
