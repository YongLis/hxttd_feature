import {RemoteRequestPost} from '@/utils/http';

export interface KafkaTopicDetail {
    id: string;
    name: string;
    partitions: number;
    replicas: number;
    consumerGroup?: string;
    offsetStrategy: string;
    dataFormat: string;
    status: string;
    serializer?: string;
    valueDeserializer?: string;
    extraConfig?: { key: string; value: string }[];
    remark?: string;
    auditReason?: string;
    crtTime?: string;
    crtUser?: string;
    deleted: boolean;
}

export interface PageResult<T> {
    code: string;
    message: string;
    current: number;
    pageSize: number;
    total: number;
    data: T[];
}

/**
 * 分页查询Kafka Topic
 */
export function pageQueryKafkaTopic(params: any) {
    return RemoteRequestPost<PageResult<KafkaTopicDetail>>('/api/kafka-topic/page', params);
}

/**
 * 添加Kafka Topic
 */
export function addKafkaTopic(params: any) {
    return RemoteRequestPost('/api/kafka-topic/add', params);
}

/**
 * 更新Kafka Topic
 */
export function updateKafkaTopic(params: any) {
    return RemoteRequestPost('/api/kafka-topic/update', params);
}

/**
 * 删除Kafka Topic
 */
export function deleteKafkaTopic(id: string) {
    return RemoteRequestPost('/api/kafka-topic/delete?id=' + id, {});
}

/**
 * 查询Kafka Topic详情
 */
export function getKafkaTopicDetail(id: string) {
    return RemoteRequestPost<KafkaTopicDetail>('/api/kafka-topic/detail?id=' + id, {});
}
