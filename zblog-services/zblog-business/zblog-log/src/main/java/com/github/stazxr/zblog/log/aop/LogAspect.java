package com.github.stazxr.zblog.log.aop;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSON;
import com.github.stazxr.zblog.bas.exception.BaseException;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.log.annotation.IgnoreLog;
import com.github.stazxr.zblog.log.autoconfigure.properties.LogProperties;
import com.github.stazxr.zblog.log.domain.entity.Log;
import com.github.stazxr.zblog.log.domain.enums.LogType;
import com.github.stazxr.zblog.log.service.LogService;
import com.github.stazxr.zblog.log.service.impl.AsyncLogService;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.ThrowableUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 切面日志
 *
 * @author SunTao
 * @since 2021-05-16
 */
@Aspect
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    private final LogService logService;

    private final AsyncLogService asyncLogService;

    private final LogProperties logProperties;

    private static final String[] IGNORED_PARAMS = {"HttpServletResponse", "HttpServletRequest", "MultipartFile", "MultipartFile[]"};

    public LogAspect(LogService logService, AsyncLogService asyncLogService, LogProperties logProperties) {
        this.logService = logService;
        this.asyncLogService = asyncLogService;
        this.logProperties = logProperties;
        log.info("日志组件已启用...");
    }

    /**
     * 配置接口的切入点
     */
    @Pointcut("execution(public * com.github.stazxr.zblog..*Controller.*(..))")
    public void controllerPointCut() {
    }

    /**
     * 记录日志
     *
     * @param joinPoint join point for advice
     */
    @Around("controllerPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime eventTime = LocalDateTime.now();
        long startTime = System.currentTimeMillis();
        boolean success = false;
        Object result = null;
        Exception exception = null;

        try {
            Object o = joinPoint.proceed();
            success = true;
            result = o;
            return o;
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            if (logProperties.isEnabled()) {
                // 只有在开关开启的情况下才记录切面日志
                long costTime = System.currentTimeMillis() - startTime;
                saveLog(joinPoint, result, eventTime, costTime, success, exception);
            }
        }
    }

    /**
     * 保存切面日志
     */
    private void saveLog(ProceedingJoinPoint joinPoint, Object result, LocalDateTime eventTime, long costTime, boolean success, Exception exception) {
        try {
            // 获取方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            // 是否忽略切面日志
            if (method.isAnnotationPresent(IgnoreLog.class)) {
                return;
            }

            // 获取路由信息，如果接口未标注 @Router 注解，则不记录日志
            Router router = method.getAnnotation(Router.class);
            if (router == null) {
                return;
            }

            // 获取请求信息和用户信息
            HttpServletRequest request = getHttpServletRequest();
            String username = getLoginUser();

            // 获取日志描述信息
            LogType logType = LogType.INTERFACES;
            String description = router.name();
            com.github.stazxr.zblog.log.annotation.Log operationLog = method.getAnnotation(com.github.stazxr.zblog.log.annotation.Log.class);
            if (operationLog != null) {
                logType = LogType.OPERATION;
                if (StringUtils.isNotBlank(operationLog.value())) {
                    description = operationLog.value();
                }
            }

            // 初始化日志
            Log log = initLog(request, success, exception);
            log.setLogType(logType.getValue());
            log.setDescription(description);
            log.setInterfaceCode(router.code());
            log.setOperateUser(username);
            log.setEventTime(eventTime);
            log.setCostTime(costTime);

            // 参数信息
            if (logProperties.isRecordParam()) {
                String param = getParameter(method, joinPoint.getArgs());
                int maxLen = logProperties.getMaxParamLength();
                if (param.length() > maxLen) {
                    param = param.substring(0, maxLen - 6) + "......";
                }
                log.setRequestParam(param);
            }

            // 返回结果
            if (logProperties.isRecordResult()) {
                String resultJson = JSON.toJSONString(result);
                int maxLen = logProperties.getMaxResultLength();
                if (resultJson.length() > maxLen) {
                    resultJson = resultJson.substring(0, maxLen - 6) + "......";
                }
                log.setRequestResult(resultJson);
            }

            // 记录日志
            if (logProperties.isAsync()) {
                asyncLogService.saveAsync(log);
            } else {
                logService.save(log);
            }
        } catch (Exception e) {
            log.error("切面日志记录失败", e);
        }
    }

    private Log initLog(HttpServletRequest request, boolean success, Exception exception) {
        Log log = new Log();
        log.setId(SequenceUtils.getId());
        log.setTraceId(MDC.get("traceId"));
        log.setExecResult(success);
        log.setCreateDate(LocalDate.now());

        // 获取请求信息
        String ua = IpUtils.getUserAgent(request);
        UserAgent userAgent = UserAgentUtil.parse(ua);
        String requestIp = IpUtils.getIp(request);

        log.setRequestIp(requestIp);
        log.setRequestUri(request.getRequestURI());
        log.setRequestMethod(request.getMethod().toUpperCase(Locale.ROOT));
        log.setAddress(IpUtils.getIpLocation(requestIp, IpUtils.IP_LOCATION_PRO));

        log.setUserAgent(ua);
        log.setBrowser(userAgent.getBrowser().getName());
        log.setBrowserVersion(userAgent.getVersion());
        log.setPlatform(userAgent.getPlatform().getName());

        // 设置返回信息和异常信息
        if (exception != null) {
            if (exception instanceof BaseException) {
                BaseException be = (BaseException) exception;
                log.setExecMessage(be.getMessage());
                log.setErrorCode(be.getCode());
            } else {
                log.setExecMessage("系统异常：" + exception.getMessage());
            }
            log.setExceptionDetail(ThrowableUtils.getStackTrace(exception));
        } else {
            log.setExecMessage(success ? "操作成功" : "操作失败");
        }

        return log;
    }

    private String getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (Arrays.asList(IGNORED_PARAMS).contains(parameter.getType().getSimpleName())) {
                continue;
            }

            Object arg = args[i];
            if (arg == null) {
                continue;
            }

            RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            if (requestBody != null) {
                argList.add(arg);
            } else {
                Map<String, Object> map = new HashMap<>(4);
                String key = requestParam != null ? requestParam.value() : parameter.getName();
                if (StringUtils.isEmpty(key)) {
                    key = parameter.getName();
                }
                map.put(key, arg);
                argList.add(map);
            }
        }

        if (argList.isEmpty()) {
            return "";
        }

        if (argList.size() == 1) {
            return JSON.toJSONString(argList.get(0));
        }

        return JSON.toJSONString(argList);
    }

    private String getLoginUser() {
        try {
            return SecurityUtils.getLoginUsernameWithoutNull();
        } catch (Exception e) {
            return "anonymous";
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
