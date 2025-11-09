package com.github.stazxr.zblog.log.aop;

import com.alibaba.fastjson.JSON;
import com.github.stazxr.zblog.bas.context.util.SpringContextUtil;
import com.github.stazxr.zblog.bas.exception.BaseException;
import com.github.stazxr.zblog.bas.msg.ResultCode;
import com.github.stazxr.zblog.bas.reqsinglepost.RequestPostSingleParam;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.log.annotation.IgnoredLog;
import com.github.stazxr.zblog.log.annotation.properties.LogProperties;
import com.github.stazxr.zblog.log.domain.entity.Log;
import com.github.stazxr.zblog.log.domain.enums.LogType;
import com.github.stazxr.zblog.log.service.LogService;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.ThrowableUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.*;
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

    private static final String[] IGNORED_PARAM = {"HttpServletResponse", "HttpServletRequest", "MultipartFile", "MultipartFile[]"};

    private static final int MAX_PARAM_LENGTH = 65535;

    public LogAspect(LogService logService) {
        this.logService = logService;

        try {
            LogProperties logProperties = SpringContextUtil.getBean(LogProperties.class);
            enabledLog = logProperties.isEnabled();
            log.info("日志组件为{}状态...", enabledLog ? "开启" : "关闭");
        } catch (Exception e) {
            log.warn("日志组件未开启，如果需要开启日志组件，请开启 @EnableLog");
            enabledLog = false;
        }
    }

    /**
     * 配置接口的切入点，扫描所有controller包下的异常信息
     */
    @Pointcut("execution(public * com.github.stazxr.zblog..*Controller.*(..))")
    public void controllerPointCut() {
    }

    /**
     * 配置 @Log 注解的环绕通知，记录操作日志
     *
     * @param joinPoint join point for advice
     */
    @Around("controllerPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!enabledLog) {
            // 未开启组件日志
            return joinPoint.proceed();
        }

        // 切入日志
        LocalDateTime operateTime = LocalDateTime.now();
        currentTime.set(System.currentTimeMillis());
        boolean executeResult = false;
        Exception exception = null;
        try {
            // 执行业务逻辑
            Object result = joinPoint.proceed();
            executeResult = true;
            return result;
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            Long startTime = currentTime.get();
            callSaveLog(joinPoint, operateTime, startTime, executeResult, exception);
            currentTime.remove();
        }
    }

    private void callSaveLog(ProceedingJoinPoint joinPoint, LocalDateTime operateTime, Long startTime, boolean executeResult, Exception exception) {
        try {
            // 获取路由信息
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            // 是否忽略日志
            if (method.isAnnotationPresent(IgnoredLog.class)) {
                return;
            }

            // 获取用户和请求信息
            HttpServletRequest request = getHttpServletRequest();
            String username = SecurityUtils.getLoginUsernameWithoutNull();

            // 异步存储日志
//            CompletableFuture.runAsync(() -> {
                // 获取路由信息，如果接口未标注 @Router 注解，则忽略
                Router router = method.getAnnotation(Router.class);
                if (router == null) {
                    return;
                }

                // 初始化日志信息
                Log log = initLog(request, username, operateTime, startTime, executeResult, exception);
                log.setInterfaceCode(router.code());

                // 设置日志类型
                com.github.stazxr.zblog.log.annotation.Log operationLog = method.getAnnotation(com.github.stazxr.zblog.log.annotation.Log.class);
                if (operationLog != null) {
                    log.setLogType(LogType.OPERATION.getValue());
                    log.setDescription(StringUtils.isBlank(operationLog.value()) ? router.name() : operationLog.value());
                } else {
                    log.setLogType(LogType.INTERFACES.getValue());
                    log.setDescription(router.name());
                }

                // 设置参数信息
                String parameter = getParameter(method, joinPoint.getArgs());
                log.setRequestParam(parameter.length() > MAX_PARAM_LENGTH ? parameter.substring(0, MAX_PARAM_LENGTH - 7).concat("......") : parameter);

                // 日志入库
                logService.save(log);
//            });
        } catch (Exception e) {
            log.error("日志入库失败", e);
        }
    }

    private Log initLog(HttpServletRequest request, String username, LocalDateTime operateTime, Long startTime, boolean executeResult, Exception exception) {
        Log log = new Log();
        log.setId(SequenceUtils.getId());
        log.setOperateUser(username);
        log.setEventTime(operateTime);
        log.setExecResult(executeResult);
        log.setCostTime(System.currentTimeMillis() - startTime);

        // 获取请求信息
        String requestIp = IpUtils.getIp(request);
        log.setRequestIp(requestIp);
        log.setRequestUri(request.getRequestURI());
        log.setRequestMethod(request.getMethod().toUpperCase(Locale.ROOT));
        log.setAddress(IpUtils.getIpLocation(requestIp, IpUtils.IP_LOCATION_PRO));
        log.setBrowser(IpUtils.getBrowser(request));

        // 设置返回信息和异常信息
        if (exception != null) {
            if (exception instanceof BaseException) {
                log.setExecMessage(exception.getMessage());
            } else {
                String errorMessage = exception.getMessage() == null ? exception.getClass().getSimpleName() : exception.getMessage();
                log.setExecMessage("系统发生未知错误：".concat(errorMessage));
            }
            log.setExceptionDetail(ThrowableUtils.getStackTrace(exception).getBytes());
        } else {
            log.setExecMessage(executeResult ? ResultCode.SUCCESS.message() : ResultCode.FAILED.message());
        }

        return log;
    }

    private String getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String paramTypeName = parameter.getType().getSimpleName();
            if (Arrays.asList(IGNORED_PARAM).contains(paramTypeName)) {
                // 不记录HttpServletResponse、HttpServletRequest参数信息
                continue;
            }

            RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
            RequestPostSingleParam singleParam = parameter.getAnnotation(RequestPostSingleParam.class);
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            if (requestBody != null) {
                argList.add(args[i]);
            } else if (singleParam != null) {
                argList.add(args[i]);
            } else if (requestParam != null) {
                Map<String, Object> map = new HashMap<>(4);
                String key = StringUtils.isEmpty(requestParam.value()) ? parameter.getName() : requestParam.value();
                map.put(key, args[i]);
                argList.add(map);
            } else {
                argList.add(args[i]);
            }
        }

        return argList.isEmpty() ? "" : argList.size() == 1 ? JSON.toJSONString(argList.get(0)) : JSON.toJSONString(argList);
    }

    private HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
