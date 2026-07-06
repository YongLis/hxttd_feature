package com.ly.ttd.language.srv.impl.jexl.fun;

import com.alibaba.fastjson2.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.tools.ant.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author yong.li
 * @since 2026/6/11 08:58
 */
public class JexlFunction {


    /**
     * 判断集合是否存在交集
     */
    public boolean existIntersection(Object a, Object b) {

        if (null == a || null == b) {
            return false;
        }
        Collection<?> tmpA = null;
        Collection<?> tmpB = null;
        if (a instanceof String) {
            String aStr = (String) a;
            if (aStr.startsWith("[")) {
                // 数组
                tmpA = JSONArray.parseArray(aStr);
            } else {
                tmpA = Arrays.asList(aStr.split(","));
            }
        } else if (a instanceof Collection<?>) {
            tmpA = (Collection<?>) a;
        }

        if (CollectionUtils.isEmpty(tmpA)) {
            return false;
        }


        if (b instanceof String) {
            String bStr = (String) b;
            if (bStr.startsWith("[")) {
                // 数组
                tmpB = JSONArray.parseArray(bStr);
            } else {
                tmpB = Arrays.asList(bStr.split(","));
            }
        } else if (b.getClass().isArray()) {
            tmpB = Arrays.stream((Object[]) b).toList();
        } else {
            tmpB = (Collection<?>) b;
        }
        if (CollectionUtils.isEmpty(tmpB)) {
            return false;
        }
        return CollectionUtils.isNotEmpty(CollectionUtils.intersection(tmpA.stream().toList(), tmpB.stream().toList()));
    }

    /**
     * 集合转字符串
     */
    public String join(Collection<?> list, String separator) {
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        return StringUtils.join(list, separator);
    }


    /**
     * 参数转字符串
     */
    public String joinStr(String separator, String... tmp) {
        return StringUtils.join(Arrays.asList(tmp
        ), separator);
    }

}
