package com.ly.ttd.feature.srv.data;

import com.ly.ttd.utils.Md5Util;
import lombok.Data;

/**
 * 轨迹数据
 *
 * @author yong.li
 * @since 2026/6/15 14:34
 */
@Data
public class TraceMessage {

    private String topic;
    private String messageId;
    private String message;

    public TraceMessage(String topic, String message) {
        this.topic = topic;
        this.messageId = Md5Util.MD5(message);
        this.message = message;
    }
}
