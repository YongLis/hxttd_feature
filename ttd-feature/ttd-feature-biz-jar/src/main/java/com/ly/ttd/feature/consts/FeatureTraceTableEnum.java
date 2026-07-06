package com.ly.ttd.feature.consts;

/**
 * @author yong.li
 * @since 2026/3/27 11:37
 */
public enum FeatureTraceTableEnum {
    BIZ_VEL_RECORD("biz_vel_record", "biz_vel_record_topic"),
    VEL_HISTORY_SNAPSHOT("vel_history_snapshot", "vel_history_snapshot_topic"),
    VEL_MASTER_VALUE_RECORD("vel_master_value_record", "vel_master_value_record_topic"),
    VEL_MONITOR_DAILY("vel_monitor_daily", "vel_monitor_daily_topic"),
    VEL_READ_SNAPSHOT("vel_read_snapshot", "vel_read_snapshot_topic"),
    VEL_EVENT_DATA("vel_event_data", "vel_event_data_topic"),
    CONNECTOR_TRACE("connector_trace", "connector_trace_topic"),
    ;
    private String dorisTable;
    private String kafkaTopic;

    private FeatureTraceTableEnum(String dorisTable, String kafkaTopic) {
        this.dorisTable = dorisTable;
        this.kafkaTopic = kafkaTopic;
    }

    public String getDorisTable() {
        return dorisTable;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }
}
