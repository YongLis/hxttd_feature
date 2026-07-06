package com.ly.ttd.language.srv.impl.groovy.fun;

import com.ly.ttd.feature.common.tip.FunctionDef;
import com.ly.ttd.feature.common.tip.MethodName;

/**
 * @author yong.li
 * @since 2026/1/23 12:59
 */
@FunctionDef
public class MathFun implements GroovyFun {
    @Override
    public String getFunName() {
        return "math";
    }

    /**
     * 加法运算
     *
     * @param a 第一个加数
     * @param b 第二个加数
     * @return 两数之和
     */
    @MethodName(prefix = "math", method = "add",
            paramType = {"整数｜小数｜金额", "整数｜小数｜金额"},
            desc = "加法运算，返回两数之和",
            returnObj = "整数｜小数｜金额")
    public double add(double a, double b) {
        return a + b;
    }

    /**
     * 减法运算
     *
     * @param a 被减数
     * @param b 减数
     * @return 两数之差
     */
    @MethodName(prefix = "math", method = "subtract",
            paramType = {"整数｜小数｜金额", "整数｜小数｜金额"},
            desc = "减法运算，返回两数之差",
            returnObj = "整数｜小数｜金额")
    public double subtract(double a, double b) {
        return a - b;
    }

    /**
     * 乘法运算
     *
     * @param a 第一个乘数
     * @param b 第二个乘数
     * @return 两数之积
     */
    @MethodName(prefix = "math", method = "multiply",
            paramType = {"整数｜小数｜金额", "整数｜小数｜金额"},
            desc = "乘法运算，返回两数之积",
            returnObj = "整数｜小数｜金额")
    public double multiply(double a, double b) {
        return a * b;
    }

    /**
     * 除法运算
     *
     * @param a 被除数
     * @param b 除数
     * @return 两数之商
     */
    @MethodName(prefix = "math", method = "divide",
            paramType = {"整数｜小数｜金额", "整数｜小数｜金额"},
            desc = "除法运算，返回两数之商",
            returnObj = "整数｜小数｜金额")
    public double divide(double a, double b) {
        return a / b;
    }

    /**
     * 取模运算
     *
     * @param a 被除数
     * @param b 除数
     * @return 两数之余数
     */
    @MethodName(prefix = "math", method = "mod",
            paramType = {"整数｜小数｜金额", "整数｜小数｜金额"},
            desc = "取模运算，返回两数之余数",
            returnObj = "整数｜小数｜金额")
    public double mod(double a, double b) {
        return a % b;
    }

    /**
     * 绝对值
     *
     * @param a 输入值
     * @return 输入值的绝对值
     */
    @MethodName(prefix = "math", method = "abs",
            paramType = {"整数｜小数｜金额"},
            desc = "返回参数的绝对值",
            returnObj = "整数｜小数｜金额")
    public double abs(double a) {
        return Math.abs(a);
    }

    /**
     * 平方根
     *
     * @param a 输入值
     * @return 输入值的平方根
     */
    @MethodName(prefix = "math", method = "sqrt",
            paramType = {"整数｜小数｜金额"},
            desc = "返回参数的平方根",
            returnObj = "小数")
    public double sqrt(double a) {
        return Math.sqrt(a);
    }

    /**
     * 立方根
     *
     * @param a 输入值
     * @return 输入值的立方根
     */
    @MethodName(prefix = "math", method = "cbrt",
            paramType = {"整数｜小数｜金额"},
            desc = "返回参数的立方根",
            returnObj = "小数")
    public double cbrt(double a) {
        return Math.cbrt(a);
    }

    /**
     * 幂运算
     *
     * @param a 底数
     * @param b 指数
     * @return a的b次方
     */
    @MethodName(prefix = "math", method = "pow",
            paramType = {"整数｜小数｜金额", "整数｜小数｜金额"},
            desc = "幂运算，返回a的b次方",
            returnObj = "小数")
    public double pow(double a, double b) {
        return Math.pow(a, b);
    }

    /**
     * 自然对数
     *
     * @param a 输入值
     * @return 输入值的自然对数
     */
    @MethodName(prefix = "math", method = "log",
            paramType = {"整数｜小数｜金额"},
            desc = "返回参数的自然对数（以e为底）",
            returnObj = "小数")
    public double log(double a) {
        return Math.log(a);
    }

    /**
     * 以10为底的对数
     *
     * @param a 输入值
     * @return 输入值的以10为底的对数
     */
    @MethodName(prefix = "math", method = "log10",
            paramType = {"整数｜小数｜金额"},
            desc = "返回参数的以10为底的对数",
            returnObj = "小数")
    public double log10(double a) {
        return Math.log10(a);
    }

    /**
     * 以2为底的对数
     *
     * @param a 输入值
     * @return 输入值的以2为底的对数
     */
    @MethodName(prefix = "math", method = "log2",
            paramType = {"整数｜小数｜金额"},
            desc = "返回参数的以2为底的对数",
            returnObj = "小数")
    public double log2(double a) {
        return Math.log(a) / Math.log(2);
    }

    /**
     * 指数函数（e的幂）
     *
     * @param a 指数
     * @return e的a次方
     */
    @MethodName(prefix = "math", method = "exp",
            paramType = {"整数｜小数｜金额"},
            desc = "指数函数，返回e的a次方",
            returnObj = "小数")
    public double exp(double a) {
        return Math.exp(a);
    }

    /**
     * 最大值
     *
     * @param values 输入值数组
     * @return 输入值中的最大值
     */
    @MethodName(prefix = "math", method = "max",
            paramType = {"整数｜小数｜金额..."},
            desc = "返回多个参数中的最大值",
            returnObj = "整数｜小数｜金额")
    public double max(double... values) {
        double max = values[0];
        for (double value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 最大值（支持任意数字类型）
     *
     * @param numbers 输入数字数组
     * @return 输入数字中的最大值
     */
    @MethodName(prefix = "math", method = "max",
            paramType = {"数字..."},
            desc = "返回多个数字参数中的最大值（支持任意数字类型）",
            returnObj = "数字")
    public Number max(Number... numbers) {
        if (numbers.length == 0) {
            throw new IllegalArgumentException("At least one number is required");
        }
        double max = numbers[0].doubleValue();
        for (Number number : numbers) {
            if (number.doubleValue() > max) {
                max = number.doubleValue();
            }
        }
        return max;
    }

    /**
     * 最小值
     *
     * @param values 输入值数组
     * @return 输入值中的最小值
     */
    @MethodName(prefix = "math", method = "min",
            paramType = {"整数｜小数｜金额..."},
            desc = "返回多个参数中的最小值",
            returnObj = "整数｜小数｜金额")
    public double min(double... values) {
        double min = values[0];
        for (double value : values) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    /**
     * 随机数（0.0到1.0之间）
     *
     * @return 0.0到1.0之间的随机数
     */
    @MethodName(prefix = "math", method = "random",
            paramType = {},
            desc = "返回0.0到1.0之间的随机数",
            returnObj = "小数")
    public double random() {
        return Math.random();
    }

    /**
     * 向上取整
     *
     * @param a 输入值
     * @return 大于或等于输入值的最小整数
     */
    @MethodName(prefix = "math", method = "ceil",
            paramType = {"整数｜小数｜金额"},
            desc = "向上取整，返回大于或等于参数的最小整数",
            returnObj = "小数")
    public double ceil(double a) {
        return Math.ceil(a);
    }

    /**
     * 向下取整
     *
     * @param a 输入值
     * @return 小于或等于输入值的最大整数
     */
    @MethodName(prefix = "math", method = "floor",
            paramType = {"整数｜小数｜金额"},
            desc = "向下取整，返回小于或等于参数的最大整数",
            returnObj = "小数")
    public double floor(double a) {
        return Math.floor(a);
    }

    /**
     * 四舍五入
     *
     * @param a 输入值
     * @return 输入值四舍五入后的整数
     */
    @MethodName(prefix = "math", method = "round",
            paramType = {"整数｜小数｜金额"},
            desc = "四舍五入，返回参数四舍五入后的整数",
            returnObj = "整数")
    public long round(double a) {
        return Math.round(a);
    }

    /**
     * 正弦函数
     *
     * @param a 角度（弧度）
     * @return 输入角度的正弦值
     */
    @MethodName(prefix = "math", method = "sin",
            paramType = {"角度（弧度）"},
            desc = "正弦函数，返回参数的正弦值",
            returnObj = "小数")
    public double sin(double a) {
        return Math.sin(a);
    }

    /**
     * 余弦函数
     *
     * @param a 角度（弧度）
     * @return 输入角度的余弦值
     */
    @MethodName(prefix = "math", method = "cos",
            paramType = {"角度（弧度）"},
            desc = "余弦函数，返回参数的余弦值",
            returnObj = "小数")
    public double cos(double a) {
        return Math.cos(a);
    }

    /**
     * 正切函数
     *
     * @param a 角度（弧度）
     * @return 输入角度的正切值
     */
    @MethodName(prefix = "math", method = "tan",
            paramType = {"角度（弧度）"},
            desc = "正切函数，返回参数的正切值",
            returnObj = "小数")
    public double tan(double a) {
        return Math.tan(a);
    }

    /**
     * 反正弦函数
     *
     * @param a 输入值
     * @return 输入值的反正弦值（弧度）
     */
    @MethodName(prefix = "math", method = "asin",
            paramType = {"小数"},
            desc = "反正弦函数，返回参数的反正弦值（弧度）",
            returnObj = "角度（弧度）")
    public double asin(double a) {
        return Math.asin(a);
    }

    /**
     * 反余弦函数
     *
     * @param a 输入值
     * @return 输入值的反余弦值（弧度）
     */
    @MethodName(prefix = "math", method = "acos",
            paramType = {"小数"},
            desc = "反余弦函数，返回参数的反余弦值（弧度）",
            returnObj = "角度（弧度）")
    public double acos(double a) {
        return Math.acos(a);
    }

    /**
     * 反正切函数
     *
     * @param a 输入值
     * @return 输入值的反正切值（弧度）
     */
    @MethodName(prefix = "math", method = "atan",
            paramType = {"整数｜小数｜金额"},
            desc = "反正切函数，返回参数的反正切值（弧度）",
            returnObj = "角度（弧度）")
    public double atan(double a) {
        return Math.atan(a);
    }

    /**
     * 角度转弧度
     *
     * @param degrees 角度值
     * @return 对应的弧度值
     */
    @MethodName(prefix = "math", method = "toRadians",
            paramType = {"角度"},
            desc = "角度转弧度，将角度值转换为弧度值",
            returnObj = "弧度")
    public double toRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    /**
     * 弧度转角度
     *
     * @param radians 弧度值
     * @return 对应的角度值
     */
    @MethodName(prefix = "math", method = "toDegrees",
            paramType = {"弧度"},
            desc = "弧度转角度，将弧度值转换为角度值",
            returnObj = "角度")
    public double toDegrees(double radians) {
        return Math.toDegrees(radians);
    }

    /**
     * 双曲正弦函数
     *
     * @param a 输入值
     * @return 输入值的双曲正弦值
     */
    @MethodName(prefix = "math", method = "sinh",
            paramType = {"整数｜小数｜金额"},
            desc = "双曲正弦函数，返回参数的双曲正弦值",
            returnObj = "小数")
    public double sinh(double a) {
        return Math.sinh(a);
    }

    /**
     * 双曲余弦函数
     *
     * @param a 输入值
     * @return 输入值的双曲余弦值
     */
    @MethodName(prefix = "math", method = "cosh",
            paramType = {"整数｜小数｜金额"},
            desc = "双曲余弦函数，返回参数的双曲余弦值",
            returnObj = "小数")
    public double cosh(double a) {
        return Math.cosh(a);
    }

    /**
     * 双曲正切函数
     *
     * @param a 输入值
     * @return 输入值的双曲正切值
     */
    @MethodName(prefix = "math", method = "tanh",
            paramType = {"整数｜小数｜金额"},
            desc = "双曲正切函数，返回参数的双曲正切值",
            returnObj = "小数")
    public double tanh(double a) {
        return Math.tanh(a);
    }

    /**
     * 欧拉常数e
     *
     * @return 欧拉常数e的值
     */
    @MethodName(prefix = "math", method = "getE",
            paramType = {},
            desc = "返回欧拉常数e的值",
            returnObj = "小数")
    public double getE() {
        return Math.E;
    }

    /**
     * 圆周率π
     *
     * @return 圆周率π的值
     */
    @MethodName(prefix = "math", method = "getPI",
            paramType = {},
            desc = "返回圆周率π的值",
            returnObj = "小数")
    public double getPI() {
        return Math.PI;
    }
}