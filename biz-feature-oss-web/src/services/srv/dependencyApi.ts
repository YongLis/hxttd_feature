import {RemoteRequestPost} from '@/utils/http';

/** 血缘节点 */
export interface DependencyNode {
    nodeId: string;
    nodeType: string;
    label: string;
}

/** 血缘边 */
export interface DependencyEdge {
    edgeId: string;
    from: string;
    to: string;
}

/** 血缘查询响应 */
export interface DependencyQueryRes {
    nodes: DependencyNode[];
    edges: DependencyEdge[];
}

/**
 * 查询资源血缘关系
 * @param resourceKey 资源标识键
 * @param resourceType 资源类型 (FACTOR/META/FEATURE 等)
 */
export function queryDependency(resourceKey: string, resourceType: string) {
    return RemoteRequestPost<DependencyQueryRes>('/api/dependency/get', {
        resourceKey: resourceKey,
        resourceType: resourceType,
    });
}
