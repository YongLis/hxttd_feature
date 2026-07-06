import React, {useState} from 'react';
import {Button, Card, Input, message, Typography} from 'antd';
import {CheckOutlined, CloseOutlined} from '@ant-design/icons';
import {history, useLocation} from '@umijs/max';
import {PageContainer} from '@ant-design/pro-layout';
import {submitAudit} from '@/services/srv/auditApi';
import AccessPointCompareView from './AccessPointCompareView';
import MetaFieldCompareView from './MetaFieldCompareView';
import MetaFactorCompareView from './MetaFactorCompareView';
import DerivativeFactorCompareView from './DerivativeFactorCompareView';
import FeatureCompareView from './FeatureCompareView';
import FeatureFactorCompareView from './FeatureFactorCompareView';
import JdbcConnectorCompareView from './JdbcConnectorCompareView';
import EsConnectorCompareView from './EsConnectorCompareView';
import HttpConnectorCompareView from './HttpConnectorCompareView';

const {Text} = Typography;
const {TextArea} = Input;

/**
 * 审核详情独立页 — 左右对比展示
 */
const AuditView: React.FC = () => {

    const location = useLocation() as { state?: { id: number, resourceType: string } };
    console.log(location);
    const id = location.state?.id;
    const resourceType = location.state?.resourceType;

    console.log(`id=${id}, resourceType=${resourceType}`);

    const [auditComment, setAuditComment] = useState('');

    const [submitting, setSubmitting] = useState(false);

    const handleApprove = (status: 'APPROVED' | 'REJECTED') => {
        setSubmitting(true);
        submitAudit({'id': Number(id), auditStatus: status, auditComment})
            .then(res => {
                if (res.code === '0000') {
                    message.success(status === 'APPROVED' ? '审核通过' : '已驳回');
                    history.push('/audit/index');
                } else {
                    message.error(res.message || '操作失败');
                }
            })
            .catch(e => message.error(e.message || '操作失败'))
            .finally(() => setSubmitting(false));
    };


    return (
        <PageContainer title={false}>
            <Card>
                {
                    resourceType === 'POINT' &&
                    <AccessPointCompareView
                        visible={true}
                        id={Number(id)}
                    />
                }
                {
                    resourceType === 'META' &&
                    <MetaFieldCompareView
                        visible={true}
                        id={Number(id)}
                    />
                }
                {
                    resourceType === 'FACTOR_META' &&
                    <MetaFactorCompareView
                        visible={true}
                        id={Number(id)}
                    />
                }
                {
                    resourceType === 'FACTOR_DERIVATIVE' &&
                    <DerivativeFactorCompareView
                        visible={true}
                        id={Number(id)}
                    />
                }
                {
                    resourceType === 'FACTOR_FEATURE' &&
                    <FeatureFactorCompareView
                        visible={true}
                        id={Number(id)}
                    />
                }
                {
                    resourceType === 'FEATURE' &&
                    <FeatureCompareView
                        visible={true}
                        id={Number(id)}
                    />
                }
                {
                    resourceType === 'CONNECTOR_JDBC' &&
                    <JdbcConnectorCompareView
                        visible={true}
                        id={Number(id)}
                    />
                }
                {
                    resourceType === 'CONNECTOR_ES' &&
                    <EsConnectorCompareView
                        visible={true}
                        id={Number(id)}
                    />
                }
                {
                    resourceType === 'CONNECTOR_HTTP' &&
                    <HttpConnectorCompareView
                        visible={true}
                        id={Number(id)}
                    />
                }

                {/* 审核意见 */}
                <div style={{marginBottom: 12}}>
                    <Text strong style={{fontSize: 15}}>审核意见</Text>
                </div>
                <TextArea
                    rows={4}
                    value={auditComment}
                    onChange={e => setAuditComment(e.target.value)}
                    placeholder="请输入审核意见（可选）"
                />

                {/* 底部操作按钮 */}

                <div style={{marginTop: 24, display: 'flex', justifyContent: 'flex-end', gap: 12}}>
                    <Button icon={<CheckOutlined/>} type="primary" loading={submitting}
                            onClick={() => handleApprove('APPROVED')}>通过</Button>
                    <Button icon={<CloseOutlined/>} danger loading={submitting}
                            onClick={() => handleApprove('REJECTED')}>驳回</Button>
                </div>

            </Card>
        </PageContainer>
    );
};

export default AuditView;
