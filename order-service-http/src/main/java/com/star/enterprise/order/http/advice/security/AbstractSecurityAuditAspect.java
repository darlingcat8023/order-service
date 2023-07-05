package com.star.enterprise.order.http.advice.security;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.core.adapter.EmployeeUserAdapter;
import com.star.enterprise.order.core.model.EmployeeUser;
import com.star.enterprise.order.remote.security.RemoteSecurityService;
import com.star.enterprise.order.remote.security.request.RemoteTokenVerifyRequest;
import com.star.enterprise.order.remote.security.response.RemoteTokenVerifyResponse;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.star.enterprise.order.base.exception.RestCode.HTTP_SECURITY_DECLINE;

/**
 * @author xiaowenrou
 * @date 2023/2/2
 */
public abstract class AbstractSecurityAuditAspect implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 执行切点方法
     * @param invoker
     * @return
     * @throws Throwable
     */
    public Object execute(ProceedingInvoker invoker) throws Throwable {
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.getUserPrincipal()
        var token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            throw new BusinessWarnException(HTTP_SECURITY_DECLINE, "error.security.notToken");
        }
        var employee = this.verifyEmployee(this.applicationContext.getBean(RemoteSecurityService.class), token);
        WebSecurityContext.setSecurity(employee);
        try {
            return invoker.invoke();
        } finally {
            WebSecurityContext.removeSecurity();
        }
    }

    /**
     * 解析企业用户的token
     * @param token
     * @return
     */
    private EmployeeUser verifyEmployee(RemoteSecurityService remoteSecurityService, String token) {
        var response = remoteSecurityService.verifyToken(new RemoteTokenVerifyRequest(token));
        if (response == null || RemoteTokenVerifyResponse.empty().equals(response)) {
            throw new BusinessWarnException(HTTP_SECURITY_DECLINE, "error.security.notFound");
        }
        return new EmployeeUserAdapter(response.employeeId(), response.employeeName(), response.isAdmin());
    }

    public interface ProceedingInvoker {
        /**
         * 执行切点方法，包装异常类
         * @return
         * @throws
         */
        Object invoke() throws Throwable;

    }

}
