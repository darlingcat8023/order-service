package com.star.enterprise.order.base.utils;

import com.star.enterprise.order.base.EnumSerialize;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xiaowenrou
 * @date 2023/1/5
 */
public abstract class EnumSerializeUtils {

    public static Map<String, String> toMap(EnumSerialize[] serializes) {
        return Arrays.stream(serializes).collect(Collectors.toMap(EnumSerialize::value, EnumSerialize::desc, (a, b) -> a, HashMap::new));
    }

}
