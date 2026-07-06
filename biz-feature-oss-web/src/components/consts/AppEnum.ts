export enum FeatureConfigOpEnum {
    /** 等于 */
    EQUALS = '==',
    /** 不等于 */
    NOT_EQUALS = '!=',
    /** 包含 */
    CONTAIN = 'contains',
    /** 大于 */
    GREATER_THAN = '>',
    /** 大于等于 */
    GREATER_THAN_OR_EQUAL = '>=',
    /** 小于 */
    LESS_THAN = '<',
    /** 小于等于 */
    LESS_THAN_OR_EQUAL = '<=',
    /** 在范围内 */
    IN = 'in',
    /** 不在范围内 */
    NOT_IN = 'not_in',
    /**存在交集*/
    EXIST = 'exist'
}

/** 条件操作符中文映射 */
export const FeatureConfigOpLabel: Record<FeatureConfigOpEnum, string> = {
    [FeatureConfigOpEnum.EQUALS]: '等于',
    [FeatureConfigOpEnum.NOT_EQUALS]: '不等于',
    [FeatureConfigOpEnum.CONTAIN]: '包含',
    [FeatureConfigOpEnum.GREATER_THAN]: '大于',
    [FeatureConfigOpEnum.GREATER_THAN_OR_EQUAL]: '大于等于',
    [FeatureConfigOpEnum.LESS_THAN]: '小于',
    [FeatureConfigOpEnum.LESS_THAN_OR_EQUAL]: '小于等于',
    [FeatureConfigOpEnum.IN]: '在范围内',
    [FeatureConfigOpEnum.NOT_IN]: '不在范围内',
    [FeatureConfigOpEnum.EXIST]: '存在交集',
};


/** 特征聚合函数 */
export enum FeatureAggregateEnum {
    /** 求和 */
    SUM = 'SUM',
    /** 求平均 */
    AVG = 'AVG',
    /** 求最大 */
    MAX = 'MAX',
    /** 求最小 */
    MIN = 'MIN',
    /** 去重计数 */
    COUNT = 'COUNT',
}

/** 特征聚合函数中文映射 */
export const FeatureAggregateLabel: Record<FeatureAggregateEnum, string> = {
    [FeatureAggregateEnum.SUM]: '求和',
    [FeatureAggregateEnum.AVG]: '求平均',
    [FeatureAggregateEnum.MAX]: '求最大',
    [FeatureAggregateEnum.MIN]: '求最小',
    [FeatureAggregateEnum.COUNT]: '去重计数',
};


/** 时间窗口类型 */
export enum FeatureTimeWindowTypeEnum {
    /** 过期时间 */
    TTL = 'TTL',
    /** 自然日 */
    DAY = 'DAY',
    /** 自然月 */
    MONTH = 'MONTH',
    /** 自然年 */
    YEAR = 'YEAR',
    FOREVER = 'FOREVER'
}

/** 特征聚合函数中文映射 */
export const FeatureTimeWindowLabel: Record<FeatureTimeWindowTypeEnum, string> = {
    [FeatureTimeWindowTypeEnum.TTL]: '过期时间',
    [FeatureTimeWindowTypeEnum.DAY]: '自然日',
    [FeatureTimeWindowTypeEnum.MONTH]: '自然月',
    [FeatureTimeWindowTypeEnum.YEAR]: '自然年',
    [FeatureTimeWindowTypeEnum.FOREVER]: '永久',
};

/** 返回值类型 */
export enum ObjectReturnTypeEnum {
    /** 列表 */
    LIST = 'LIST',
    /** 整数 */
    LONG = 'LONG',
    /** 小数 */
    DOUBLE = 'DOUBLE',
    /** 金额 */
    DECIMAL = 'DECIMAL',
    /** 日期 */
    DATE = 'DATE',
    /** 字典 */
    DICT = 'DICT',
    /** 字符串 */
    STRING = 'STRING',
}

/** 返回值类型中文映射 */
export const ObjectReturnTypeLabel: Record<ObjectReturnTypeEnum, string> = {
    [ObjectReturnTypeEnum.LIST]: '列表',
    [ObjectReturnTypeEnum.LONG]: '整数',
    [ObjectReturnTypeEnum.DOUBLE]: '小数',
    [ObjectReturnTypeEnum.DECIMAL]: '金额',
    [ObjectReturnTypeEnum.DATE]: '日期',
    [ObjectReturnTypeEnum.DICT]: '字典',
    [ObjectReturnTypeEnum.STRING]: '字符串',
};


const ObjectReturnTypeMap: Record<string, string> = {
    'LIST': '列表',
    'LONG': '整数',
    'DOUBLE': '小数',
    'DECIMAL': '金额',
    'DATE': '日期',
    'DICT': '字典',
    'STRING': '字符串',
};


/** 根据返回值类型获取默认值、异常值 */
export const getObjectReturnDefaultValue = (value: string) => {
    console.log(value);
    // 根据返回值类型获取默认值、异常值
    switch (value) {
        case ObjectReturnTypeEnum.LIST:
            return '[]';
        case ObjectReturnTypeEnum.LONG:
            return '0';
        case ObjectReturnTypeEnum.DOUBLE:
            return '0.0';
        case ObjectReturnTypeEnum.DECIMAL:
            return '0.00';
        case ObjectReturnTypeEnum.DATE:
            return '2099-01-01 00:00:00';
        case ObjectReturnTypeEnum.DICT:
            return '{}';
        case ObjectReturnTypeEnum.STRING:
            return '-999999';
        default:
            return '';
    }
};

/** 时间单位 */
export enum FeatureTimeUnitEnum {
    /** 秒 */
    SECOND = 'S',
    /** 分钟 */
    MINUTE = 'M',
    /** 小时 */
    HOUR = 'H',
    /** 天 */
    DAY = 'D',
}

/** 时间单位中文映射 */
export const FeatureTimeUnitLabel: Record<FeatureTimeUnitEnum, string> = {
    [FeatureTimeUnitEnum.SECOND]: '秒',
    [FeatureTimeUnitEnum.MINUTE]: '分钟',
    [FeatureTimeUnitEnum.HOUR]: '小时',
    [FeatureTimeUnitEnum.DAY]: '天',
};

/** HTTP方法类型 */
export enum HttpMethodTypeEnum {
    /** GET */
    GET = 'GET',
    /** POST */
    POST = 'POST',
    /** DELETE */
    DELETE = 'DELETE',
    /** PUT */
    PUT = 'PUT'
}


/** HTTP方法类型中文映射 */
export const HttpMethodTypeLabel: Record<HttpMethodTypeEnum, string> = {
    [HttpMethodTypeEnum.GET]: 'GET',
    [HttpMethodTypeEnum.POST]: 'POST',
    [HttpMethodTypeEnum.DELETE]: 'DELETE',
    [HttpMethodTypeEnum.PUT]: 'PUT',
};


export enum ResourceTypeEnum {
    POINT = '接入点',
    META = '元数据',
    FACTOR_META = '元字段指标',
    FACTOR_DERIVATIVE = '衍生指标',
    FACTOR_FEATURE = '实时特征指标',
    FEATURE = '特征',
    DATA_STRUCT = '数据集',
    CONNECTOR_JDBC = 'JDBC连接',
    CONNECTOR_HTTP = 'HTTP连接',
    CONNECTOR_ES = 'ES连接',
}

/** 时间单位中文映射 */
export const ResourceTypeEnumLabel: Record<ResourceTypeEnum, string> = {
    [ResourceTypeEnum.POINT]: '接入点',
    [ResourceTypeEnum.META]: '元数据',
    [ResourceTypeEnum.FACTOR_META]: '元字段指标',
    [ResourceTypeEnum.FACTOR_DERIVATIVE]: '衍生指标',
    [ResourceTypeEnum.FACTOR_FEATURE]: '实时特征指标',
    [ResourceTypeEnum.FEATURE]: '特征',
    [ResourceTypeEnum.DATA_STRUCT]: '数据集',
    [ResourceTypeEnum.CONNECTOR_JDBC]: 'JDBC连接',
    [ResourceTypeEnum.CONNECTOR_HTTP]: 'HTTP连接',
    [ResourceTypeEnum.CONNECTOR_ES]: 'ES连接',
};

export const ResourceTypeMap: Record<string, string> = {
    POINT: '接入点',
    META: '元数据',
    FACTOR_META: '元字段指标',
    FACTOR_DERIVATIVE: '衍生指标',
    FACTOR_FEATURE: '实时特征指标',
    FACTOR: '指标',
    FEATURE: '特征',
    DATA_STRUCT: '数据集',
    // CONNECTOR: '连接',
    CONNECTOR_JDBC: 'JDBC连接器',
    CONNECTOR_ES: 'ES连接器',
    CONNECTOR_HTTP: 'HTTP连接器',
};

export const ResourceTypePrefixMap: Record<string, string> = {
    POINT: 'A',
    META: 'M',
    FACTOR_META: 'F',
    FACTOR_DERIVATIVE: 'F',
    FACTOR_FEATURE: 'F',
    FEATURE: 'V',
    DATA_STRUCT: 'D',
    CONNECTOR: 'C',
    CONNECTOR_JDBC: 'C',
    CONNECTOR_ES: 'C',
    CONNECTOR_HTTP: 'C',
};


export enum FactorTypeEnum {
    META = '元字段指标',
    DERIVATIVE = '衍生指标',
    FEATURE = '实时特征指标',
}

export const FactorTypeColorMap: Record<string, string> = {
    META: 'blue',
    DERIVATIVE: 'purple',
    FEATURE: 'orange',
};
export const FactorTypeLabelMap: Record<string, string> = {
    META: '基础指标',
    DERIVATIVE: '衍生指标',
    FEATURE: '特征指标',
};


export enum DeletedEnum {
    true = '无效',
    false = '有效',
}