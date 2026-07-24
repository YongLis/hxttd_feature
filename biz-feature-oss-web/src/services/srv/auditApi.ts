import {RemoteRequestGet, RemoteRequestPost} from '@/utils/http';
import {AccessPointDetail} from './accessPointApi';
import {FeatureConfigDetail} from './featureConfigApi';
import {MetaFieldDetail} from './metaFieldApi';

/** 审核列表记录 */
export interface AuditRecord {
    id: number;
    resourceType: string;
    resourceKey: string;
    resourceName: string;
    auditStatus: string;
    operationType: string;
    auditComment?: string;
    submitUser: string;
    auditUser?: string;
    submitTime: string;
    auditTime?: string;
}

/** 接入点审核详情 */
export interface PointAuditDetail {
    id: number;
    resourceKey: string;
    pointCode: string;
    pointName: string;
    auditStatus: string;
    operationType: string;
    before?: AccessPointDetail;
    after?: AccessPointDetail;
    auditComment?: string;
    submitUser: string;
    auditUser?: string;
    submitTime: string;
    auditTime?: string;
}

/** 审核详情（结构化，before/after 为解析后的配置对象） */
export interface FeatureAuditDetail {
    id: number;
    resourceKey: string;
    featureCode: string;
    featureName: string;
    auditStatus: string;
    operationType: string;
    before?: FeatureConfigDetail;
    after?: FeatureConfigDetail;
    auditComment?: string;
    submitUser: string;
    auditUser?: string;
    submitTime: string;
    auditTime?: string;
}

/** 元字段审核详情 */
export interface MetaAuditDetail {
    id: number;
    resourceKey: string;
    metaCode: string;
    metaName: string;
    auditStatus: string;
    operationType: string;
    before?: MetaFieldDetail;
    after?: MetaFieldDetail;
    auditComment?: string;
    submitUser: string;
    auditUser?: string;
    submitTime: string;
    auditTime?: string;
}

// ============ 指标审核详情 ============

/** 连接器参数字段 */
export interface ConnectorParamField {
    fieldCode: string;
    sourceCode: string;
}

/** 指标详情基类（对应 FactorDetailRes） */
export interface FactorDetailRes {
    id: number;
    resourceKey: string;
    resourceName: string;
    version: string;
    projectId: number;
    factorType: string;
    returnType: string;
    defaultValue: string;
    exceptionValue: string;
    timeout: number;
    crtUser: string;
    crtTime: string;
}

/** 元字段指标详情（对应 MetaFactorDetailRes） */
export interface MetaFactorDetailRes extends FactorDetailRes {
    metaFieldCode: string;
}

/** 衍生指标详情（对应 DerivativeFactorDetailRes） */
export interface DerivativeFactorDetailRes extends FactorDetailRes {
    factorCodes: string[];
    connectorType: string;
    connectorCode: string;
    params: ConnectorParamField[];
    language: string;
    conditionScript: string;
    script: string;
}

/** 实时特征指标详情（对应 FeatureFactorDetailRes） */
export interface FeatureFactorDetailRes extends FactorDetailRes {
    featureCode: string;
}

/** 元字段指标审核详情 */
export interface MetaFactorAuditDetail {
    id: number;
    resourceType: string;
    resourceKey: string;
    resourceName: string;
    auditStatus: string;
    operationType: string;
    before?: MetaFactorDetailRes;
    after?: MetaFactorDetailRes;
    auditComment?: string;
    submitUser: string;
    auditUser?: string;
    submitTime: string;
    auditTime?: string;
}

/** 衍生指标审核详情 */
export interface DerivativeFactorAuditDetail {
    id: number;
    resourceType: string;
    resourceKey: string;
    resourceName: string;
    auditStatus: string;
    operationType: string;
    before?: DerivativeFactorDetailRes;
    after?: DerivativeFactorDetailRes;
    auditComment?: string;
    submitUser: string;
    auditUser?: string;
    submitTime: string;
    auditTime?: string;
}

/** 实时特征指标审核详情 */
export interface FeatureFactorAuditDetail {
    id: number;
    resourceType: string;
    resourceKey: string;
    resourceName: string;
    auditStatus: string;
    operationType: string;
    before?: FeatureFactorDetailRes;
    after?: FeatureFactorDetailRes;
    auditComment?: string;
    submitUser: string;
    auditUser?: string;
    submitTime: string;
    auditTime?: string;
}


/** 分页查询指标审核列表 */
export function pageQueryAudit(params: any) {
    return RemoteRequestPost<{
        code: string;
        data: AuditRecord[];
        total: number;
        pageSize: number;
        current: number;
    }>('/api/audit/page', params);
}

/** 指标审核操作(通过/驳回) */
export function submitAudit(params: { id: number; auditStatus: string; auditComment?: string }) {
    return RemoteRequestPost('/api/audit/submit', params);
}


export function getPointAuditDetail(id: number) {
    return RemoteRequestGet<{ code: string; data: PointAuditDetail }>('/api/audit/getPointAuditDetail?id=' + id);
}

export function getMetaAuditDetail(id: number) {
    return RemoteRequestGet<{ code: string; data: MetaAuditDetail }>('/api/audit/getMetaAuditDetail?id=' + id);
}

export function getFeatureAuditDetail(id: number) {
    return RemoteRequestGet<{ code: string; data: FeatureAuditDetail }>('/api/audit/getFeatureAuditDetail?id=' + id);
}

export function getMetaFactorAuditDetail(id: number) {
    return RemoteRequestGet<{ code: string; data: MetaFactorAuditDetail }>('/api/audit/getMetaFactorAuditDetail?id=' + id);
}

export function getDerivativeFactorAuditDetail(id: number) {
    return RemoteRequestGet<{ code: string; data: DerivativeFactorAuditDetail }>('/api/audit/getDerivativeFactorAuditDetail?id=' + id);
}

export function getFeatureFactorAuditDetail(id: number) {
    return RemoteRequestGet<{ code: string; data: FeatureFactorAuditDetail }>('/api/audit/getFeatureFactorAuditDetail?id=' + id);
}

// ============ 连接器审核详情 ============

export interface DataFieldModel {
    fieldCode: string;
    fieldName: string;
    fieldType: string;
}

/** 基础连接器字段（对应 ConnectorModel + BaseResourceModel） */
export interface ConnectorDetailRes {
    id: number;
    resourceKey: string;
    resourceName: string;
    version: string;
    projectId: number;
    connectorType: string;
    returnType: string;
    defaultValue: string;
    exceptionValue: string;
    timeout: number;
    crtUser: string;
    crtTime: string;
}

/** JDBC 连接器详情 */
export interface JdbcConnectorDetailRes extends ConnectorDetailRes {
    dataSourceName: string;
    sql: string;
    fields: DataFieldModel[];
    condition: string;
}

/** ES 连接器详情 */
export interface EsConnectorDetailRes extends ConnectorDetailRes {
    condition: string;
    endpoint: string;
    dsl: string;
    fields: DataFieldModel[];
}

/** HTTP 连接器详情 */
export interface HttpConnectorDetailRes extends ConnectorDetailRes {
    url: string;
    method: string;
    header: Record<string, any>;
    param: Record<string, any>;
    fields: DataFieldModel[];
}

export interface JdbcConnectorAuditDetail {
    before?: JdbcConnectorDetailRes;
    after?: JdbcConnectorDetailRes;
}

export interface EsConnectorAuditDetail {
    before?: EsConnectorDetailRes;
    after?: EsConnectorDetailRes;
}

export interface HttpConnectorAuditDetail {
    before?: HttpConnectorDetailRes;
    after?: HttpConnectorDetailRes;
}

export function getJdbcConnectorAuditDetail(id: number) {
    return RemoteRequestGet<{ code: string; data: JdbcConnectorAuditDetail }>('/api/audit/getJdbcConnectorAuditDetail?id=' + id);
}

export function getEsConnectorAuditDetail(id: number) {
    return RemoteRequestGet<{ code: string; data: EsConnectorAuditDetail }>('/api/audit/getEsConnectorAuditDetail?id=' + id);
}

export function getHttpConnectorAuditDetail(id: number) {
    return RemoteRequestGet<{ code: string; data: HttpConnectorAuditDetail }>('/api/audit/getHttpConnectorAuditDetail?id=' + id);
}

// ============ Kafka Topic 审核详情 ============

export interface KafkaTopicDetailRes {
    id: string;
    name: string;
    partitions: number;
    replicas: number;
    consumerGroup?: string;
    topicStatus: string;
    remark?: string;
    auditReason?: string;
    crtUser?: string;
    crtTime?: string;
    deleted: boolean;
}

export interface KafkaTopicAuditDetail {
    before?: KafkaTopicDetailRes;
    after?: KafkaTopicDetailRes;
}

export function getKafkaTopicAuditDetail(id: number) {
    return RemoteRequestGet<{ code: string; data: KafkaTopicAuditDetail }>('/api/audit/getKafkaTopicAuditDetail?id=' + id);
}

// ============ 数据表定义 审核详情 ============

// 复用在 tableDefApi.ts 中定义的 TableColumnDetail
import type {TableColumnDetail} from '@/services/srv/tableDefApi';

export interface TableDefAuditDetailRes {
    id: string;
    tableName: string;
    dataSource: string;
    topic?: string;
    crtUser?: string;
    crtTime?: string;
}

export interface TableDefAuditDetail {
    before?: TableDefAuditDetailRes;
    after?: TableDefAuditDetailRes;
    beforeColumns?: TableColumnDetail[];
    afterColumns?: TableColumnDetail[];
}

export function getTableDefAuditDetail(id: number) {
    return RemoteRequestGet<{ code: string; data: TableDefAuditDetail }>('/api/audit/getTableDefAuditDetail?id=' + id);
}

// ============ 管道任务 审核详情 ============

export interface PipeTaskDetailRes {
    id: string;
    pointCode: string;
    taskCode: string;
    taskName: string;
    tableName: string;
    kafkaTopic: string;
    taskStatus: string;
    taskPriority: number;
    deleted: boolean;
    crtUser?: string;
    crtTime?: string;
    uptUser?: string;
    uptTime?: string;
}

export interface PipeTaskAuditDetail {
    before?: PipeTaskDetailRes;
    after?: PipeTaskDetailRes;
}

export function getPipeTaskAuditDetail(id: number) {
    return RemoteRequestGet<{ code: string; data: PipeTaskAuditDetail }>('/api/audit/getPipeTaskAuditDetail?id=' + id);
}
