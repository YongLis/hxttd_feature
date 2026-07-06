import type {Request, Response} from 'express';

// =============================================
//  模拟数据
// =============================================

// 接入点请求入参（树形结构）
// id=0 返回默认模板参数，id=1 返回具体接入点参数
const accessPointParams: Record<number, any[]> = {
    0: [
        {
            id: 201,
            paramName: '交易号',
            paramCode: 'bizOrderNo',
            paramType: 'STRING',
            required: 1,
            defaultValue: '',
            description: '交易号',
            sortOrder: 0,
            parentId: null,
            children: [],
        },
        {
            id: 202,
            paramName: '接入点编码',
            paramCode: 'pointCode',
            paramType: 'STRING',
            required: 1,
            defaultValue: '',
            description: '接入点编码',
            sortOrder: 1,
            parentId: null,
            children: [],
        },
        {
            id: 203,
            paramName: '请求参数',
            paramCode: 'req',
            paramType: 'DICT',
            required: 0,
            defaultValue: '',
            description: '请求参数',
            sortOrder: 2,
            parentId: null,
            children: [],
        },
    ],
    1: [
        {
            id: 101,
            paramName: '用户ID',
            paramCode: 'userId',
            paramType: 'STRING',
            required: 1,
            defaultValue: '',
            description: '用户唯一标识',
            sortOrder: 0,
            parentId: null,
            children: [
                {
                    id: 102,
                    paramName: '用户ID类型',
                    paramCode: 'userIdType',
                    paramType: 'STRING',
                    required: 0,
                    defaultValue: 'UID',
                    description: '用户ID类型枚举',
                    sortOrder: 0,
                    parentId: 101,
                    children: [
                        {
                            id: 103,
                            paramName: 'ID类型编码',
                            paramCode: 'idTypeCode',
                            paramType: 'STRING',
                            required: 0,
                            defaultValue: '',
                            description: '',
                            sortOrder: 0,
                            parentId: 102,
                            children: [],
                        },
                    ],
                },
            ],
        },
        {
            id: 104,
            paramName: '场景编码',
            paramCode: 'sceneCode',
            paramType: 'STRING',
            required: 1,
            defaultValue: 'DEFAULT',
            description: '业务场景编码',
            sortOrder: 1,
            parentId: null,
            children: [],
        },
        {
            id: 105,
            paramName: '请求时间',
            paramCode: 'requestTime',
            paramType: 'DATE',
            required: 0,
            defaultValue: '',
            description: '请求发起时间',
            sortOrder: 2,
            parentId: null,
            children: [],
        },
        {
            id: 106,
            paramName: '分页参数',
            paramCode: 'pageParam',
            paramType: 'STRING',
            required: 0,
            defaultValue: '',
            description: '分页相关参数组',
            sortOrder: 3,
            parentId: null,
            children: [
                {
                    id: 107,
                    paramName: '页码',
                    paramCode: 'pageNo',
                    paramType: 'NUMBER',
                    required: 0,
                    defaultValue: '1',
                    description: '当前页码',
                    sortOrder: 0,
                    parentId: 106,
                    children: [],
                },
                {
                    id: 108,
                    paramName: '每页条数',
                    paramCode: 'pageSize',
                    paramType: 'NUMBER',
                    required: 0,
                    defaultValue: '20',
                    description: '每页记录数',
                    sortOrder: 1,
                    parentId: 106,
                    children: [],
                },
            ],
        },
    ],
};

// =============================================
//  接入点参数查询 Mock
// =============================================

/**
 * 查询接入点请求入参（树形结构）
 * POST /api/access-point/params?id=1
 */
function queryAccessPointParams(req: Request, res: Response) {
    const id = Number(req.query.id);

    const params = accessPointParams[id];
    if (!params) {
        // 无参数时返回空数组
        res.json({code: '0000', message: 'success', data: []});
        return;
    }

    res.json({code: '0000', message: 'success', data: params});
}

// =============================================
//  Export
// =============================================
export default {
    'POST /api/access-point/params': queryAccessPointParams,
};
