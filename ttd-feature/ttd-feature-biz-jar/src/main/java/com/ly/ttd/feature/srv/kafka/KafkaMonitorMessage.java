package com.ly.ttd.feature.srv.kafka;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.utils.Md5Util;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

/**
 * @author yong.li
 * @since 2026/7/2 15:49
 */
@Data
public class KafkaMonitorMessage {

    /**
     * 消息ID
     */
    private String message_id;
    /**
     * 表名
     */
    private String table_name;
    /**
     * 表数据
     */
    private Map<String, Object> table_data;

    public KafkaMonitorMessage(String table_name, Map<String, Object> table_data) {
        this.message_id = Md5Util.MD5(table_name + JSON.toJSONString(table_data) + UUID.randomUUID().toString());
        this.table_name = table_name;
        this.table_data = table_data;
    }
}
