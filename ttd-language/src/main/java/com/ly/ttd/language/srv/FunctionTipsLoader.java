package com.ly.ttd.language.srv;

import com.ly.ttd.language.srv.tip.FunctionDef;
import com.ly.ttd.language.srv.tip.FunctionTip;
import com.ly.ttd.language.srv.tip.MethodName;
import com.ly.ttd.language.srv.tip.ParamObj;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yong.li
 * @since 2026/6/24 22:06
 */
public class FunctionTipsLoader {


    public static List<FunctionTip> loadFunctionTips(List<String> packagePaths) throws Exception {
        List<FunctionTip> list = new ArrayList<>();
        for (String packageName : packagePaths) {
            ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
            // 如果只想获取标注了特定注解的类，可以添加过滤器
            provider.addIncludeFilter(new AnnotationTypeFilter(FunctionDef.class));
            Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents(packageName);
            for (BeanDefinition bd : beanDefinitions) {
                String className = bd.getBeanClassName();
                list.addAll(loadFunctionTip(className));
            }
        }

        return list;
    }


    private static List<FunctionTip> loadFunctionTip(String className) throws Exception {
        Class<?> clz = Class.forName(className);
        Method[] methods = clz.getDeclaredMethods();
        List<FunctionTip> tips = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(MethodName.class)) {
                MethodName methodName = method.getAnnotation(MethodName.class);
                FunctionTip tip = new FunctionTip();
                tip.setGroup(methodName.prefix());
                tip.setMethodName(methodName.method());
                tip.setDescription(methodName.desc());
                String[] paramTypes = methodName.paramType();
                if (paramTypes != null) {
                    List<ParamObj> params = new ArrayList<>();
                    for (int i = 0; i < paramTypes.length; i++) {
                        ParamObj obj = new ParamObj();
                        obj.setType(paramTypes[i]);
                        obj.setName("参数" + (i + 1));
                        params.add(obj);
                    }
                    tip.setParams(params);
                }
                tip.setReturnObj(methodName.returnObj());
                tips.add(tip);
            }
        }
        return tips;
    }

}
