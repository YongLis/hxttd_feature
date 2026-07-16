package com.ly.ttd.biz.feature.dem.sweb.config;

import com.ly.ttd.biz.feature.dem.sweb.cache.UserCache;
import com.ly.ttd.biz.feature.dem.sweb.interceptor.SessionIdInterceptor;
import com.ly.ttd.biz.feature.dem.sweb.service.user.res.UserInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yong.li
 * @since 2026/4/13 11:09
 */

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    /**
     * 自定义认证过滤器
     * 作用：从请求头获取 sessionId，从缓存获取用户信息，同步到 Spring Security
     */
    public static class CustomAuthenticationFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {

            String requestPath = request.getRequestURI();
            log.info("🚀 CustomAuthenticationFilter - 开始执行: path={}", requestPath);

            // 放过文件访问接口，不需要认证
            if (requestPath.startsWith("/api/file/view/")) {
                log.info("✅ CustomAuthenticationFilter - 跳过认证（文件访问）: path={}", requestPath);
                filterChain.doFilter(request, response);
                return;
            }

            // 从请求头获取 sessionId
            String sessionId = request.getHeader(SessionIdInterceptor.SESSION_ID_HEADER);
            log.debug("🔍 CustomAuthenticationFilter - 请求路径: {}, sessionId: {}", requestPath,
                    sessionId != null ? maskSensitiveData(sessionId) : "null");

            if (sessionId == null || sessionId.trim().isEmpty()) {
                log.debug("❌ CustomAuthenticationFilter - 请求头中未包含 sessionId, path={}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            // 从缓存获取用户信息
            Object obj = UserCache.INSTANCE.getIfPresent(sessionId.trim());
            log.debug("🔍 CustomAuthenticationFilter - 缓存查询结果: sessionId={}, userInfo类型={}",
                    maskSensitiveData(sessionId), obj != null ? obj.getClass().getName() : "null");

            if (obj != null) {
                UserInfo userInfo = (UserInfo) obj;
                log.debug("🔍 CustomAuthenticationFilter - UserInfo详情: userName={}, role={}",
                        maskSensitiveData(userInfo.getUserName()), userInfo.getRole());

                // 构建权限列表
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (userInfo.getRole() != null && !userInfo.getRole().isEmpty()) {
                    // 添加用户角色作为权限
                    authorities.add(new SimpleGrantedAuthority(userInfo.getRole()));
                    // 兼容 ROLE_ 前缀
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + userInfo.getRole().toUpperCase()));
                    // 添加小写版本
                    authorities.add(new SimpleGrantedAuthority(userInfo.getRole().toLowerCase()));
                    log.info("🔍 CustomAuthenticationFilter - 构建的权限列表: {}", authorities);
                } else {
                    log.warn("❌ CustomAuthenticationFilter - 用户角色为空，使用默认角色 admin");
                    // 如果角色为空，默认给 admin 角色
                    authorities.add(new SimpleGrantedAuthority("admin"));
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                }

                // 用户已登录，创建 Spring Security 认证令牌
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userInfo.getUserName(),  // 用户名
                                null,                     // 凭证（不需要）
                                authorities               // 权限列表
                        );

                // 设置到 Spring Security 上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("✅ CustomAuthenticationFilter - 认证成功: role={}, path={}",
                        userInfo.getRole(), request.getRequestURI());
                log.debug("✅ CustomAuthenticationFilter - SecurityContext中的认证信息: {}",
                        SecurityContextHolder.getContext().getAuthentication());
            } else {
                log.debug("❌ CustomAuthenticationFilter - 认证失败: 缓存中未找到用户信息, sessionId={}, path={}",
                        maskSensitiveData(sessionId), request.getRequestURI());
            }

            filterChain.doFilter(request, response);
        }

        /**
         * 脱敏处理敏感数据
         *
         * @param data 原始数据
         * @return 脱敏后的数据（显示前4位和后4位，中间用*替换）
         */
        private String maskSensitiveData(String data) {
            if (data == null || data.isEmpty()) {
                return data;
            }
            if (data.length() <= 8) {
                return "***";
            }
            return data.substring(0, 4) + "****" + data.substring(data.length() - 4);
        }
    }

    @Bean
    public SecurityFilterChain buildSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("🔧 SecurityConfig - 初始化安全配置");
        return httpSecurity
                // 添加自定义认证过滤器，必须在最前面执行
                .addFilterBefore(new CustomAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authz) ->
                        authz
                                // Swagger UI 和 API 文档路径
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**",
                                        "/v3/api-docs.yaml"
                                ).permitAll()
                                // TODO: 联调期间临时关闭所有权限认证，上线前务必恢复
                                .anyRequest().permitAll())
                .csrf(csrf -> {
                    csrf.disable();
                    log.info("🔧 SecurityConfig - CSRF 已禁用");
                })
                .cors(cors -> {
                    cors.configure(httpSecurity);
                    log.info("🔧 SecurityConfig - CORS 已配置");
                })
                .formLogin(formLogin -> formLogin.disable())
                .logout((logout) -> logout.permitAll())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.error("❌ SecurityConfig - 认证失败: path={}, error={}", request.getRequestURI(), authException.getMessage());
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"code\":\"401\",\"message\":\"未认证\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.error("❌ SecurityConfig - 权限不足: path={}, user={}, error={}",
                                    request.getRequestURI(),
                                    SecurityContextHolder.getContext().getAuthentication(),
                                    accessDeniedException.getMessage());
                            response.setStatus(403);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"code\":\"403\",\"message\":\"权限不足\"}");
                        })
                )
                .build();
    }

}
