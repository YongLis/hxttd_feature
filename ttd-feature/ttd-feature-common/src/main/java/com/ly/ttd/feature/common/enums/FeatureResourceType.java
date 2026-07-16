package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 特征平台资源类型
 *
 * @author yong.li
 * @since 2026/5/23 20:56
 */
@Getter
@AllArgsConstructor
public enum FeatureResourceType {
    POINT("POINT", "元字段", "A"),
    META_FIELD("META", "元字段", "M"),
    FACTOR_META("FACTOR_META", "元数据指标", "F"),
    FACTOR_DERIVATIVE("FACTOR_DERIVATIVE", "衍生指标", "F"),
    FACTOR_FEATURE("FACTOR_FEATURE", "实时特征指标", "F"),
    FEATURE_CONFIG("FEATURE", "实时特征配置", "V"),
    DATA_STRUCT("DATA_STRUCT", "数据结构", "D"),
    CONNECTOR_JDBC("CONNECTOR_JDBC", "连接器", "C"),
    CONNECTOR_ES("CONNECTOR_ES", "连接器", "C"),
    CONNECTOR_HTTP("CONNECTOR_HTTP", "连接器", "C"),
    KAFKA_TOPIC("KAFKA_TOPIC", "Kafka主题", "T"),
    TABLE_DEF("TABLE_DEF", "表定义", "E"),
    PIPE_TASK("PIPE_TASK", "管道任务", "P"),


    ;

    private String type;
    private String desc;
    private String prefix;


    public static String getTypeByCodePrefix(String codePrefix, String code) {
        for (FeatureResourceType resourceType : values()) {
            String prefix = String.format("%s_%s_", resourceType.getPrefix(), resourceType.getPrefix());
            if (code.startsWith(prefix)) {
                return resourceType.getType();
            }
        }
        return null;

    }
}
