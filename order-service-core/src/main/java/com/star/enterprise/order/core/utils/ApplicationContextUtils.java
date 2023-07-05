package com.star.enterprise.order.core.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/1/13
 */
public abstract class ApplicationContextUtils {

    public static <T> List<T> getBeans(ApplicationContext context, Class<T> clazz) {
        var beans = new ArrayList<>(context.getBeansOfType(clazz).values());
        AnnotationAwareOrderComparator.sort(beans);
        return beans;
    }

}
