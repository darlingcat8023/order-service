package com.star.enterprise.order.http.advice;

import com.star.enterprise.order.base.Rest;
import com.star.enterprise.order.base.exception.BusinessErrorException;
import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.http.utils.MessageResolver;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.context.MessageSource;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.star.enterprise.order.base.exception.RestCode.*;

/**
 * @author xiaowenrou
 * @date 2022/9/16
 */
@Slf4j
@AllArgsConstructor
@RestControllerAdvice(value = "com.star.enterprise.order.http")
public class WebExceptionWrapper {

    private final MessageSource messageSource;

    @ExceptionHandler(value = {BindException.class, ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public Rest<Void> argumentException(Exception e) {
        if (log.isWarnEnabled()) {
            log.warn("参数校验失败:{}", e.getMessage());
        }
        if (e instanceof MethodArgumentNotValidException ma) {
            List<FieldError> fieldErrors = ma.getBindingResult().getFieldErrors();
            if (!CollectionUtils.isEmpty(fieldErrors)) {
                return Rest.just(ARGUMENT_NOT_VALID.getCode(), fieldErrors.get(0).getDefaultMessage(), null);
            }
            List<ObjectError> objectErrors = ma.getBindingResult().getGlobalErrors();
            if (!CollectionUtils.isEmpty(objectErrors)) {
                return Rest.just(ARGUMENT_NOT_VALID.getCode() ,objectErrors.get(0).getDefaultMessage(), null);
            }
        } else if (e instanceof BindException be) {
            return Rest.just(ARGUMENT_NOT_VALID.getCode(), be.getObjectName().concat("不能为空"), null);
        } else if (e instanceof HttpMessageNotReadableException hm) {
            return Rest.just(ARGUMENT_NOT_VALID.getCode(), "参数类型错误", null);
        } else if (e instanceof ConstraintViolationException cv) {
            return cv.getConstraintViolations().stream().findFirst().map(x -> Rest.just(ARGUMENT_NOT_VALID.getCode(), x.getMessage(), (Void)null)).get();
        }
        return Rest.just(ARGUMENT_NOT_VALID.getCode(), "参数异常", null);
    }

    /**
     * 触发限流
     * @param requestNotPermitted
     * @return
     */
    @ExceptionHandler(value = {RequestNotPermitted.class})
    public Rest<Void> rateLimitException(RequestNotPermitted requestNotPermitted) {
        return Rest.just(HTTP_RATE_LIMIT.getCode(), MessageResolver.resolveMessage(this.messageSource, "error.http.limit"), null);
    }

    /**
     * 业务失败
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {BusinessWarnException.class})
    public Rest<Void> businessWarnException(BusinessWarnException exception) {
        var message = MessageResolver.resolveMessage(this.messageSource, exception.getMessageKey(), exception.getVars());
        if (log.isWarnEnabled()) {
            log.warn(message);
        }
        return Rest.just(exception.getCode(), message, null);
    }

    /**
     * 业务失败
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {BusinessErrorException.class})
    public Rest<Void> businessErrorException(BusinessErrorException exception) {
        var message = MessageResolver.resolveMessage(this.messageSource, exception.getMessageKey(), exception.getVars());
        if (log.isErrorEnabled()) {
            log.error(message, exception);
        }
        return Rest.just(exception.getCode(), message, null);
    }

    /**
     * 远程服务错误
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {NoFallbackAvailableException.class})
    public Rest<Void> remoteServiceException(NoFallbackAvailableException exception) {
        var message = exception.getCause().getMessage();
        return Rest.just(REMOTE_SERVICE_FAIL.getCode(), message, null);
    }

    /**
     * 未知错误
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    public Rest<Void> unknownException(Exception exception) {
        log.error(exception.getMessage(), exception);
        var message = MessageResolver.resolveMessage(this.messageSource, "error.unknown");
        return Rest.just(UNKNOWN_ERROR.getCode(), message, null);
    }

}
