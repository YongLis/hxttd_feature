import {RemoteRequestPost} from '@/utils/http';

export interface MetaFieldDetail {
  id: number;
  resourceKey: string;
  resourceName: string;
  version: string;
  projectId: number;
  language: string;
  script: string;
  returnType: string;
  defaultValue: string;
  exceptionValue: string;
  timeout: number;
  categoryTag: string;
  crtUser: string;
  crtTime: string;
}

export interface PageResult<T> {
  code: string;
  data: T[];
  total: number;
  pageSize: number;
  current: number;
}

/**
 * 分页查询元字段
 */
export function pageQueryMetaField(params: any) {
  return RemoteRequestPost<PageResult<MetaFieldDetail>>('/api/meta-field/page', params);
}

/**
 * 根据ID查询元字段详情
 */
export function getMetaFieldById(id: number) {
  return RemoteRequestPost<{ code: string; data: MetaFieldDetail }>('/api/meta-field/detail?id=' + id, {});
}

/**
 * 添加元字段
 */
export function addMetaField(params: any) {
  return RemoteRequestPost('/api/meta-field/add', params);
}

/**
 * 测试元字段
 */
export function testMetaField(params: any) {
  return RemoteRequestPost('/api/meta-field/test', params);
}

/**
 * 查询测试数据
 */
export function queryTestData(params: any) {
  return RemoteRequestPost('/api/meta-field/queryTestData', params);
}

/**
 * 更新元字段
 */
export function updateMetaField(params: any) {
  return RemoteRequestPost('/api/meta-field/update', params);
}

/**
 * 删除元字段
 */
export function deleteMetaField(id: number) {
  return RemoteRequestPost('/api/meta-field/delete?id=' + id, {});
}

/**
 * 测试结果类型
 */
export interface TestCaseResult {
  success: boolean;
  result?: any;
  resultType?: string;
  errorMessage?: string;
  executionTime: number;
  resourceKey: string;
  resourceName: string;
  language: string;
}

/**
 * 获取全部元字段（下拉列表用）
 */
export interface MetaFieldOption {
  resourceKey: string;
  resourceName: string;
}

export function getAllMetaField() {
  return RemoteRequestPost<{ code: string; data: MetaFieldOption[] }>('/api/meta-field/getAllMeta', {});
}
