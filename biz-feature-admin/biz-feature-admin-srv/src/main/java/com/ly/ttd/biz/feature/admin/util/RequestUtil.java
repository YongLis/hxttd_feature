package com.ly.ttd.biz.feature.admin.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 请求上下文工具类
 * 用于获取当前请求的 sessionId
 *
 * @author yong.li
 * @since 2026/4/30
 */
public class RequestUtil {

    private static final String SESSION_ID_ATTR = "sessionId";

    /**
     * 获取当前请求的 HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        return null;
    }

    /**
     * 从请求属性中获取 sessionId
     * 由 SessionIdInterceptor 自动设置
     *
     * @return sessionId，如果不存在则返回 null
     */
    public static String getSessionId() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return (String) request.getAttribute(SESSION_ID_ATTR);
        }
        return null;
    }

    /**
     * 从请求属性中获取 sessionId，如果不存在则返回默认值
     *
     * @param defaultValue 默认值
     * @return sessionId
     */
    public static String getSessionId(String defaultValue) {
        String sessionId = getSessionId();
        return sessionId != null ? sessionId : defaultValue;
    }
}
