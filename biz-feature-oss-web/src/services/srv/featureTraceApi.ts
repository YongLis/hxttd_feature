import {RemoteRequestPost} from '@/utils/http';

export interface FeatureTraceDetail {
    id: number;
    eventId: string;
    eventType: string;
    operationType: string;
    body: string;
    status: string;
    retryCount: number;
    errorMessage: string;
    crtUser: string;
    crtTime: string;
}

/** 分页查询特征溯源(只读) */
export function pageQueryFeatureTrace(params: any) {
    return RemoteRequestPost<{ code: string; data: FeatureTraceDetail[]; total: number; pageSize: number; current: number }>(
        '/api/feature-trace/page', params);
}
