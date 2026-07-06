package com.ly.ttd.language.srv.impl;

import com.ly.ttd.feature.common.tip.FunctionTip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LanguageExecuteFactory 单元测试
 *
 * @author yong.li
 * @since 2026/6/24
 */
@ExtendWith(MockitoExtension.class)
class LanguageExecuteFactoryTest {

    private LanguageExecuteFactory factory;

    @BeforeEach
    void setUp() {
        // 重置静态 serviceMap
        resetServiceMap();
        factory = new LanguageExecuteFactory();
    }

    /**
     * 重置静态 serviceMap
     */
    private void resetServiceMap() {
        try {
            java.lang.reflect.Field field = LanguageExecuteFactory.class.getDeclaredField("serviceMap");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, AbstractLanguageEngine> map = (Map<String, AbstractLanguageEngine>) field.get(null);
            map.clear();
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset serviceMap", e);
        }
    }

    @Test
    @DisplayName("测试获取不存在的语言引擎返回null")
    void testGetInstanceWithNonExistentLang() {
        // 确保 serviceMap 为空
        resetServiceMap();

        // 调用 getInstance 方法
        AbstractLanguageEngine result = LanguageExecuteFactory.getInstance("nonexistent");

        // 验证返回 null
        assertNull(result, "获取不存在的语言引擎应该返回null");
    }

    @Test
    @DisplayName("测试获取存在的语言引擎")
    void testGetInstanceWithExistentLang() {
        // 创建模拟的语言引擎
        AbstractLanguageEngine mockEngine = new MockLanguageEngine("testLang");

        // 手动注册引擎
        registerEngine("testLang", mockEngine);

        // 调用 getInstance 方法
        AbstractLanguageEngine result = LanguageExecuteFactory.getInstance("testLang");

        // 验证返回正确的引擎
        assertNotNull(result, "应该能获取到testLang引擎");
        assertEquals("testLang", result.getEngineName(), "引擎名称应该匹配");
    }

    @Test
    @DisplayName("测试多个相同名称引擎覆盖")
    void testMultipleEnginesWithSameName() {
        // 创建两个同名的模拟引擎
        AbstractLanguageEngine engine1 = new MockLanguageEngine("sameName");
        AbstractLanguageEngine engine2 = new MockLanguageEngine("sameName");

        // 注册第一个引擎
        registerEngine("sameName", engine1);
        AbstractLanguageEngine result1 = LanguageExecuteFactory.getInstance("sameName");
        assertSame(engine1, result1, "应该返回第一个注册的引擎");

        // 注册第二个引擎（覆盖）
        registerEngine("sameName", engine2);
        AbstractLanguageEngine result2 = LanguageExecuteFactory.getInstance("sameName");
        assertSame(engine2, result2, "应该返回最后注册的引擎（已被覆盖）");
    }

    /**
     * 获取 serviceMap 的大小
     */
    private int getServiceMapSize() {
        try {
            java.lang.reflect.Field field = LanguageExecuteFactory.class.getDeclaredField("serviceMap");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, AbstractLanguageEngine> map = (Map<String, AbstractLanguageEngine>) field.get(null);
            return map.size();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get serviceMap size", e);
        }
    }

    /**
     * 注册引擎到 serviceMap
     */
    private void registerEngine(String name, AbstractLanguageEngine engine) {
        try {
            java.lang.reflect.Field field = LanguageExecuteFactory.class.getDeclaredField("serviceMap");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, AbstractLanguageEngine> map = (Map<String, AbstractLanguageEngine>) field.get(null);
            map.put(name, engine);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register engine", e);
        }
    }

    /**
     * 模拟的语言引擎实现
     */
    private static class MockLanguageEngine extends AbstractLanguageEngine {

        private final String engineName;

        public MockLanguageEngine(String engineName) {
            this.engineName = engineName;
        }

        @Override
        public String getEngineName() {
            return engineName;
        }

        @Override
        public Object execute(String script, Map<String, Object> variables) {
            return null;
        }

        @Override
        public java.util.List<FunctionTip> getEngineBuiltinFunctionTips() throws Exception {
            return java.util.Collections.emptyList();
        }
    }
}