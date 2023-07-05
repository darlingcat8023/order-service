package com.star.enterprise.order.base.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author xiaowenrou
 * @date 2022/9/21
 */
public abstract class DateTimeUtils {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String TIME_PATTERN = "HH:mm:ss";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    public static final ZoneOffset ZONE_OFFSET = ZoneOffset.of("+8");

    /**
     * 转换Unix时间戳
     * @param localDateTime
     * @return
     */
    public static long convertTimestamp(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZONE_OFFSET).toEpochMilli();
    }

}
