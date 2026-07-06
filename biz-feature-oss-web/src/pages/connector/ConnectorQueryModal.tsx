import React, {useRef, useState} from 'react';
import {Modal, Radio, Tag} from 'antd';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {ConnectorDetail, pageQueryConnector} from '@/services/srv/connectorApi';
import {getDictCodeOptions} from '@/services/srv/dictCodeApi';
import {CustomProTable} from '@/components';
import {SelectedConnector} from '@/services/srv/factorApi';

const connectorTypeColorMap: Record<string, string> = {
    JDBC: 'blue',
    ES: 'green',
    HBASE: 'purple',
    HTTP: 'orange',
};

interface Props {
    visible: boolean;
    onSelect: (item: SelectedConnector) => void;
    onClose: () => void;
}


const ConnectorQueryModal: React.FC<Props> = ({visible, onSelect, onClose}) => {
    const actionRef = useRef<ActionType | null>(null);

    const [selectedRowKey, setSelectedRowKey] = useState<string>();


    const columns: ProColumns<ConnectorDetail>[] = [

        {
            title: '选择',
            key: 'id',
            width: 50,
            search: false,
            render: (_, record) => (
                <Radio onChange={() => setSelectedRowKey(record.resourceKey)}
                       checked={selectedRowKey === record.resourceKey}/>
            ),
        },

        {
            title: '资源标识键',
            dataIndex: 'resourceKey',
            key: 'resourceKey',
            width: 160,
            ellipsis: true,
            copyable: true,
        },
        {title: '资源名称', dataIndex: 'resourceName', key: 'resourceName', width: 160, ellipsis: true},
        {
            title: '版本',
            dataIndex: 'version',
            key: 'version',
            width: 80,
            align: 'center',
            hideInSearch: true,
            ellipsis: true
        },
        {
            title: '连接器类型', dataIndex: 'connectorType', key: 'connectorType', width: 120, align: 'center',
            valueType: 'select',
            request: async () => {
                return getDictCodeOptions('ttd', 'connectorType');
            },
            render: (_, r) => <Tag color={connectorTypeColorMap[r.connectorType] || 'default'}>{r.connectorType}</Tag>,
        },
        {title: '超时(ms)', dataIndex: 'timeout', key: 'timeout', width: 90, align: 'center', hideInSearch: true},
        {title: '创建人', dataIndex: 'crtUser', key: 'crtUser', width: 100, align: 'center', hideInSearch: true},
        {
            title: '创建时间',
            dataIndex: 'crtTime',
            key: 'crtTime',
            width: 170,
            valueType: 'dateTime',
            align: 'center',
            hideInSearch: true,
        }
    ];

    return (
        <Modal title={'连接查询'} width={'80%'} height={'80%'} open={visible}
               onCancel={onClose}
               onOk={onClose}>
            <CustomProTable<ConnectorDetail>
                actionRef={actionRef}
                headerTitle="连接器列表"
                dateFormatter="string"
                rowKey="resourceKey"
                cardBordered
                toolBarRender={false}
                request={(params: any) => pageQueryConnector({...params})}
                columns={columns}
                onRow={(record) => {
                    return {
                        onClick: () => {
                            setSelectedRowKey(record.resourceKey)
                            onSelect({
                                connectorCode: record.resourceKey,
                                connectorType: record.connectorType,
                                connectorParam: record.factorCodes
                            })
                        }
                    }
                }}
            />
        </Modal>
    );
};

export default ConnectorQueryModal;
