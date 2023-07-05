package com.star.enterprise.order.base.utils;

import java.util.Map;

/**
 * 泛型map工具
 * @author xiaowenrou
 * @date 2022/8/22
 */
public abstract class ParameterizedTypeMapUtils {

    /**
     * 从泛型Map中取值
     * @param map
     * @param key
     * @param requiredClass
     * @return
     * @param <T>
     */
    public static <T> T getAttribute(Map<String, Object> map, String key, Class<T> requiredClass) {
        var obj = map.get(key);
        if (obj == null) {
            return null;
        }
        if (!requiredClass.isInstance(obj)) {
            throw new IllegalArgumentException("Expected value to be of type: " + requiredClass + ", but was " + obj.getClass());
        } else {
            return requiredClass.cast(obj);
        }
    }

}
