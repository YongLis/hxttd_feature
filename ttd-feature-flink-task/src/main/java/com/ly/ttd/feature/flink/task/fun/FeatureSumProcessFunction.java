package com.ly.ttd.feature.flink.task.fun;

import com.ly.ttd.feature.common.enums.VelocityExpireUnitEnum;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 求和处理函数（动态时间窗口）
 * <p>
 * 按 statedKey 分区，对 velValue 进行累加求和，
 * 通过 expireNum + expireType 动态计算窗口时长。
 *
 * @author yong.li
 * @since 2026/5/30
 */
public class FeatureSumProcessFunction
        extends KeyedProcessFunction<String, VelEventData, BigDecimal> {

    /**
     * 累加和
     */
    private ValueState<BigDecimal> sumState;

    /**
     * 当前已处理的事件（用于过期清理）
     */
    private ValueState<Set<VelEventData>> eventState;

    /**
     * 已注册的交易号
     */
    private ValueState<Set<String>> registeredState;

    /**
     * 定时器时间戳
     */
    private ValueState<Long> timerState;

    @Override
    public void open(OpenContext ctx) throws Exception {
        sumState = getRuntimeContext().getState(
                new ValueStateDescriptor<>("sum-state", BigDecimal.class));
        eventState = getRuntimeContext().getState(
                new ValueStateDescriptor<>("event-state", TypeInformation.of(new TypeHint<Set<VelEventData>>() {
                })));
        registeredState = getRuntimeContext().getState(
                new ValueStateDescriptor<>("registered-state", TypeInformation.of(new TypeHint<Set<String>>() {
                })));
        timerState = getRuntimeContext().getState(
                new ValueStateDescriptor<>("timer-state", Long.class));
    }

    @Override
    public void processElement(VelEventData event, Context ctx, Collector<BigDecimal> out)
            throws Exception {

        Set<VelEventData> events = eventState.value();
        Set<String> registered = registeredState.value();
        if (events == null) events = new HashSet<>();
        if (registered == null) registered = new HashSet<>();

        // ---- 去重：同一 txnId 只处理一次 ----
        if (!registered.contains(event.getTxnId())) {
            events.add(event);
            registered.add(event.getTxnId());
        }

        // ---- 清理过期事件 ----
        Set<String> toRemove = getTimeoutElements(events);
        events = events.stream()
                .filter(t -> !toRemove.contains(t.getTxnId()))
                .collect(Collectors.toSet());
        registered = registered.stream()
                .filter(t -> !toRemove.contains(t))
                .collect(Collectors.toSet());
        eventState.update(events);
        registeredState.update(registered);

        // ---- 重算累加和 ----
        BigDecimal sum = events.stream()
                .map(e -> e.getVelValue() != null ? e.getVelValue() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        sumState.update(sum);

        // ---- 定时器 ----
        long expireSeconds = (long) VelocityExpireUnitEnum.getSecondByType(event.getTimeUnit());
        long windowMillis = event.getTimeWindow() != null
                ? (long) event.getTimeWindow() * expireSeconds * 1000L
                : expireSeconds * 1000L;
        long fireTime = ctx.timestamp() + windowMillis;

        Long currentTimer = timerState.value();
        if (currentTimer == null || fireTime > currentTimer) {
            if (currentTimer != null) {
                ctx.timerService().deleteEventTimeTimer(currentTimer);
            }
            ctx.timerService().registerEventTimeTimer(fireTime);
            timerState.update(fireTime);
        }
    }

    private Set<String> getTimeoutElements(Set<VelEventData> events) {
        Date now = new Date();
        return events.stream()
                .filter(t -> t.getExpireTime() != null && t.getExpireTime().compareTo(now) < 0)
                .map(VelEventData::getTxnId)
                .collect(Collectors.toSet());
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<BigDecimal> out)
            throws Exception {
        BigDecimal sum = sumState.value();
        out.collect(sum != null ? sum : BigDecimal.ZERO);

        sumState.clear();
        eventState.clear();
        registeredState.clear();
        timerState.clear();
    }
}
