package com.ly.ttd.feature.srv.factor.velocity.vel.sink;


import com.ly.ttd.feature.common.enums.VelocityTimeModeEnum;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.utils.DateUtil;

/**
 * 实时特征redis key构建器
 *
 * @author yong.li
 * @since 2026/4/23 17:27
 */
public class VelocityRedisKeyBuilder {

    protected static String TXN_VEL_ITEM_TTL = "pay:rcs:txn:vel:%s:%s";
    protected static String TXN_VEL_ITEM_CALENDAR = "pay:rcs:txn:vel:%s:%s:%s";
    protected static String TXN_FACTOR_VALUE = "pay:rcs:txn:factor:%s:%s:%s:%s";


    public static String buildVelItemKey(VelEventData dto) {
        if (VelocityTimeModeEnum.TTL.getType().equals(dto.getTimeMode())) {
            return buildTtlTxnVelItemKey(dto.getFeatureCode(), dto.getMasterValue());
        } else if (VelocityTimeModeEnum.CURRENT_DAY.getType().contains(dto.getTimeMode())) {
            return buildCalendarTxnVelItemKey(dto.getFeatureCode(), dto.getMasterValue(),
                    DateUtil.format("yyyyMMdd", dto.getTxnTime()));
        } else if (VelocityTimeModeEnum.CURRENT_MONTH.getType().contains(dto.getTimeMode())) {
            return buildCalendarTxnVelItemKey(dto.getFeatureCode(), dto.getMasterValue(),
                    DateUtil.format("yyyyMM", dto.getTxnTime()));
        } else if (VelocityTimeModeEnum.CURRENT_YEAR.getType().contains(dto.getTimeMode())) {
            return buildCalendarTxnVelItemKey(dto.getFeatureCode(), dto.getMasterValue(),
                    DateUtil.format("yyyy", dto.getTxnTime()));
        } else {
            return buildTxnFactorValueKey(dto.getPointCode(), dto.getTxnId(), dto.getFeatureCode(), dto.getMasterValue());
        }

    }

    /**
     * 实时特征交易数据缓存key=实时特征编码+主维度值（TTL模式）
     */
    private static String buildTtlTxnVelItemKey(String velocityCode, String masterValue) {
        return String.format(TXN_VEL_ITEM_TTL, velocityCode, masterValue);
    }

    /**
     * 实时特征交易数据缓存key=实时特征编码+主维度值+日期（自然日月年模式）
     */
    private static String buildCalendarTxnVelItemKey(String velocityCode, String masterValue, String timeStr) {
        return String.format(TXN_VEL_ITEM_CALENDAR, velocityCode, masterValue, timeStr);
    }


    /**
     * 交易因子数据缓存key=接入点+交易号+因子名
     *
     * @param pointCode    接入点
     * @param txnId        交易号
     * @param velocityCode 因子名
     * @param masterValue  主维度值
     * @return
     */
    private static String buildTxnFactorValueKey(String pointCode, String txnId, String velocityCode, String masterValue) {
        return String.format(TXN_FACTOR_VALUE, pointCode, txnId, velocityCode, masterValue);
    }


}
