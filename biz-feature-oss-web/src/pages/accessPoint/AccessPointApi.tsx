import {Button, Card, Descriptions, message, Space, Table, Tag, Typography} from 'antd';
import {history, useSearchParams} from '@umijs/max';
import {PageContainer} from '@ant-design/pro-layout';
import {ArrowLeftOutlined, FilePdfOutlined, FileTextOutlined} from '@ant-design/icons';
import React, {useCallback, useEffect, useRef, useState} from 'react';
import {AccessPointDocDetail, getAccessPointDoc} from '@/services/srv/accessPointApi';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';

const {Title, Text, Paragraph} = Typography;

/** 参数表格公共列 */
const paramTableColumns = [

    {
        title: '一级编码',
        dataIndex: 'paramCode',
        key: 'paramCode1',
        width: 160,
        render: (val: string, record: any) => <Text code>{record.paramLevel === 1 ? val : '-'}</Text>,
    },
    {
        title: '二级编码',
        dataIndex: 'paramCode',
        key: 'paramCode2',
        width: 160,
        render: (val: string, record: any) => <Text code>{record.paramLevel === 2 ? val : '-'}</Text>,
    },
    {
        title: '三级编码',
        dataIndex: 'paramCode',
        key: 'paramCode3',
        width: 160,
        render: (val: string, record: any) => <Text code>{record.paramLevel === 3 ? val : '-'}</Text>,
    },
    {
        title: '参数名',
        dataIndex: 'paramName',
        key: 'paramName',
        width: 160,
    },
    {
        title: '类型',
        dataIndex: 'paramType',
        key: 'paramType',
        width: 90,
        align: 'center' as const,
        render: (val: string) => <Tag>{val}</Tag>,
    },
    {
        title: '必填',
        dataIndex: 'required',
        key: 'required',
        width: 60,
        align: 'center' as const,
        render: (val: number) =>
            val === 1 ? <Tag color="red">是</Tag> : <Tag>否</Tag>,
    },
    {
        title: '默认值',
        dataIndex: 'defaultValue',
        key: 'defaultValue',
        width: 120,
        ellipsis: true,
        render: (val: string) => val || '-',
    },
    {
        title: '说明',
        dataIndex: 'description',
        key: 'description',
        ellipsis: true,
        render: (val: string) => val || '-',
    },
];

/**
 * 接入点 API 接口文档预览页面
 */
const AccessPointApi: React.FC = () => {
    const [searchParams] = useSearchParams();
    const accessPointId = searchParams.get('id');

    const [loading, setLoading] = useState(false);
    const [exporting, setExporting] = useState(false);
    const [detail, setDetail] = useState<AccessPointDocDetail | null>(null);
    const contentRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (accessPointId) {
            setLoading(true);
            getAccessPointDoc(Number(accessPointId))
                .then((res) => {
                    if (res.code === '0000' && res.data) {
                        setDetail(res.data);
                    }
                })
                .finally(() => setLoading(false));
        }
    }, [accessPointId]);

    const handleBack = () => history.push('/access-point/index');

    /** 导出 PDF */
    const handleExportPdf = useCallback(async () => {
        if (!contentRef.current || !detail) return;
        setExporting(true);
        try {
            const canvas = await html2canvas(contentRef.current, {
                scale: 2,
                useCORS: true,
                backgroundColor: '#ffffff',
            });
            const imgData = canvas.toDataURL('image/png');
            const pdf = new jsPDF('p', 'mm', 'a4');
            const pageWidth = pdf.internal.pageSize.getWidth();
            const pageHeight = pdf.internal.pageSize.getHeight();
            const imgWidth = pageWidth;
            const imgHeight = (canvas.height * imgWidth) / canvas.width;
            let heightLeft = imgHeight;
            let position = 0;

            pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
            heightLeft -= pageHeight;

            // 多页处理
            while (heightLeft > 0) {
                position = -(imgHeight - heightLeft);
                pdf.addPage();
                pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
                heightLeft -= pageHeight;
            }

            pdf.save(`${detail.name}_API文档.pdf`);
            message.success('PDF 已保存');
        } catch {
            message.error('PDF 导出失败');
        } finally {
            setExporting(false);
        }
    }, [detail]);

    if (!detail) {
        return (
            <PageContainer title={false} loading={loading}>
                <Card>
                    <Button type="link" icon={<ArrowLeftOutlined/>} onClick={handleBack}>
                        返回列表
                    </Button>
                </Card>
            </PageContainer>
        );
    }

    return (
        <PageContainer title={false} loading={loading}>
            <div ref={contentRef}>
                {/* API 基本信息 */}
                <Card style={{marginBottom: 16}}>
                    <Space align="center" style={{marginBottom: 16}}>
                        <FileTextOutlined style={{fontSize: 24, color: '#1890ff'}}/>
                        <Title level={3} style={{margin: 0}}>
                            {detail.name}
                        </Title>
                        <Tag color={detail.deleted ? 'red' : 'green'}>
                            {detail.deleted ? '无效' : '有效'}
                        </Tag>
                    </Space>

                    {detail.description && (
                        <Paragraph type="secondary">{detail.description}</Paragraph>
                    )}

                    <Descriptions column={1} size="small" bordered style={{marginTop: 16}}>
                        <Descriptions.Item label="编码">{detail.code}</Descriptions.Item>
                        <Descriptions.Item label="名称">{detail.name}</Descriptions.Item>
                        <Descriptions.Item label="版本">{detail.version}</Descriptions.Item>
                        <Descriptions.Item label="创建人">{detail.crtUser}</Descriptions.Item>
                    </Descriptions>
                </Card>

                {/* 请求参数 */}
                <Card style={{marginBottom: 16}} title="请求参数">
                    {detail.reqParam && detail.reqParam.length > 0 ? (
                        <Table
                            size="small"
                            dataSource={detail.reqParam}
                            rowKey="id"
                            pagination={false}
                            defaultExpandAllRows
                            columns={paramTableColumns}
                        />
                    ) : (
                        <Text type="secondary">暂无请求参数</Text>
                    )}
                </Card>

                {/* 响应参数 */}
                <Card title="响应参数">
                    {detail.resParam && detail.resParam.length > 0 ? (
                        <Table
                            size="small"
                            dataSource={detail.resParam}
                            rowKey="id"
                            pagination={false}
                            defaultExpandAllRows
                            columns={paramTableColumns}
                        />
                    ) : (
                        <Text type="secondary">暂无响应参数</Text>
                    )}
                </Card>
            </div>

            {/* 导出按钮 */}
            <div style={{textAlign: 'center', marginTop: 16}}>
                <Button
                    type="primary"
                    icon={<FilePdfOutlined/>}
                    loading={exporting}
                    onClick={handleExportPdf}
                >
                    保存为 PDF
                </Button>
            </div>
        </PageContainer>
    );
};

export default AccessPointApi;
