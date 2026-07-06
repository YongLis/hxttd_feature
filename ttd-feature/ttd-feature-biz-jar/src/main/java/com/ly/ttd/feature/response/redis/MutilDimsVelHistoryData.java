package com.ly.ttd.feature.response.redis;

import com.ly.ttd.feature.common.event.dto.VelValueItem;

import java.util.*;

/**
 * 多维实时特征累计
 *
 * @author yong.li
 * @since 2026/3/18 16:38
 */
public class MutilDimsVelHistoryData {
    private String redisKey;

    private Map<String, List<VelValueItem>> items;

    private Date expireTime;

    public MutilDimsVelHistoryData(String redisKey, Date expireTime) {
        this.redisKey = redisKey;
        this.expireTime = expireTime;
        items = new HashMap<>();
    }

    public void addItem(String key, VelValueItem item) {
        List<VelValueItem> list = items.computeIfAbsent(key, k -> new ArrayList<>());
        list.add(item);
        items.put(key, list);
    }


    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public Map<String, List<VelValueItem>> getItems() {
        return items;
    }

    public void setItems(Map<String, List<VelValueItem>> items) {
        this.items = items;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
