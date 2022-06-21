package com.github.stazxr.zblog.log.aop;

import com.github.stazxr.zblog.core.util.SpringContextUtils;
import com.github.stazxr.zblog.log.annotation.properties.LogProperties;
import com.github.stazxr.zblog.log.domain.entity.Log;
import com.github.stazxr.zblog.log.domain.enums.LogType;
import com.github.stazxr.zblog.log.service.LogService;
import com.github.stazxr.zblog.util.ThrowableUtils;
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
            log.info("Enabled Log: {}", enabledLog);
        } catch (Exception e) {
            log.warn("日志组件未开启，如果需要开启日志组件，请开启 @EnableLog");
            enabledLog = false;
        }
    }

    /**
     * 配置切入点，Log
     */
    @Pointcut("@annotation(com.github.stazxr.zblog.log.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 配置切入点，Controller的所有接口
     */
    @Pointcut("execution(public * com.github.stazxr.zblog..*.controller..*.*(..))")
    public void expLogPointCut() {
    }

    /**
     * 配置环绕通知
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (enabledLog) {
            String operateTime = DateUtils.formatNow();
            currentTime.set(System.currentTimeMillis());
            Object result = joinPoint.proceed();
            Log log = new Log(LogType.INFO, operateTime, System.currentTimeMillis() - currentTime.get());
            currentTime.remove();
            logService.saveLog(getHttpServletRequest(), joinPoint, log);
            System.out.println("result: " + result);
            return result;
        } else {
            return joinPoint.proceed();
        }
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (enabledLog) {
            String occurTime = DateUtils.formatNow();
            Log log = new Log(LogType.ERROR, occurTime,System.currentTimeMillis() - currentTime.get());
            currentTime.remove();
            log.setExceptionDetail(ThrowableUtils.getStackTrace(e).getBytes());
            logService.saveLog(getHttpServletRequest(), (ProceedingJoinPoint) joinPoint, log);
        }
    }

    /**
     * 配置返回通知
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "logPointCut()", returning = "keys")
    public void saveOperateLog(JoinPoint joinPoint, Object keys) {
    }

    private HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
