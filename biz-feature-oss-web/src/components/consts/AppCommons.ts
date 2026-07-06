import {
    FeatureAggregateEnum,
    FeatureAggregateLabel,
    FeatureConfigOpEnum,
    FeatureConfigOpLabel,
    FeatureTimeUnitEnum,
    FeatureTimeUnitLabel,
    FeatureTimeWindowLabel,
    FeatureTimeWindowTypeEnum,
    HttpMethodTypeEnum,
    ObjectReturnTypeEnum,
    ObjectReturnTypeLabel,
    ResourceTypeEnum,
    ResourceTypeEnumLabel,
} from './AppEnum';

/** 操作符选项 */
export const OP_OPTIONS = Object.values(FeatureConfigOpEnum).map((code) => ({
    value: code,
    label: FeatureConfigOpLabel[code],
}));

/** 聚合函数选项 */
export const AGGREGATE_OPTIONS = Object.values(FeatureAggregateEnum).map((code) => ({
    value: code,
    label: FeatureAggregateLabel[code],
}));

/** 时间窗口类型选项 */
export const TIME_WINDOW_OPTIONS = Object.values(FeatureTimeWindowTypeEnum).map((code) => ({
    value: code,
    label: FeatureTimeWindowLabel[code],
}));

/** 时间单位选项 */
export const TIME_UNIT_OPTIONS = Object.values(FeatureTimeUnitEnum).map((code) => ({
    value: code,
    label: FeatureTimeUnitLabel[code],
}));

/** 返回值类型选项 */
export const RETURN_TYPE_OPTIONS = Object.values(ObjectReturnTypeEnum).map((code) => ({
    value: code,
    label: ObjectReturnTypeLabel[code],
}));

export const HTTP_METHOD_OPTIONS = Object.values(HttpMethodTypeEnum).map((code) => ({
    value: code,
    label: code,
}));

// 资源类型选项
export const RESOURCE_TYPE_OPTIONS = Object.values(ResourceTypeEnum).map((code) => ({
    value: code,
    label: ResourceTypeEnumLabel[code],
}));