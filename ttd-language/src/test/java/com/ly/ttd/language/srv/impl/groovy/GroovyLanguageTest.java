package com.ly.ttd.language.srv.impl.groovy;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.language.srv.consts.ScriptType;
import com.ly.ttd.language.srv.impl.groovy.fun.GroovyFun;
import com.ly.ttd.language.srv.impl.groovy.fun.ListFun;
import com.ly.ttd.language.srv.impl.groovy.fun.MathFun;
import com.ly.ttd.language.srv.tip.FunctionTip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GroovyLanguage 单元测试
 *
 * @author yong.li
 * @since 2026/6/24
 */
class GroovyLanguageTest {

    private GroovyLanguage groovyLanguage;
    private GroovyFunctionRegistry registry;

    @BeforeEach
    void setUp() {
        groovyLanguage = new GroovyLanguage();
        registry = new GroovyFunctionRegistry();
        injectRegistry(groovyLanguage, registry);
    }

    private void injectRegistry(GroovyLanguage language, GroovyFunctionRegistry registry) {
        try {
            java.lang.reflect.Field field = GroovyLanguage.class.getDeclaredField("registry");
            field.setAccessible(true);
            field.set(language, registry);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void registerFunction(GroovyFun fun) {
        registry.getFunctionMap().put(fun.getFunName(), fun);
    }

    @Test
    @DisplayName("测试获取引擎名称")
    void testGetEngineName() {
        String engineName = groovyLanguage.getEngineName();
        assertEquals(ScriptType.GROOVY.getCode(), engineName, "引擎名称应该是groovy");
    }

    @Test
    @DisplayName("测试执行简单Groovy脚本")
    void testExecuteSimpleScript() {
        String script = "1 + 2";
        Map<String, Object> variables = new HashMap<>();

        Object result = groovyLanguage.execute(script, variables);

        assertNotNull(result, "执行结果不应为null");
        assertEquals(3, result, "1 + 2 应该等于 3");
    }

    @Test
    @DisplayName("测试执行带变量的Groovy脚本")
    void testExecuteWithVariables() {
        String script = "a + b";
        Map<String, Object> variables = new HashMap<>();
        variables.put("a", 10);
        variables.put("b", 20);

        Object result = groovyLanguage.execute(script, variables);

        assertNotNull(result, "执行结果不应为null");
        assertEquals(30, result, "10 + 20 应该等于 30");
    }

    @Test
    @DisplayName("测试执行条件判断脚本")
    void testExecuteConditionScript() {
        String script = "if(name == 'tom') { return true; } else { return false; }";
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "tom");

        Object result = groovyLanguage.execute(script, variables);

        assertNotNull(result, "执行结果不应为null");
        assertTrue((Boolean) result, "name为tom时应该返回true");
    }

    @Test
    @DisplayName("测试执行返回false的条件判断脚本")
    void testExecuteConditionScriptReturnFalse() {
        String script = "if(name == 'tom') { return true; } else { return false; }";
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "jerry");

        Object result = groovyLanguage.execute(script, variables);

        assertNotNull(result, "执行结果不应为null");
        assertFalse((Boolean) result, "name为jerry时应该返回false");
    }

    @Test
    @DisplayName("测试执行字符串操作脚本")
    void testExecuteStringOperation() {
        String script = "'Hello, ' + name";
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "World");

        Object result = groovyLanguage.execute(script, variables);

        assertNotNull(result, "执行结果不应为null");
        assertEquals("Hello, World", result, "字符串拼接应该正确");
    }

    @Test
    @DisplayName("测试执行Map操作脚本")
    void testExecuteMapOperation() {
        String script = "person.name";
        Map<String, Object> variables = new HashMap<>();
        Map<String, Object> person = new HashMap<>();
        person.put("name", "tom");
        variables.put("person", person);

        Object result = groovyLanguage.execute(script, variables);

        assertNotNull(result, "执行结果不应为null");
        assertEquals("tom", result, "Map属性访问应该正确");
    }

    @Test
    @DisplayName("测试执行null变量脚本")
    void testExecuteWithNullVariables() {
        String script = "1 + 1";

        Object result = groovyLanguage.execute(script, null);

        assertNotNull(result, "执行结果不应为null");
        assertEquals(2, result, "无变量脚本应该正常执行");
    }

    @Test
    @DisplayName("测试执行空脚本")
    void testExecuteEmptyScript() {
        String script = "";

        Object result = groovyLanguage.execute(script, new HashMap<>());

        assertNull(result, "空脚本应该返回null");
    }

    @Test
    @DisplayName("测试执行算术运算脚本")
    void testExecuteArithmeticOperations() {
        String script = "(a + b) * c / d";
        Map<String, Object> variables = new HashMap<>();
        variables.put("a", 10);
        variables.put("b", 5);
        variables.put("c", 2);
        variables.put("d", 3);

        BigDecimal result = (BigDecimal) groovyLanguage.execute(script, variables);

        assertNotNull(result, "执行结果不应为null");
        assertTrue(result.compareTo(BigDecimal.valueOf(10.0)) == 0);
    }

    @Test
    @DisplayName("测试获取引擎内置函数提示")
    void testGetEngineBuiltinFunctionTips() throws Exception {
        List<FunctionTip> tips = groovyLanguage.getEngineBuiltinFunctionTips();

        System.out.println(JSON.toJSONString(tips));
        assertNotNull(tips, "函数提示列表不应为null");
        // 如果funPackage包下有函数类，应该返回非空列表
        // 如果没有，则返回空列表也是正常的
    }

    @Test
    @DisplayName("测试执行包含MathFun的脚本")
    void testExecuteWithMathFun() {
        registerFunction(new MathFun());

        String script = "math.add(1, 2)";
        Map<String, Object> variables = new HashMap<>();

        double result = (double) groovyLanguage.execute(script, variables);

        assertEquals(3.0, result, "math.add(1, 2) 应该等于 3.0");
    }

    @Test
    @DisplayName("测试执行ListFun脚本")
    void testExecuteWithListFun() {
        registerFunction(new ListFun());

        String script = "list.size([1, 2, 3])";
        Map<String, Object> variables = new HashMap<>();

        Object result = groovyLanguage.execute(script, variables);

        assertEquals(3, result, "list.size([1, 2, 3]) 应该返回 3");
    }

    @Test
    @DisplayName("测试执行异常脚本应该抛出RuntimeException")
    void testExecuteInvalidScript() {
        String script = "def x = 1 / 0";
        Map<String, Object> variables = new HashMap<>();

        assertThrows(RuntimeException.class, () -> {
            groovyLanguage.execute(script, variables);
        }, "除以零的脚本应该抛出RuntimeException");
    }
}