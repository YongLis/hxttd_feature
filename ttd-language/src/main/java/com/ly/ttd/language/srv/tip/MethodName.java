package com.ly.ttd.language.srv.tip;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yong.li
 * @since 2026/1/26 11:41
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodName {

    /**
     * 方法前缀
     */
    public String prefix() default "";

    /**
     * 方法名
     */
    public String method() default "";

    /**
     * 方法描述
     */
    public String desc() default "";

    /**
     * 参数类型
     */
    public String[] paramType() default {};

    /**
     * 返回值类型
     */
    public String returnObj() default "";


}
