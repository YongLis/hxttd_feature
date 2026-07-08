package com.ly.ttd.feature.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

/**
 * @author yong.li
 * @since 2026/5/20 16:03
 */
public class Md5Util {

    /**
     * MD5加密
     *
     * @param source
     * @return
     */
    public static String MD5(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(source.getBytes());
    }

}
