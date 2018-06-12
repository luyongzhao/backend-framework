package com.lyz.backend.framework.annotation;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 带缓存的注解查找器<br />
 * 查找某个方法上指定的注解， 如果没有则从类上获取，否则返回默认的注解
 *
 * @param <A>
 * @author luyongzhao
 */
public class CachedAnnotationUtils<A extends Annotation> {
    private Map<Class<?>, A> classAnnotations = new HashMap<Class<?>, A>();
    private Map<Method, A> methodAnnotations = new HashMap<Method, A>();

    public A getAnnotation(HandlerMethod method, A defaultAnnotation, Class<A> annotationClass) {
        A a = methodAnnotations.get(method.getMethod());
        if (a == null) {// 方法缓存中未找到

            a = method.getMethodAnnotation(annotationClass);
            if (a == null) {// 方法中未找到
                Class<?> cls = method.getBean().getClass();
                a = classAnnotations.get(cls);
                if (a == null) {// 类缓存中未找到
                    a = AnnotationUtils.findAnnotation(cls, annotationClass);
                    if (a != null) {
                        classAnnotations.put(cls, a);
                    } else {// 所有的都未找到， 返回一个默认的Context, 所有需求都为false
                        a = defaultAnnotation;
                    }
                }
                methodAnnotations.put(method.getMethod(), a);
            } else {
                methodAnnotations.put(method.getMethod(), a);
            }
        }
        return a;
    }
}
