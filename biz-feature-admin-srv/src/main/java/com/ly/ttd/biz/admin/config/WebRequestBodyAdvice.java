package com.ly.ttd.biz.admin.config;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.biz.admin.req.BaseRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.lang.reflect.Type;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class WebRequestBodyAdvice implements RequestBodyAdvice {

    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // 仅处理带有@RequestBody注解的参数
        return methodParameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) {
        //预处理原始输入流, 无需处理
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        // 1、打印日志，2、参数处理，BaseParam
        try {
            log.info("[{}],requestUri={},param={} ", getControllerInfo(parameter),
                    getRequestUri(), serializeBody(body));
            // 2. 统一参数处理
            if (body instanceof BaseRequest) {
                handleBaseParam((BaseRequest) body);
            }
        } catch (Exception e) {
            log.error("handleBaseParam error", e);
        }
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                  Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    private String getControllerInfo(MethodParameter returnType) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(returnType.getContainingClass().getName()).append("#").append(Objects.requireNonNull(returnType.getMethod()).getName());
            return sb.toString();
        } catch (Exception e) {
            log.warn("getControllerInfo error", e);
        }
        return "";
    }

    /**
     * 带脱敏的序列化
     */
    private String serializeBody(Object obj) {
        if (obj == null) {
            return "";
        }
        return JSON.toJSONString(obj);
    }

    /**
     * 基础参数统一处理
     */
    private void handleBaseParam(BaseRequest param) {

        if (param.getProjectId() == null) {
            String projectIdStr = getRequestHeader("X-Project-Id");
            if (projectIdStr != null && !projectIdStr.isEmpty()) {
                param.setProjectId(Long.valueOf(projectIdStr));
            }
        }
        // 参数trim处理
        trimStringFields(param);
    }

    /**
     * 字符串字段trim处理
     */
    private void trimStringFields(Object obj) {
        if (obj == null) {
            return;
        }

        ReflectionUtils.doWithFields(obj.getClass(), field -> {
            if (field.getType() == String.class) {
                field.setAccessible(true);
                String value = (String) field.get(obj);
                if (value != null) {
                    field.set(obj, value.trim());
                }
            }
        });
    }

    private String getRequestUri() {
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return servletRequest.getRequestURI();
    }

    private String getRequestHeader(String headerName) {
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return servletRequest.getHeader(headerName);
    }

}
