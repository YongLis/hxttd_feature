package com.ly.ttd.feature.srv.fallback;

import com.ly.ttd.feature.common.enums.ObjectTypeEnum;
import com.ly.ttd.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author yong.li
 * @since 2026/6/10 13:25
 */
public class ValueConvertor {

    public static Object convert(String returnType, String defaultValue) {
        ObjectTypeEnum typeEnum = ObjectTypeEnum.valueOf(returnType);
        if (null != typeEnum && StringUtils.isEmpty(defaultValue)) {
            defaultValue = typeEnum.getDefaultValue();
        }
        switch (typeEnum) {
            case LONG:
                return Long.parseLong(defaultValue);
            case DOUBLE:
                return Double.parseDouble(defaultValue);
            case DECIMAL:
                return new BigDecimal(defaultValue);
            case BOOLEAN:
                return Boolean.parseBoolean(defaultValue);
            case LIST:
                return new ArrayList<>();
            case DATE:
                return StringUtils.isNoneEmpty(defaultValue) ? DateUtil.parse(DateUtil.PATTERN_YYYYMMDDHHMMSS, defaultValue) : null;
            case DICT:
                return new HashMap<>();
            default:
                return defaultValue;
        }

    }
}
