package com.github.stazxr.zblog.log.aop;

import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.core.util.SpringContextUtils;
import com.github.stazxr.zblog.log.annotation.properties.LogProperties;
import com.github.stazxr.zblog.log.domain.entity.Log;
import com.github.stazxr.zblog.log.service.LogService;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 切面日志
 *
 * @author SunTao
 * @since 2021-05-16
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    private final LogService logService;

    private boolean enabledLog;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    public LogAspect(LogService logService) {
        this.logService = logService;

        try {
            LogProperties logProperties = SpringContextUtils.getBean(LogProperties.class);
            enabledLog = logProperties.isEnabled();
            log.info("Global Log Status [{}]", enabledLog ? "OPEN" : "CLOSE");
        } catch (Exception e) {
            log.warn("日志组件未开启，如果需要开启日志组件，请开启 @EnableLog");
            enabledLog = false;
        }
    }

    /**
     * 配置接口的切入点，扫描所有controller包下的异常信息
     */
    @Pointcut("execution(public * com.github.stazxr.zblog..*Controller.*(..))")
    public void ctlLogPointCut() {
    }

    /**
     * 配置 @Log 注解的环绕通知，记录操作日志
     *
     * @param joinPoint join point for advice
     */
    @Around("ctlLogPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!enabledLog) {
            return joinPoint.proceed();
        }

        // exec
        String operateTime = DateUtils.formatNow();
        currentTime.set(System.currentTimeMillis());
        Object result = joinPoint.proceed();
        long costTime = System.currentTimeMillis() - currentTime.get();
        currentTime.remove();

        try {
            // save log
            Log log = new Log();
            log.setCostTime(costTime);
            log.setEventTime(operateTime);
            log.setRequestInfo(getHttpServletRequest());
            log.setOperateUser(SecurityUtils.getLoginUsernameNoEor());
            CompletableFuture.runAsync(() -> logService.saveLog(joinPoint, log, result, null));
        } catch (Exception e) {
            log.error("==================== LogAspect[logPointCut] catch eor", e);
        }

        // return
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "ctlLogPointCut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        try {
            if (!enabledLog) {
                return;
            }

            // costTime
            Long costTime = currentTime.get() == null ? null : System.currentTimeMillis() - currentTime.get();
            currentTime.remove();

            // save log
            Log log = new Log();
            log.setCostTime(costTime);
            log.setEventTime(DateUtils.formatNow());
            log.setRequestInfo(getHttpServletRequest());
            log.setOperateUser(SecurityUtils.getLoginUsernameNoEor());
            CompletableFuture.runAsync(() -> logService.saveLog((ProceedingJoinPoint) joinPoint, log, null, e));
        } catch (Exception ex) {
            log.error("==================== LogAspect[expLogPointCut] catch eor", e);
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
