package com.ly.ttd.biz.feature.admin.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存注解（方法级）
 *
 * @author yong.li
 * @since 2026/4/30 14:27
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface LocalCache {
}
