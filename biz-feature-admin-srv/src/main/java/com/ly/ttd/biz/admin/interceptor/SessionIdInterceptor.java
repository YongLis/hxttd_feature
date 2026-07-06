package com.ly.ttd.biz.admin.interceptor;

import com.ly.ttd.biz.admin.cache.UserCache;
import com.ly.ttd.biz.admin.consts.LoginUserUtils;
import com.ly.ttd.biz.admin.srv.user.res.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * SessionId 拦截器
 * 自动从请求 headers 获取 X-Session-Id 并设置到请求属性中
 *
 * @author yong.li
 * @since 2026/4/30
 */
@Slf4j
@Component
public class SessionIdInterceptor implements HandlerInterceptor {

    public static final String SESSION_ID_HEADER = "X-Session-Id";
    public static final String TENANT_ID_HEADER = "X-Tenant-Id";
    public static final String PROJECT_ID_HEADER = "X-Project-Id";
    private static final String SESSION_ID_ATTR = "sessionId";
    private static final String TENANT_ID_ATTR = "tenantId";
    private static final String PROJECT_ID_ATTR = "projectId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头获取 sessionId
        String sessionId = request.getHeader(SESSION_ID_HEADER);
        log.debug("SessionIdInterceptor - 请求路径: {}, sessionId: {}", request.getRequestURI(),
                sessionId != null ? maskSessionId(sessionId) : "null");

        if (StringUtils.isNotBlank(sessionId)) {
            // 将 sessionId 设置到请求属性中，方便后续使用
            request.setAttribute(SESSION_ID_ATTR, sessionId.trim());

            // 从缓存获取用户信息
            UserInfo userInfo = (UserInfo) UserCache.INSTANCE.getIfPresent(sessionId);

            if (userInfo != null) {
                LoginUserUtils.INSTANCE.setUserInfo(userInfo);
            } else {
                LoginUserUtils.INSTANCE.setUserInfo(null);
            }
        } else {
            log.debug("SessionIdInterceptor - 请求头中未包含 X-Session-Id");
            LoginUserUtils.INSTANCE.setUserInfo(null);
        }

        // 从请求头获取 tenantId 和 projectId
        String projectId = request.getHeader(PROJECT_ID_HEADER);
        log.debug("SessionIdInterceptor - projectId: {}", projectId);
        if (StringUtils.isNotBlank(projectId)) {
            request.setAttribute(PROJECT_ID_ATTR, projectId.trim());
            LoginUserUtils.INSTANCE.setProjectId(Long.valueOf(projectId.trim()));
        } else {
            LoginUserUtils.INSTANCE.setProjectId(null);
        }

        return true;
    }

    /**
     * 脱敏 sessionId
     */
    private String maskSessionId(String sessionId) {
        if (sessionId == null || sessionId.isEmpty()) {
            return sessionId;
        }
        if (sessionId.length() <= 8) {
            return "***";
        }
        return sessionId.substring(0, 4) + "****" + sessionId.substring(sessionId.length() - 4);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        // 可以在这里进行后处理
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        // 清理资源
    }
}
