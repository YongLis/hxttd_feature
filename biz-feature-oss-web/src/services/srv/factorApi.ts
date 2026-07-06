import {RemoteRequestPost} from '@/utils/http';

export interface FactorDetail {
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
    resourceJson?: string;
}

export interface DerivativeFactorModel {
    factorCodes: string[];
    connectorType: string;
    connectorCode: string;
    params: { fieldCode: string; sourceCode: string }[];
    language: string;
    conditionScript: string;
    script: string;
}

export interface PageResult<T> {
    code: string;
    data: T[];
    total: number;
    pageSize: number;
    current: number;
}

export interface SelectedConnector {
    connectorCode: string;
    connectorType: string;
    connectorParam: string[];
}


export function pageQueryFactor(params: any) {
    return RemoteRequestPost<PageResult<FactorDetail>>('/api/factor/page', params);
}

export function addMetaFactor(params: any) {
    return RemoteRequestPost('/api/factor/addMetaFactor', params);
}

export function addDerivativeFactor(params: any) {
    return RemoteRequestPost('/api/factor/addDerivativeFactor', params);
}

export function addFeatureFactor(params: any) {
    return RemoteRequestPost('/api/factor/addFeatureFactor', params);
}


export function updateMetaFactor(params: any) {
    return RemoteRequestPost('/api/factor/updateMetaFactor', params);
}

export function updateDerivativeFactor(params: any) {
    return RemoteRequestPost('/api/factor/updateDerivativeFactor', params);
}

export function updateFeatureFactor(params: any) {
    return RemoteRequestPost('/api/factor/updateFeatureFactor', params);
}

export function deleteFactor(id: number, factorType: string) {
    return RemoteRequestPost('/api/factor/delete?id=' + id + '&factorType=' + factorType, {});
}
