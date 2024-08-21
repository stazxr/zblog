package com.github.stazxr.zblog.core.aop;

import com.alibaba.fastjson.JSON;
import com.github.stazxr.zblog.cache.util.GlobalCacheHelper;
import com.github.stazxr.zblog.core.annotation.FormResubmitCheck;
import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.encryption.util.Md5Utils;
import com.github.stazxr.zblog.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;

/**
 * 表单重复提交校验切面，依赖 Redis
 *
 * @author SunTao
 * @since 2023-12-11
 */
@Slf4j
@Aspect
@Component
public class FormResubmitCheckAspect {
    @Around("@annotation(formResubmitCheck)")
    public Object logAround(ProceedingJoinPoint joinPoint, FormResubmitCheck formResubmitCheck) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 获取方法签名
        Object[] args = joinPoint.getArgs();
        String argString = getParameter(signature.getMethod(), args);
        String methodString = signature.toLongString();
        String methodSignature = methodString.concat(argString);
        methodSignature = formResubmitCheck.global() ? methodSignature : methodSignature.concat(String.valueOf(SecurityUtils.getLoginId()));
        String messageDigest = Md5Utils.getMessageDigest(methodSignature.getBytes(StandardCharsets.UTF_8));

        // 规则校验
        String rk = "frc:" + messageDigest;
        Predicate<String> p = k -> GlobalCacheHelper.get(k) != null;
        Assert.isTrue(p.test(rk), formResubmitCheck.value());

        try {
            GlobalCacheHelper.put(rk, "formResubmitCheck", formResubmitCheck.timeout());
            return joinPoint.proceed();
        } finally {
            GlobalCacheHelper.remove(messageDigest);
        }
    }

    private String getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String paramTypeName = parameter.getType().getSimpleName();
            final String[] ignoreParams = {"HttpServletResponse", "HttpServletRequest", "MultipartFile", "MultipartFile[]"};
            if (Arrays.asList(ignoreParams).contains(paramTypeName)) {
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
                String key = com.github.stazxr.zblog.util.StringUtils.isEmpty(requestParam.value()) ? parameter.getName() : requestParam.value();
                map.put(key, args[i]);
                argList.add(map);
            } else {
                argList.add(args[i]);
            }
        }

        return argList.isEmpty() ? "" : argList.size() == 1 ? JSON.toJSONString(argList.get(0)) : JSON.toJSONString(argList);
    }
}
