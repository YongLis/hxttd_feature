import {Button, Descriptions, Divider, Modal, Spin, Table} from 'antd';
import React, {useEffect, useState} from 'react';
import {getTableDefDetail, type TableColumnDetail, type TableDefDetail} from '@/services/srv/tableDefApi';

interface Props {
    visible: boolean;
    tableName: string;
    onClose: () => void;
}

/**
 * 表定义详情弹窗 — 展示表基本信息 + 字段列表
 */
const TableDefDetailView: React.FC<Props> = ({visible, tableName, onClose}) => {
    const [detailData, setDetailData] = useState<TableDefDetail | null>(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (visible && tableName) {
            setLoading(true);
            setDetailData(null);
            getTableDefDetail(tableName)
                .then((res: any) => {
                    if (res.code === '0000' && res.data) {
                        setDetailData(res.data);
                    }
                })
                .catch(() => {
                })
                .finally(() => setLoading(false));
        }
    }, [visible, tableName]);

    return (
        <Modal
            title="表定义详情"
            open={visible}
            onCancel={onClose}
            footer={<Button onClick={onClose}>关闭</Button>}
            destroyOnClose
            width={900}
            style={{top: 40}}
        >
            <Spin spinning={loading}>
                {detailData && (
                    <>
                        <Descriptions
                            title="表基本信息"
                            column={2}
                            size="small"
                            bordered
                            style={{marginBottom: 16}}
                        >
                            <Descriptions.Item label="表名">{detailData.tableName}</Descriptions.Item>
                            <Descriptions.Item label="所属库">{detailData.dataSource}</Descriptions.Item>
                            <Descriptions.Item label="关联Topic">{detailData.topic || '-'}</Descriptions.Item>
                            <Descriptions.Item label="字段数量">{detailData.columnCount || 0}</Descriptions.Item>
                            <Descriptions.Item label="创建人">{detailData.crtUser || '-'}</Descriptions.Item>
                            <Descriptions.Item label="创建时间">{detailData.crtTime || '-'}</Descriptions.Item>
                            <Descriptions.Item label="修改人">{detailData.uptUser || '-'}</Descriptions.Item>
                            <Descriptions.Item label="修改时间">{detailData.uptTime || '-'}</Descriptions.Item>
                        </Descriptions>

                        <Divider orientation="left" style={{fontWeight: 600}}>字段列表</Divider>
                        <Table<TableColumnDetail>
                            dataSource={detailData.columns || []}
                            rowKey="columnName"
                            pagination={false}
                            size="small"
                            bordered
                            columns={[
                                {title: '列名', dataIndex: 'columnName', width: 140},
                                {title: '数据类型', dataIndex: 'columnType', width: 100},
                                {
                                    title: '是否为空',
                                    dataIndex: 'nullAble',
                                    width: 80,
                                    align: 'center',
                                    render: (v: string) => v === 'Y' ? '是' : v === 'N' ? '否' : v,
                                },
                                {title: '指标编码', dataIndex: 'factorCode', width: 140, render: (v: string) => v || '-'},
                                // {title: '创建人', dataIndex: 'crtUser', width: 100, render: (v: string) => v || '-'},
                                // {title: '创建时间', dataIndex: 'crtTime', width: 160, render: (v: string) => v || '-'},
                            ]}
                        />
                    </>
                )}
            </Spin>
        </Modal>
    );
};

export default TableDefDetailView;
