import {Form, Input, message, Modal} from 'antd';
import React, {useEffect} from 'react';
import {addProject, ProjectDetail, updateProject} from '@/services/srv/projectApi';

interface ProjectModalProps {
    visible: boolean;
    mode: 'add' | 'edit';
    record?: ProjectDetail;
    onSuccess: () => void;
    onCancel: () => void;
}

const ProjectModal: React.FC<ProjectModalProps> = ({visible, mode, record, onSuccess, onCancel}) => {
    const [form] = Form.useForm();

    useEffect(() => {
        if (visible) {
            if (mode === 'edit' && record) {
                form.setFieldsValue(record);
            } else {
                form.resetFields();
            }
        }
    }, [visible, mode, record]);

    const handleOk = () => {
        form.validateFields().then(values => {
            const apiCall = mode === 'add' ? addProject(values) : updateProject(values);

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
            title={mode === 'add' ? '添加项目' : '编辑项目'}
            open={visible}
            onOk={handleOk}
            onCancel={handleCancel}
            okText="确定"
            cancelText="取消"
        >
            <Form
                form={form}
                layout="vertical"
            >
                <Form.Item
                    name="projectCode"
                    label="项目代码"
                    rules={[
                        {required: true, message: '请输入项目代码'},
                        {max: 4, message: '项目代码不能超过4个字符'}
                    ]}
                >
                    <Input placeholder="请输入项目代码（最多4个字符）"/>
                </Form.Item>

                <Form.Item
                    name="name"
                    label="项目名称"
                    rules={[
                        {required: true, message: '请输入项目名称'},
                        {max: 200, message: '项目名称不能超过200个字符'}
                    ]}
                >
                    <Input placeholder="请输入项目名称"/>
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default ProjectModal;
