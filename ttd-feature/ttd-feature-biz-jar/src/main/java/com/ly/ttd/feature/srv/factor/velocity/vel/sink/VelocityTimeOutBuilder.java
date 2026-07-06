package com.ly.ttd.feature.srv.factor.velocity.vel.sink;

import com.ly.ttd.feature.common.enums.VelocityExpireUnitEnum;
import com.ly.ttd.feature.common.enums.VelocityTimeModeEnum;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.utils.DateUtil;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * 实时特征redis key构建器
 *
 * @author yong.li
 * @since 2026/4/23 17:27
 */
public class VelocityTimeOutBuilder {

    /**
     * 缓存60秒
     */
    public static int CACHE_60_SECOND = 60;

    /**
     * 缓存120秒
     */
    public static int CACHE_120_SECOND = 120;
    /**
     * 缓存180秒
     */
    public static int CACHE_180_SECOND = 180;

    /**
     * 根据交易时间获取当前周期的最小时间
     *
     * @param dto 交易数据
     * @return 最小时间戳
     */
    public static long getWindowMinTime(VelEventData dto) {
        return dto.getTxnTime().getTime() - (long) dto.getTimeWindow() * VelocityExpireUnitEnum.getSecondByType(dto.getTimeUnit()) * 1000;
    }

    /**
     * 根据交易时间获取当前周期的最大时间
     *
     * @param dto 交易数据
     * @return 最大时间戳
     */
    public static long getWindowMaxTime(VelEventData dto) {
        return dto.getTxnTime().getTime() + (long) dto.getTimeWindow() * VelocityExpireUnitEnum.getSecondByType(dto.getTimeUnit()) * 1000;
    }

    /**
     * 根据当前交易时间获取周期的过期时间
     */
    public static Date getWindowExpireTime(VelEventData dto) {
        if (VelocityTimeModeEnum.TTL.getType().equals(dto.getTimeMode())) {
            return DateUtils.addSeconds(dto.getTxnTime(), dto.getTimeWindow() * VelocityExpireUnitEnum.getSecondByType(dto.getTimeUnit()));
        } else if (VelocityTimeModeEnum.CURRENT_DAY.getType().equals(dto.getTimeMode())) {
            // 自然日，过期时间返回下一天0点0分0秒
            Date nexDate = DateUtils.addDays(dto.getTxnTime(), 1);
            return DateUtil.parse("yyyy-MM-dd", DateUtil.format("yyyy-MM-dd", nexDate));
        } else if (VelocityTimeModeEnum.CURRENT_MONTH.getType().equals(dto.getTimeMode())) {
            // 自然月，过期时间返回下个月1日0点0分0秒
            Date nexDate = DateUtils.addMonths(dto.getTxnTime(), 1);
            return DateUtil.parse("yyyy-MM-dd", DateUtil.format("yyyy-MM", nexDate) + "-01");
        } else if (VelocityTimeModeEnum.CURRENT_YEAR.getType().equals(dto.getTimeMode())) {
            // 自然年，过期时间返回明年1月1日0点0分0秒
            Date nexDate = DateUtils.addYears(dto.getTxnTime(), 1);
            return DateUtil.parse("yyyy-MM-dd", DateUtil.format("yyyy", nexDate) + "-01-01");
        } else {
            Date nexDate = DateUtils.addYears(dto.getTxnTime(), 99);
            return DateUtil.parse("yyyy-MM-dd", DateUtil.format("yyyy", nexDate) + "-01-01");
        }
    }


    public static Integer getCacheTime(VelEventData dto) {
        if (VelocityTimeModeEnum.FOREVER.getType().equals(dto.getTimeMode())) {
            return 10 * 365 * 24 * 3600;
        } else {
            return dto.getTimeWindow() * VelocityExpireUnitEnum.getSecondByType(dto.getTimeUnit());
        }
    }

}
