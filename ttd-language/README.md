# ttd-language

多语言脚本执行引擎模块，为特征平台提供统一的脚本执行能力。支持 **Groovy**、**Aviator**、**Jexl** 三种脚本语言，通过工厂模式实现语言无关的调用。

## 模块结构

```
ttd-language/
├── src/main/java/com/ly/ttd/language/
│   └── srv/
│       ├── ScriptLanguageService.java          # 对外服务接口
│       ├── FunctionTipsLoader.java             # 函数提示加载器（扫描 @FunctionDef 注解）
│       └── impl/
│           ├── AbstractLanguageEngine.java      # 引擎抽象基类
│           ├── EngineLanguageServiceImpl.java   # ScriptLanguageService 实现
│           ├── LanguageExecuteFactory.java      # 引擎工厂（按名称获取引擎实例）
│           ├── aviator/
│           │   ├── AviatorLanguage.java         # Aviator 表达式引擎
│           │   └── fun/                         # Aviator 内置函数
│           │       ├── math/    (14个)           # 数学函数
│           │       ├── seq/     (26个)           # 集合操作函数
│           │       ├── string/  (8个)            # 字符串函数
│           │       └── system/  (8个)            # 系统函数
│           ├── groovy/
│           │   ├── GroovyLanguage.java          # Groovy 脚本引擎
│           │   ├── GroovyFunctionRegistry.java  # Groovy 函数注册器
│           │   └── fun/
│           │       ├── GroovyFun.java           # 函数接口
│           │       ├── MathFun.java             # 数学内置函数
│           │       └── ListFun.java             # 列表内置函数
│           └── jexl/
│               ├── JexlLanguage.java            # Jexl 表达式引擎
│               └── fun/
│                   └── JexlFunction.java        # Jexl 内置函数
└── src/test/java/                              # 单元测试
```

## 架构设计

```
ScriptLanguageService (接口)
        │
        ▼
EngineLanguageServiceImpl
        │
        │  ScriptVariable{lang, script, params}
        ▼
LanguageExecuteFactory.getInstance(lang)
        │
        │  查找 serviceMap 中已注册的引擎
        ▼
AbstractLanguageEngine
        │
        ├── AviatorLanguage    (ScriptType.AVIATOR)
        ├── GroovyLanguage     (ScriptType.GROOVY)
        └── JexlLanguage       (ScriptType.JEXL)
```

- **LanguageExecuteFactory**：引擎工厂，启动时通过 `@PostConstruct` 扫描所有 `AbstractLanguageEngine` 的 Spring
  Bean，按 `getEngineName()` 注册到静态 `serviceMap`。
- **AbstractLanguageEngine**
  ：抽象基类，定义 `getEngineName()`、`execute(script, variables)`、`getEngineBuiltinFunctionTips()` 三个核心方法。
- **ScriptLanguageService**：对外统一接口，调用方只需传入 `ScriptVariable`（包含语言类型、脚本内容、参数 Map）。

## 三引擎对比

| 特性   | Groovy               | Aviator              | Jexl                        |
|------|----------------------|----------------------|-----------------------------|
| 类型   | 完整脚本语言               | 表达式引擎                | 表达式引擎                       |
| 适用场景 | 复杂逻辑、条件判断            | 高性能表达式计算             | 简单表达式                       |
| 函数扩展 | 实现 GroovyFun 接口      | 继承 AbstractFunction  | 在 JexlFunction 中添加方法        |
| 函数提示 | ✅ 通过 @FunctionDef 扫描 | ✅ 通过 @FunctionDef 扫描 | ❌ 暂不支持                      |
| 脚本缓存 | ❌                    | ❌                    | ✅ ConcurrentHashMap 缓存编译后脚本 |

## 快速开始

### 1. 调用示例

```java
// 方式一：通过 ScriptLanguageService 接口
@Resource
private ScriptLanguageService scriptLanguageService;

ScriptVariable variable = new ScriptVariable();
variable.setLang("groovy");                        // 语言类型
variable.setScript("a + b");                       // 脚本内容
variable.setParams(Map.of("a", 10, "b", 20));      // 变量

Object result = scriptLanguageService.execute(variable);
// result = 30

// 方式二：直接通过工厂获取引擎
AbstractLanguageEngine engine = LanguageExecuteFactory.getInstance("groovy");
Object result = engine.execute("math.add(1, 2)", new HashMap<>());
// result = 3.0
```

### 2. 用变量控制脚本逻辑

```java
String script = "if(amount > 1000) { return 'high' } else { return 'low' }";
Map<String, Object> params = Map.of("amount", 1500);

Object result = LanguageExecuteFactory.getInstance("groovy").execute(script, params);
// result = "high"
```

## 自定义函数

### Groovy 自定义函数

Groovy 引擎的函数通过 `GroovyFunctionRegistry` 自动注册到 Groovy 的 `Binding` 中，脚本内可直接以 `函数名.方法名()` 的方式调用。

**步骤一：创建函数类，实现 `GroovyFun` 接口**

```java
package com.ly.ttd.language.srv.impl.groovy.fun;

import com.ly.ttd.feature.common.tip.FunctionDef;
import com.ly.ttd.feature.common.tip.MethodName;

@FunctionDef  // 标记，使 FunctionTipsLoader 能扫描到并生成函数提示
public class StrFun implements GroovyFun {

    @Override
    public String getFunName() {
        return "str";  // 脚本中的调用前缀，如 str.upper(xxx)
    }

    @MethodName(prefix = "str", method = "upper",
            paramType = {"字符串"},
            desc = "将字符串转为大写",
            returnObj = "字符串")
    public String upper(String s) {
        return s != null ? s.toUpperCase() : null;
    }

    @MethodName(prefix = "str", method = "lower",
            paramType = {"字符串"},
            desc = "将字符串转为小写",
            returnObj = "字符串")
    public String lower(String s) {
        return s != null ? s.toLowerCase() : null;
    }
}
```

**步骤二：确保类在 `funPackage` 扫描路径下**

`GroovyLanguage.java` 中配置了函数扫描包路径：

```java
private static String[] funPackage = {
    "com.ly.ttd.language.srv.impl.groovy.fun"
};
```

类放在该包或其子包下即可被 `GroovyFunctionRegistry` 在 Spring
启动时自动扫描注册。如果放在其他包，需要同步修改 `funPackage`。

**步骤三：在脚本中使用**

```groovy
str.upper("hello")   // 返回 "HELLO"
str.lower("WORLD")   // 返回 "world"
```

**关键注解说明：**

- `@FunctionDef`：标记类为自定义函数，`FunctionTipsLoader` 通过此注解扫描生成函数提示列表
- `@MethodName`：标记每个对外暴露的方法
    - `prefix`：调用前缀，与 `GroovyFun.getFunName()` 对应
    - `method`：方法名，脚本中实际调用的方法名
    - `paramType`：参数类型描述（展示用）
    - `desc`：方法功能描述
    - `returnObj`：返回值类型描述

### Aviator 自定义函数

Aviator 函数继承 Aviator 的 `AbstractFunction`，实现 `getName()` 和 `call()` 方法（可选标记 `TtdAviatorDefineFun` 接口）。

```java
public class TtdMathAbsFunction extends AbstractFunction implements TtdAviatorDefineFun {

    @Override
    public String getName() {
        return "math.abs";  // 脚本调用名
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg) {
        Number num = FunctionUtils.getNumberFirst(arg, env);
        return AviatorNumber.valueOf(Math.abs(num.doubleValue()));
    }
}
```

### Jexl 自定义函数

Jexl 函数直接在 `JexlFunction` 类中添加方法，脚本中通过 `fn.方法名()` 调用。

```java
public class JexlFunction {

    public boolean existIntersection(Object a, Object b) {
        // 判断两个集合是否存在交集
        // ...
    }
}
```

脚本用法：

```jexl
fn.existIntersection(listA, listB)
```

### 函数提示机制

`FunctionTipsLoader` 通过类路径扫描 `@FunctionDef` 注解的类，提取其中 `@MethodName` 注解的方法，生成 `FunctionTip`
列表供前端展示函数签名和参数提示：

```java
// 获取引擎内置函数提示
List<FunctionTip> tips = LanguageExecuteFactory.getInstance("groovy")
        .getEngineBuiltinFunctionTips();
// 返回当前引擎所有标注了 @FunctionDef 和 @MethodName 的函数元数据
```

## 添加新语言引擎

实现一个自定义引擎仅需两步：

**步骤一：创建引擎类，继承 `AbstractLanguageEngine`**

```java
@Service
public class PythonLanguage extends AbstractLanguageEngine {

    @Override
    public String getEngineName() {
        return "python";
    }

    @Override
    public Object execute(String script, Map<String, Object> variables) {
        // 调用 Python 解释器执行脚本
        // ...
    }

    @Override
    public List<FunctionTip> getEngineBuiltinFunctionTips() throws Exception {
        return Collections.emptyList();
    }
}
```

**步骤二：添加枚举值**

在 `ttd-feature-common` 模块的 `ScriptType` 枚举中添加对应类型即可，`LanguageExecuteFactory` 启动时会自动发现并注册。

## 依赖

```xml
<dependency>
    <groupId>com.ly.ttd</groupId>
    <artifactId>ttd-language</artifactId>
    <version>${ttd.version}</version>
</dependency>
```

核心第三方依赖：

- **Groovy 5.0.4** — Groovy 脚本引擎
- **Aviator 5.3.3** — Aviator 表达式引擎
- **Commons Jexl 3.5.0** — Jexl 表达式引擎
- **Spring Context** — IoC 容器，引擎 Bean 自动注册
