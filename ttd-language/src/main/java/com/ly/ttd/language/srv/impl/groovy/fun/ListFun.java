package com.ly.ttd.language.srv.impl.groovy.fun;

import com.ly.ttd.language.srv.tip.FunctionDef;
import com.ly.ttd.language.srv.tip.MethodName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yong.li
 * @since 2026/1/23 12:59
 */
@FunctionDef
public class ListFun implements GroovyFun {
    @Override
    public String getFunName() {
        return "list";
    }

    /**
     * 创建一个空列表
     *
     * @return 新创建的空列表
     */
    @MethodName(prefix = "list", method = "create",
            paramType = {},
            desc = "创建一个空列表",
            returnObj = "列表")
    public List<Object> create() {
        return new ArrayList<>();
    }

    /**
     * 创建一个包含指定元素的列表
     *
     * @param elements 要添加到列表中的元素
     * @return 包含指定元素的新列表
     */
    @MethodName(prefix = "list", method = "of",
            paramType = {"元素..."},
            desc = "创建一个包含指定元素的列表",
            returnObj = "列表")
    public List<Object> of(Object... elements) {
        List<Object> list = new ArrayList<>();
        for (Object element : elements) {
            list.add(element);
        }
        return list;
    }

    /**
     * 向列表中添加元素
     *
     * @param list    目标列表
     * @param element 要添加的元素
     * @return 添加元素后的列表
     */
    @MethodName(prefix = "list", method = "add",
            paramType = {"列表", "元素"},
            desc = "向列表中添加元素",
            returnObj = "列表")
    public List<Object> add(List<Object> list, Object element) {
        list.add(element);
        return list;
    }

    /**
     * 向列表指定位置添加元素
     *
     * @param list    目标列表
     * @param index   要添加元素的位置
     * @param element 要添加的元素
     * @return 添加元素后的列表
     */
    @MethodName(prefix = "list", method = "addAt",
            paramType = {"列表", "整数", "元素"},
            desc = "向列表指定位置添加元素",
            returnObj = "列表")
    public List<Object> addAt(List<Object> list, int index, Object element) {
        list.add(index, element);
        return list;
    }

    /**
     * 从列表中移除指定元素
     *
     * @param list    目标列表
     * @param element 要移除的元素
     * @return 移除元素后的列表
     */
    @MethodName(prefix = "list", method = "remove",
            paramType = {"列表", "元素"},
            desc = "从列表中移除指定元素",
            returnObj = "列表")
    public List<Object> remove(List<Object> list, Object element) {
        list.remove(element);
        return list;
    }

    /**
     * 从列表中移除指定位置的元素
     *
     * @param list  目标列表
     * @param index 要移除元素的位置
     * @return 移除元素后的列表
     */
    @MethodName(prefix = "list", method = "removeAt",
            paramType = {"列表", "整数"},
            desc = "从列表中移除指定位置的元素",
            returnObj = "列表")
    public List<Object> removeAt(List<Object> list, int index) {
        list.remove(index);
        return list;
    }

    /**
     * 获取列表指定位置的元素
     *
     * @param list  目标列表
     * @param index 要获取元素的位置
     * @return 列表中指定位置的元素
     */
    @MethodName(prefix = "list", method = "get",
            paramType = {"列表", "整数"},
            desc = "获取列表指定位置的元素",
            returnObj = "元素")
    public Object get(List<Object> list, int index) {
        return list.get(index);
    }

    /**
     * 设置列表指定位置的元素
     *
     * @param list    目标列表
     * @param index   要设置元素的位置
     * @param element 要设置的元素
     * @return 设置元素后的列表
     */
    @MethodName(prefix = "list", method = "set",
            paramType = {"列表", "整数", "元素"},
            desc = "设置列表指定位置的元素",
            returnObj = "列表")
    public List<Object> set(List<Object> list, int index, Object element) {
        list.set(index, element);
        return list;
    }

    /**
     * 获取列表的大小
     *
     * @param list 目标列表
     * @return 列表的大小
     */
    @MethodName(prefix = "list", method = "size",
            paramType = {"列表"},
            desc = "获取列表的大小",
            returnObj = "整数")
    public int size(List<Object> list) {
        return list.size();
    }

    /**
     * 检查列表是否为空
     *
     * @param list 目标列表
     * @return 如果列表为空则返回true，否则返回false
     */
    @MethodName(prefix = "list", method = "isEmpty",
            paramType = {"列表"},
            desc = "检查列表是否为空",
            returnObj = "布尔值")
    public boolean isEmpty(List<Object> list) {
        return list.isEmpty();
    }

    /**
     * 检查列表是否包含指定元素
     *
     * @param list    目标列表
     * @param element 要检查的元素
     * @return 如果列表包含指定元素则返回true，否则返回false
     */
    @MethodName(prefix = "list", method = "contains",
            paramType = {"列表", "元素"},
            desc = "检查列表是否包含指定元素",
            returnObj = "布尔值")
    public boolean contains(List<Object> list, Object element) {
        return list.contains(element);
    }

    /**
     * 获取指定元素在列表中第一次出现的位置
     *
     * @param list    目标列表
     * @param element 要查找的元素
     * @return 元素在列表中第一次出现的位置，如果列表不包含该元素则返回-1
     */
    @MethodName(prefix = "list", method = "indexOf",
            paramType = {"列表", "元素"},
            desc = "获取指定元素在列表中第一次出现的位置",
            returnObj = "整数")
    public int indexOf(List<Object> list, Object element) {
        return list.indexOf(element);
    }

    /**
     * 获取指定元素在列表中最后一次出现的位置
     *
     * @param list    目标列表
     * @param element 要查找的元素
     * @return 元素在列表中最后一次出现的位置，如果列表不包含该元素则返回-1
     */
    @MethodName(prefix = "list", method = "lastIndexOf",
            paramType = {"列表", "元素"},
            desc = "获取指定元素在列表中最后一次出现的位置",
            returnObj = "整数")
    public int lastIndexOf(List<Object> list, Object element) {
        return list.lastIndexOf(element);
    }

    /**
     * 清空列表中的所有元素
     *
     * @param list 目标列表
     * @return 清空后的列表
     */
    @MethodName(prefix = "list", method = "clear",
            paramType = {"列表"},
            desc = "清空列表中的所有元素",
            returnObj = "列表")
    public List<Object> clear(List<Object> list) {
        list.clear();
        return list;
    }

    /**
     * 对列表进行排序
     *
     * @param list 目标列表
     * @return 排序后的列表
     */
    @MethodName(prefix = "list", method = "sort",
            paramType = {"列表"},
            desc = "对列表进行排序",
            returnObj = "列表")
    public List<Object> sort(List<Object> list) {
        list.sort(null);
        return list;
    }

    /**
     * 反转列表中的元素顺序
     *
     * @param list 目标列表
     * @return 反转后的列表
     */
    @MethodName(prefix = "list", method = "reverse",
            paramType = {"列表"},
            desc = "反转列表中的元素顺序",
            returnObj = "列表")
    public List<Object> reverse(List<Object> list) {
        List<Object> reversed = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            reversed.add(list.get(i));
        }
        return reversed;
    }

    /**
     * 复制列表
     *
     * @param list 要复制的列表
     * @return 列表的副本
     */
    @MethodName(prefix = "list", method = "copy",
            paramType = {"列表"},
            desc = "复制列表，返回列表的副本",
            returnObj = "列表")
    public List<Object> copy(List<Object> list) {
        return new ArrayList<>(list);
    }

    /**
     * 合并两个列表
     *
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return 合并后的列表
     */
    @MethodName(prefix = "list", method = "merge",
            paramType = {"列表", "列表"},
            desc = "合并两个列表",
            returnObj = "列表")
    public List<Object> merge(List<Object> list1, List<Object> list2) {
        List<Object> merged = new ArrayList<>(list1);
        merged.addAll(list2);
        return merged;
    }

    /**
     * 截取列表的一部分
     *
     * @param list      目标列表
     * @param fromIndex 开始位置（包含）
     * @param toIndex   结束位置（不包含）
     * @return 截取后的列表
     */
    @MethodName(prefix = "list", method = "subList",
            paramType = {"列表", "整数", "整数"},
            desc = "截取列表的一部分",
            returnObj = "列表")
    public List<Object> subList(List<Object> list, int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }


    /**
     * 最大值
     *
     * @param values 输入值数组
     * @return 输入值中的最大值
     */
    @MethodName(prefix = "list", method = "max",
            paramType = {"小数..."},
            desc = "返回多个小数参数中的最大值",
            returnObj = "小数")
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
     * 最大值
     *
     * @param values 输入值数组
     * @return 输入值中的最大值
     */
    @MethodName(prefix = "list", method = "max",
            paramType = {"整数..."},
            desc = "返回多个整数参数中的最大值",
            returnObj = "整数")
    public int max(int... values) {
        int max = values[0];
        for (int value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 最大值
     *
     * @param values 输入值数组
     * @return 输入值中的最大值
     */
    @MethodName(prefix = "list", method = "max",
            paramType = {"单精度小数..."},
            desc = "返回多个单精度小数参数中的最大值",
            returnObj = "单精度小数")
    public float max(float... values) {
        float max = values[0];
        for (float value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 最大值（支持数字列表）
     *
     * @param list 数字列表
     * @return 列表中的最大值
     */
    @MethodName(prefix = "list", method = "max",
            paramType = {"数字列表"},
            desc = "返回列表中的最大值（支持数字列表）",
            returnObj = "数字")
    public Number max(List<Object> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        double max = Double.NEGATIVE_INFINITY;
        for (Object element : list) {
            if (element instanceof Number) {
                double value = ((Number) element).doubleValue();
                if (value > max) {
                    max = value;
                }
            }
        }
        if (max == Double.NEGATIVE_INFINITY) {
            throw new IllegalArgumentException("List must contain at least one number");
        }
        return max;
    }

}