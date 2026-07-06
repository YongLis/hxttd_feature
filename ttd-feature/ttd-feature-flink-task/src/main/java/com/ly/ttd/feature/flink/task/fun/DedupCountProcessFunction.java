package com.ly.ttd.feature.flink.task.fun;

import com.ly.ttd.feature.common.enums.VelocityExpireUnitEnum;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 去重计数处理函数（动态时间窗口）
 * <p>
 * 特征事件流按 statedKey（velocityCode + masterValue + 时间标识）分区后，
 * 从每个事件中提取 expireNum + expireType 动态计算窗口时长，对 slaveValue 去重计数。
 * <ul>
 *   <li>expireType：SECOND=秒, MINUTE=分钟, HOUR=小时, DAY=天</li>
 *   <li>增量聚合：每条事件到达即更新 HashSet 累加器</li>
 *   <li>TTL 机制：新事件到达时自动延长窗口关闭时间</li>
 *   <li>事件时间：窗口关闭由水位线触发，保证乱序容忍</li>
 * </ul>
 *
 * @author yong.li
 * @since 2026/5/29
 */
public class DedupCountProcessFunction
        extends KeyedProcessFunction<String, VelEventData, Long> {

    /**
     * 去重累加器：存储不重复的 slaveValue
     */
    private ValueState<Set<VelEventData>> dedupState;

    /**
     * 当前注册的定时器时间戳
     */
    private ValueState<Long> timerState;


    /**
     * 当前已注册的交易号
     */
    private ValueState<Set<String>> registeredState;


    @Override
    public void open(OpenContext ctx) throws Exception {
        ValueStateDescriptor<Set<VelEventData>> dedupDesc =
                new ValueStateDescriptor<>("dedup-state", Set.class, VelEventData.class);
        dedupState = getRuntimeContext().getState(dedupDesc);

        ValueStateDescriptor<Long> timerDesc =
                new ValueStateDescriptor<>("timer-state", Long.class);
        timerState = getRuntimeContext().getState(timerDesc);

        ValueStateDescriptor<Set<String>> registeredDesc =
                new ValueStateDescriptor<>("registered-state", TypeInformation.of(new TypeHint<Set<String>>() {
                }));
        registeredState = getRuntimeContext().getState(registeredDesc);

    }

    @Override
    public void processElement(VelEventData event, Context ctx, Collector<Long> out)
            throws Exception {

        // ---- 增量去重：将 slaveValue 加入累加器 ----
        Set<VelEventData> dedup = dedupState.value();
        Set<String> registered = registeredState.value();
        if (dedup == null) {
            dedup = new HashSet<>();
        }
        if (StringUtils.isNotBlank(event.getSlaveValue()) && !registered.contains(event.getTxnId())) {
            dedup.add(event);
            registered.add(event.getTxnId());
        }
        Set<String> toRemove = getTimeoutElement(dedup);
        dedupState.update(dedup.stream().filter(t -> !toRemove.contains(t.getTxnId())).collect(Collectors.toSet()));
        registeredState.update(registered.stream().filter(t -> !toRemove.contains(t)).collect(Collectors.toSet()));


        // ---- 动态计算窗口时长（expireNum × expireType） ----
        long expireSeconds = (long) VelocityExpireUnitEnum.getSecondByType(event.getTimeUnit());
        long windowMillis = event.getTimeWindow() != null
                ? (long) event.getTimeWindow() * expireSeconds * 1000L
                : expireSeconds * 1000L;
        long fireTime = ctx.timestamp() + windowMillis;

        // ---- TTL 延长：新事件到达时推迟定时器 ----
        Long currentTimer = timerState.value();
        if (currentTimer == null || fireTime > currentTimer) {
            if (currentTimer != null) {
                ctx.timerService().deleteEventTimeTimer(currentTimer);
            }
            ctx.timerService().registerEventTimeTimer(fireTime);
            timerState.update(fireTime);
        }
    }

    private Set<String> getTimeoutElement(Set<VelEventData> dedup) {
        return dedup.stream()
                .filter(t -> t.getExpireTime().compareTo(new Date()) < 0)
                .map(VelEventData::getTxnId)
                .collect(Collectors.toSet());
    }


    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<Long> out)
            throws Exception {
        Set<VelEventData> dedup = dedupState.value();
        long count = dedup != null ? dedup.stream().map(VelEventData::getSlaveValue).collect(Collectors.toSet()).size() : 0L;
        out.collect(count);

        // 清理状态
        dedupState.clear();
        timerState.clear();
        registeredState.clear();
    }
}
