import {Form, Input, message, Modal} from 'antd';
import React, {useEffect} from 'react';
import {AccountDetail, addAccount, updateAccount} from '@/services/srv/accountApi';

interface AccountModalProps {
    visible: boolean;
    mode: 'add' | 'edit';
    record?: AccountDetail;
    onSuccess: () => void;
    onCancel: () => void;
}

const AccountModal: React.FC<AccountModalProps> = ({visible, mode, record, onSuccess, onCancel}) => {
    const [form] = Form.useForm();

    useEffect(() => {
        if (visible) {
            if (mode === 'edit' && record) {
                form.setFieldsValue({
                    id: record.id,
                    userAccount: record.userAccount
                });
            } else {
                form.resetFields();
            }
        }
    }, [visible, mode, record]);

    const handleOk = () => {
        form.validateFields().then(values => {
            const apiCall = mode === 'add' ? addAccount(values) : updateAccount(values);

            apiCall
                .then(res => {
                    if (res.code === '0000') {
                        message.success(mode === 'add' ? '添加成功' : '更新成功');
                        form.resetFields();
                        onSuccess();
                    } else {
                        message.error(res.message || (mode === 'add' ? '添加失败' : '更新失败'));
                    }
                })
                .catch(e => {
                    message.error(e.message || (mode === 'add' ? '添加失败' : '更新失败'));
                });
        });
    };

    const handleCancel = () => {
        form.resetFields();
        onCancel();
    };

    return (
        <Modal
            title={mode === 'add' ? '添加账户' : '编辑账户'}
            open={visible}
            onOk={handleOk}
            onCancel={handleCancel}
            okText="确定"
            cancelText="取消"
        >
            <Form
                form={form}
                layout="horizontal"
            >
                <Form.Item
                    name="userAccount"
                    label="账户名称"
                    rules={[
                        {required: true, message: '请输入账户名称'},
                        {max: 256, message: '账户名称不能超过256个字符'}
                    ]}
                >
                    <Input placeholder="请输入账户名称"/>
                </Form.Item>

                {mode === 'add' && (
                    <Form.Item
                        name="password"
                        label="密码"
                        rules={[
                            {required: true, message: '请输入密码'},
                            {min: 6, message: '密码长度不能少于6个字符'}
                        ]}
                    >
                        <Input.Password placeholder="请输入密码（至少6个字符）"/>
                    </Form.Item>
                )}
            </Form>
        </Modal>
    );
};

export default AccountModal;
