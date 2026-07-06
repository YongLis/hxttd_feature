import {RemoteRequestGet, RemoteRequestPost} from '@/utils/http';

/** 条件表达式元 */
export interface ConditionMeta {
    left: string;
    op: string;
    rightType: string;
    right: string;
}

/** 列条件 */
export interface ColumnCondition {
    columns: ConditionMeta[];
}

/** 资源配置表单 */
export interface FeatureConfigForm {
    resourceKey: string;
    resourceName: string;
    version: string;
    projectId: number;
    featureCode: string;
    conditions: ColumnCondition[];
    returnType: string;
    mainDimension: string;
    slaveDimension: string;
    valueType: string;
    valueDimension: string;
    fixValue: string;
    aggregateMode: string;
    timeMode: string;
    timeUnit: string;
    timeWindow: number;
}

export interface FeatureConfigDetail {
    id: number;
    resourceKey: string;
    resourceName: string;
    version: string;
    projectId: number;
    featureCode: string;
    language: string;
    conditionScript: string;
    mainDimScript: string;
    slaveDimScript: string;
    metaFields: string;
    returnType: string;
    defaultValue?: string;
    exceptionValue?: string;
    timeout?: number;
    valueType: string;
    value?: string;
    mainDimension?: string;
    slaveDimension?: string;
    valueScript: string;
    fixValue: string;
    aggregateMode: string;
    timeMode: string;
    timeUnit: string;
    timeWindow: number;
    configForm?: FeatureConfigForm;
    crtUser: string;
    uptUser?: string;
    crtTime: string;
    uptTime?: string;
    deleted?: boolean;
}

export interface FeatureConfigItem {
    id: number;
    resourceKey: string;
    resourceName: string;
    featureCode: string;
    returnType: string;
    defaultValue: string;
    exceptionValue: string;
    timeout: number;
}

/** 分页查询特征配置 */
export function pageQueryFeatureConfig(params: any) {
    return RemoteRequestPost<{ code: string; data: FeatureConfigDetail[]; total: number; pageSize: number; current: number }>(
        '/api/feature-config/page', params);
}

/** 获取特征配置下拉列表 */
export function listFeatureConfig() {
    return RemoteRequestPost<{ code: string; data: FeatureConfigItem[] }>('/api/feature-config/list', {});
}

/** 新增特征配置 */
export function addFeatureConfig(params: any) {
    return RemoteRequestPost('/api/feature-config/add', params);
}

/** 更新特征配置 */
export function updateFeatureConfig(params: any) {
    return RemoteRequestPost('/api/feature-config/update', params);
}

/** 删除特征配置 */
export function deleteFeatureConfig(id: number) {
    return RemoteRequestPost('/api/feature-config/delete?id=' + id, {});
}

/** 获取特征配置详情（含 configForm） */
export function getFeatureConfigDetail(id: number) {
    return RemoteRequestGet<{ code: string; data: FeatureConfigDetail }>('/api/feature-config/detail?id=' + id);
}

/** 获取条件操作符列表 */
export interface ConditionOp {
    code: string;
    msg: string;
}

export function getConditionExpressOp() {
    return RemoteRequestPost<{ code: string; data: ConditionOp[] }>('/api/feature-config/getCondtionExpressOp', {});
}
