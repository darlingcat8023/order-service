package com.star.enterprise.order.http.utils;

import lombok.SneakyThrows;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author xiaowenrou
 * @date 2023/1/9
 */
public abstract class MessageResolver {

    /**
     * 国际化输出
     * @param messageSource
     * @param messageKey
     * @param args
     * @return
     */
    @SneakyThrows
    public static String resolveMessage(MessageSource messageSource, String messageKey, Object ... args) {
        var local = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageKey, args, local);
    }

}
