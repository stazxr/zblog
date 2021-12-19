//package com.github.stazxr.zblog.log.aop;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.TypeReference;
//import com.github.stazxr.blog.core.annotation.OperateLog;
//import com.github.stazxr.blog.core.util.IpUtil;
//import com.github.stazxr.blog.core.util.UuidUtil;
//import com.github.stazxr.blog.system.admin.entity.OperationExpLog;
//import com.github.stazxr.blog.system.admin.entity.OperationLog;
//import com.github.stazxr.blog.system.admin.service.OperateExpLogService;
//import com.github.stazxr.blog.system.admin.service.OperateLogService;
//import com.github.stazxr.zblog.core.model.Result;
//import com.github.stazxr.zblog.util.time.DateUtils;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 切面记录系统操作日志与异常日志
// *
// * @author SunTao
// * @since 2021-05-16
// */
//@Slf4j
//@Aspect
//@Component
//@AllArgsConstructor
//public class LogAspect {
//    private static final String RESULT_CLASS = "Result";
//
//    private static final String BOOLEAN_CLASS = "Boolean";
//
//    private final OperateLogService operateLogService;
//
//    private final OperateExpLogService operateExpLogService;
//
//    private final SecurityManager securityManager;
//
//    /**
//     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
//     */
//    @Pointcut("@annotation(com.github.stazxr.blog.core.annotation.OperateLog)")
//    public void operateLogPointCut() {
//    }
//
//    /**
//     * 设置操作异常切入点记录异常日志 扫描所有controller包下操作
//     */
//    @Pointcut("execution(public * com.github.stazxr.blog.system.admin.controller..*.*(..))")
//    public void operateExpLogPointCut() {
//    }
//
//    /**
//     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行，如果连接点抛出异常，则不会执行
//     *
//     * @param joinPoint 切入点
//     * @param keys      返回结果
//     */
//    @AfterReturning(value = "operateLogPointCut()", returning = "keys")
//    public void saveOperateLog(JoinPoint joinPoint, Object keys) {
//        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
//        if (attributes == null) {
//            return;
//        }
//
//        try {
//            // 获取Request对象
//            HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
//
//            // 从切面织入点处通过反射机制获取织入点处的方法
//            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//            Method method = signature.getMethod();
//
//            // 设置操作信息
//            OperationLog operateLog = new OperationLog();
//            operateLog.setOperateId(UuidUtil.generateShortUuid());
//
//            OperateLog opLog = method.getAnnotation(OperateLog.class);
//            if (opLog != null) {
//                operateLog.setOperateName(opLog.name());
//                operateLog.setOperateModule(opLog.module());
//                operateLog.setOperateType(opLog.type().name());
//                operateLog.setOperateLevel(opLog.level().name());
//            }
//
//            // 设置响应结果
//            if (RESULT_CLASS.equals(method.getReturnType().getSimpleName())) {
//                String resultStr = JSON.toJSONString(keys);
//                Result result = JSON.parseObject(resultStr, new TypeReference<Result>() {});
//                operateLog.setOperateResult(result.getCode());
//            } else if (BOOLEAN_CLASS.equals(method.getReturnType().getSimpleName())) {
//                String resultStr = (String) keys;
//                operateLog.setOperateResult("true".equals(resultStr) ? 1 : 0);
//            } else {
//                // 返回不是Result，不知道成功失败
//                operateLog.setOperateResult(-1);
//            }
//
//            // 获取请求的方法名
//            String className = joinPoint.getTarget().getClass().getName();
//            String methodName = method.getName();
//            methodName = className + "." + methodName;
//            operateLog.setOperateMethod(methodName);
//
//            // 请求的参数
//            Map<String, String> rtnMap = convertMap(request.getParameterMap());
//            String params = JSON.toJSONString(rtnMap);
//            operateLog.setOperateParam(params);
//
//            operateLog.setReturnResult(JSON.toJSONString(keys));
//            operateLog.setOperateIp(IpUtil.getIpAddr(request));
//            operateLog.setOperateUrl(request.getRequestURI());
//            operateLog.setOperateUser(securityManager.getLoginUsername());
//            operateLog.setOperateTime(DateUtil.formatNow());
//            operateLogService.save(operateLog);
//        } catch (Exception e) {
//            log.error("aop pointcut of operateLogPointCut catch ex", e);
//        }
//    }
//
//    /**
//     * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
//     *
//     * @param joinPoint 切入点
//     * @param e         异常信息
//     */
//    @AfterThrowing(pointcut = "operateExpLogPointCut()", throwing = "e")
//    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
//        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
//        if (attributes == null) {
//            return;
//        }
//
//        try {
//            // 获取Request对象
//            HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
//
//            // 从切面织入点处通过反射机制获取织入点处的方法
//            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//            Method method = signature.getMethod();
//
//            // 设置操作信息
//            OperationExpLog operateExpLog = new OperationExpLog();
//            operateExpLog.setOperateId(UuidUtil.generateShortUuid());
//
//            // 获取请求的方法名
//            String className = joinPoint.getTarget().getClass().getName();
//            String methodName = method.getName();
//            methodName = className + "." + methodName;
//
//            // 请求的参数
//            Map<String, String> rtnMap = convertMap(request.getParameterMap());
//            String params = JSON.toJSONString(rtnMap);
//
//            operateExpLog.setOperateParam(params);
//            operateExpLog.setOperateMethod(methodName);
//            operateExpLog.setExpName(e.getClass().getName());
//            operateExpLog.setExpMessage(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace()));
//            operateExpLog.setExpTime(DateUtils.formatNow());
//            operateExpLog.setOperateIp(IpUtils.getIpAddr(request));
//            operateExpLog.setOperateUrl(request.getRequestURI());
//            operateExpLog.setOperateUser(securityManager.getLoginUsername());
//            operateExpLogService.save(operateExpLog);
//        } catch (Exception e2) {
//            log.error("aop pointcut of operateExpLogPointCut catch ex", e);
//        }
//    }
//
//    private String stackTraceToString(String expName, String message, StackTraceElement[] elements) {
//        StringBuilder buffer = new StringBuilder();
//        for (StackTraceElement stet : elements) {
//            buffer.append(stet).append("\n");
//        }
//        return expName + ":" + message + "\n\t" + buffer.toString();
//    }
//
//    private Map<String, String> convertMap(Map<String, String[]> parameterMap) {
//        Map<String, String> rtnMap = new HashMap<>(16);
//        for (String key : parameterMap.keySet()) {
//            rtnMap.put(key, parameterMap.get(key)[0]);
//        }
//        return rtnMap;
//    }
//}
