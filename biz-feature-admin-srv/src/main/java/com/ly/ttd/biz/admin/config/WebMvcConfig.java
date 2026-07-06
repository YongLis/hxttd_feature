package com.ly.ttd.biz.admin.config;

import com.ly.ttd.biz.admin.interceptor.SessionIdInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 * 注册 SessionId 拦截器
 *
 * @author yong.li
 * @since 2026/4/30
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private SessionIdInterceptor sessionIdInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionIdInterceptor)
                .addPathPatterns("/api/**")  // 拦截所有 API 请求
                .excludePathPatterns(
                        "/api/blog/**",  // 排除公开接口
                        "/api/admin/login",
                        "/api/admin/logout",
                        "/api/health/check",
                        "/api/file/view/**"
                );
    }
}
