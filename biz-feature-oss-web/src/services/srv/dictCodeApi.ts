import {RemoteRequestPost} from '@/utils/http';

export interface DictCodeDetail {
    id: number;
    dictId: number;
    dictKey: string;
    dictValue: string;
    deleted: boolean;
    crtTime?: string;
    crtUser?: string;
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
 * 分页查询字典键值
 */
export function pageQueryDictCode(params: any) {
    return RemoteRequestPost<PageResult<DictCodeDetail>>('/api/dict-code/page', params);
}

/**
 * 添加字典键值
 */
export function addDictCode(params: any) {
    return RemoteRequestPost('/api/dict-code/add', params);
}

/**
 * 更新字典键值
 */
export function updateDictCode(params: any) {
    return RemoteRequestPost('/api/dict-code/update', params);
}

/**
 * 删除字典键值
 */
export function deleteDictCode(id: number) {
    return RemoteRequestPost('/api/dict-code/delete?id=' + id, {});
}

// 查询字典
export async function getDictCode(systemCode: string, dictCode: string) {
    return RemoteRequestPost('/api/dict-code/getDictCode', {'systemCode': systemCode, 'dictCode': dictCode})
}

export async function getDictCodeOptions(systemCode: string, dictCode: string) {
    return getDictCode(systemCode, dictCode)
        .then(res => {
            let code = res.code;
            if ('0000' === code) {
                let data = res.data as DictCodeDetail[]
                return data.map(t => ({'label': t.dictValue, 'value': t.dictKey}));
            } else {
                alert(res.message)
                return []
            }

        }).catch(e => {
            alert(e.message);
            return []
        })
}
