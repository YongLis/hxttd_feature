import {RemoteRequestPost} from '@/utils/http';

export interface TestCaseDetail {
    id: number;
    metaFieldCode: string;
    caseType: string;
    bizOrderNo: string;
    caseContent: string;
    targetValue: string;
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

/** 分页查询测试用例 */
export function pageQueryTestCase(params: any) {
    return RemoteRequestPost<PageResult<TestCaseDetail>>('/api/test-case/page', params);
}

/** 新增测试用例 */
export function addTestCase(params: any) {
    return RemoteRequestPost('/api/test-case/add', params);
}

/** 更新测试用例 */
export function updateTestCase(params: any) {
    return RemoteRequestPost('/api/test-case/update', params);
}

/** 删除测试用例 */
export function deleteTestCase(id: number) {
    return RemoteRequestPost('/api/test-case/delete?id=' + id, {});
}
