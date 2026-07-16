package com.ly.ttd.feature.srv.meta;

import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.common.ctx.TxnParamContext;
import com.ly.ttd.feature.common.model.meta.MetaFieldModel;
import com.ly.ttd.language.srv.ScriptLanguageService;
import com.ly.ttd.language.srv.req.ScriptVariable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * AccessMetaFieldCalculate 单元测试
 *
 * @author yong.li
 * @since 2026-07-01
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AccessMetaFieldCalculate 单元测试")
class AccessMetaFieldCalculateTest {

    @Mock
    private ScriptLanguageService scriptLanguageService;

    @InjectMocks
    private AccessMetaFieldCalculate calculate;

    private FeatureConfiguration featureConfiguration;
    private TxnParamContext ctx;

    private static final String FIELD_KEY = "testField";
    private static final String TXN_ID = "TX20260701001";
    private static final String POINT_CODE = "POINT_001";

    @BeforeEach
    void setUp() {
        featureConfiguration = new FeatureConfiguration(null);
        featureConfiguration.getMetaFieldMap().clear();
        featureConfiguration.getPointCodeMetaFieldMap().clear();
        calculate.setFeatureConfiguration(featureConfiguration);

        ctx = new TxnParamContext();
        ctx.setTxnId(TXN_ID);
        ctx.setTxnTime(new Date());
        ctx.setPointCode(POINT_CODE);
        ctx.setReq(Collections.singletonMap("amount", 100));
    }

    // ==================== loadValue tests ====================

    @Test
    @DisplayName("loadValue — 缓存命中直接返回，不执行脚本")
    void testLoadValue_CachedValue() {
        ctx.put(FIELD_KEY, "cached_result");

        Object result = calculate.loadValue(FIELD_KEY, ctx);

        assertEquals("cached_result", result);
        verify(scriptLanguageService, never()).execute(any());
    }

    @Test
    @DisplayName("loadValue — fieldKey 为空返回 null")
    void testLoadValue_BlankFieldKey() {
        Object result = calculate.loadValue("", ctx);
        assertNull(result);
        verify(scriptLanguageService, never()).execute(any());
    }

    @Test
    @DisplayName("loadValue — txnId 为空返回 null")
    void testLoadValue_BlankTxnId() {
        ctx.setTxnId(null);

        Object result = calculate.loadValue(FIELD_KEY, ctx);

        assertNull(result);
        verify(scriptLanguageService, never()).execute(any());
    }

    @Test
    @DisplayName("loadValue — MetaFieldModel 不存在返回 null")
    void testLoadValue_MetaFieldNotFound() {
        Object result = calculate.loadValue("nonexistent", ctx);

        assertNull(result);
        verify(scriptLanguageService, never()).execute(any());
    }

    @Test
    @DisplayName("loadValue — 脚本执行成功，返回值并缓存")
    void testLoadValue_ScriptSuccess() throws Exception {
        MetaFieldModel field = buildMetaField("STRING", "defaultVal", "exceptionVal");
        featureConfiguration.getMetaFieldMap().put(FIELD_KEY, field);
        when(scriptLanguageService.execute(any())).thenReturn("computed_value");

        Object result = calculate.loadValue(FIELD_KEY, ctx);

        assertEquals("computed_value", result);
        assertEquals("computed_value", ctx.get(FIELD_KEY));
        verify(scriptLanguageService, times(1)).execute(any());
    }

    @Test
    @DisplayName("loadValue — 脚本返回 null，返回 defaultValue")
    void testLoadValue_ScriptReturnsNull() throws Exception {
        MetaFieldModel field = buildMetaField("STRING", "fallback_default", "fallback_error");
        featureConfiguration.getMetaFieldMap().put(FIELD_KEY, field);
        when(scriptLanguageService.execute(any())).thenReturn(null);

        Object result = calculate.loadValue(FIELD_KEY, ctx);

        assertEquals("fallback_default", result);
        assertEquals("fallback_default", ctx.get(FIELD_KEY));
    }

    @Test
    @DisplayName("loadValue — 脚本返回空字符串，返回 defaultValue")
    void testLoadValue_ScriptReturnsEmptyString() throws Exception {
        MetaFieldModel field = buildMetaField("STRING", "fallback_default", "fallback_error");
        featureConfiguration.getMetaFieldMap().put(FIELD_KEY, field);
        when(scriptLanguageService.execute(any())).thenReturn("");

        Object result = calculate.loadValue(FIELD_KEY, ctx);

        assertEquals("fallback_default", result);
    }

    @Test
    @DisplayName("loadValue — 脚本执行异常，返回 exceptionValue 并缓存")
    void testLoadValue_ScriptThrowsException() throws Exception {
        MetaFieldModel field = buildMetaField("STRING", "defaultVal", "error_fallback");
        featureConfiguration.getMetaFieldMap().put(FIELD_KEY, field);
        when(scriptLanguageService.execute(any())).thenThrow(new RuntimeException("script error"));

        Object result = calculate.loadValue(FIELD_KEY, ctx);

        assertEquals("error_fallback", result);
        assertEquals("error_fallback", ctx.get(FIELD_KEY));
    }

    @Test
    @DisplayName("loadValue — LONG 返回值类型，异常时返回转换后的 exceptionValue")
    void testLoadValue_ScriptThrows_LongReturnType() throws Exception {
        MetaFieldModel field = buildMetaField("LONG", "0", "999");
        featureConfiguration.getMetaFieldMap().put(FIELD_KEY, field);
        when(scriptLanguageService.execute(any())).thenThrow(new RuntimeException("script error"));

        Object result = calculate.loadValue(FIELD_KEY, ctx);

        assertEquals(999L, result);
        assertEquals(999L, ctx.get(FIELD_KEY));
    }

    @Test
    @DisplayName("loadValue — DOUBLE 返回值类型，脚本执行成功")
    void testLoadValue_DoubleReturnType() throws Exception {
        MetaFieldModel field = buildMetaField("DOUBLE", "0.0", "-1.0");
        featureConfiguration.getMetaFieldMap().put(FIELD_KEY, field);
        when(scriptLanguageService.execute(any())).thenReturn(123.45);

        Object result = calculate.loadValue(FIELD_KEY, ctx);

        assertEquals(123.45, result);
    }

    // ==================== batchLoadValue tests ====================

    @Test
    @DisplayName("batchLoadValue — 接入点下无元字段，空处理")
    void testBatchLoadValue_NoMetaFields() {
        ctx.setPointCode("EMPTY_POINT");
        featureConfiguration.getPointCodeMetaFieldMap().put("EMPTY_POINT", Collections.emptyList());

        calculate.batchLoadValue(ctx);

        verify(scriptLanguageService, never()).execute(any());
    }

    @Test
    @DisplayName("batchLoadValue — 接入点下多个元字段，并行加载并缓存")
    void testBatchLoadValue_MultipleFields() throws Exception {
        Map<String, MetaFieldModel> metaMap = new ConcurrentHashMap<>();
        metaMap.put("field_a", buildMetaField("STRING", "a_default", "a_error"));
        metaMap.put("field_b", buildMetaField("STRING", "b_default", "b_error"));
        metaMap.put("field_c", buildMetaField("LONG", "0", "-1"));
        featureConfiguration.getMetaFieldMap().putAll(metaMap);

        List<String> fieldList = new ArrayList<>();
        fieldList.add("field_a");
        fieldList.add("field_b");
        fieldList.add("field_c");
        featureConfiguration.getPointCodeMetaFieldMap().put(POINT_CODE, fieldList);

        when(scriptLanguageService.execute(any())).thenAnswer(invocation -> {
            String key = ((ScriptVariable) invocation.getArgument(0)).getResourceKey();
            if ("field_a".equals(key)) return "value_a";
            if ("field_b".equals(key)) return "value_b";
            if ("field_c".equals(key)) return 42L;
            return null;
        });

        calculate.batchLoadValue(ctx);

        assertEquals("value_a", ctx.get("field_a"));
        assertEquals("value_b", ctx.get("field_b"));
        assertEquals(42L, ctx.get("field_c"));
        verify(scriptLanguageService, times(3)).execute(any());
    }

    @Test
    @DisplayName("batchLoadValue — 部分元字段已缓存，仅加载未缓存的")
    void testBatchLoadValue_PartialCache() throws Exception {
        ctx.put("field_a", "already_cached");

        Map<String, MetaFieldModel> metaMap = new ConcurrentHashMap<>();
        metaMap.put("field_a", buildMetaField("STRING", "a_default", "a_error"));
        metaMap.put("field_b", buildMetaField("STRING", "b_default", "b_error"));
        featureConfiguration.getMetaFieldMap().putAll(metaMap);

        List<String> fieldList = new ArrayList<>();
        fieldList.add("field_a");
        fieldList.add("field_b");
        featureConfiguration.getPointCodeMetaFieldMap().put(POINT_CODE, fieldList);

        when(scriptLanguageService.execute(any())).thenReturn("value_b");

        calculate.batchLoadValue(ctx);

        assertEquals("already_cached", ctx.get("field_a"));
        assertEquals("value_b", ctx.get("field_b"));
        // field_a already cached, field_b executed once
        verify(scriptLanguageService, times(1)).execute(any());
    }

    @Test
    @DisplayName("loadValue — 同 fieldKey 多次调用仅执行一次脚本")
    void testLoadValue_CacheOnSecondCall() throws Exception {
        MetaFieldModel field = buildMetaField("STRING", "defaultVal", "exceptionVal");
        featureConfiguration.getMetaFieldMap().put(FIELD_KEY, field);
        when(scriptLanguageService.execute(any())).thenReturn("first_result");

        calculate.loadValue(FIELD_KEY, ctx);
        Object result = calculate.loadValue(FIELD_KEY, ctx);

        assertEquals("first_result", result);
        verify(scriptLanguageService, times(1)).execute(any());
    }

    @Test
    @DisplayName("loadValue — ctx 为 null 时返回 null（异常捕获路径）")
    void testLoadValue_NullContext() {
        Object result = calculate.loadValue(FIELD_KEY, null);
        assertNull(result);
    }

    @Test
    @DisplayName("loadValue — BOOLEAN 返回值类型，脚本执行成功")
    void testLoadValue_BooleanReturnType() throws Exception {
        MetaFieldModel field = buildMetaField("BOOLEAN", "false", "true");
        featureConfiguration.getMetaFieldMap().put(FIELD_KEY, field);
        when(scriptLanguageService.execute(any())).thenReturn(true);

        Object result = calculate.loadValue(FIELD_KEY, ctx);

        assertEquals(true, result);
    }

    // ==================== batchLoadValue 补充 ====================

    @Test
    @DisplayName("batchLoadValue — pointCode 未注册，null guard 返回不抛异常")
    void testBatchLoadValue_PointCodeNotInMap() {
        ctx.setPointCode("UNREGISTERED_POINT");

        calculate.batchLoadValue(ctx);

        verify(scriptLanguageService, never()).execute(any());
    }

    @Test
    @DisplayName("batchLoadValue — 元字段脚本执行异常，使用 exceptionValue")
    void testBatchLoadValue_ScriptFailure_UsesExceptionValue() throws Exception {
        MetaFieldModel field = buildMetaField("STRING", "defaultVal", "error_fallback");
        featureConfiguration.getMetaFieldMap().put("fail_field", field);

        List<String> fieldList = new ArrayList<>();
        fieldList.add("fail_field");
        featureConfiguration.getPointCodeMetaFieldMap().put(POINT_CODE, fieldList);

        when(scriptLanguageService.execute(any())).thenThrow(new RuntimeException("script error"));

        calculate.batchLoadValue(ctx);

        assertEquals("error_fallback", ctx.get("fail_field"));
    }

    // ==================== helpers ====================

    private MetaFieldModel buildMetaField(String returnType, String defaultValue, String exceptionValue) {
        MetaFieldModel field = new MetaFieldModel();
        field.setResourceKey(FIELD_KEY);
        field.setReturnType(returnType);
        field.setTimeout(5000L);
        field.setDefaultValue(defaultValue);
        field.setExceptionValue(exceptionValue);
        field.setScript("return params.amount + 1");
        field.setLanguage("groovy");
        return field;
    }
}
