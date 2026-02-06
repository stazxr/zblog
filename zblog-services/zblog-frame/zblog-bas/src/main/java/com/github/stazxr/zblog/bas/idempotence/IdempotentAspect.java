package com.github.stazxr.zblog.bas.idempotence;

import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.exception.SystemException;
import com.github.stazxr.zblog.bas.exception.code.CommonErrorCode;
import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import com.github.stazxr.zblog.bas.exception.code.ErrorCodeUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 幂等切面
 *
 * @author SunTao
 * @since 2026-02-02
 */
@Aspect
@Component
@RequiredArgsConstructor
public class IdempotentAspect {
    private static final String KEY_PREFIX = "idempotent:";

    private final SpelExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(idempotent)")
    public Object around(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        String key = buildKey(joinPoint, idempotent.key());
        String cacheKey = KEY_PREFIX + key;

        Boolean success = GlobalCache.setIfAbsent(cacheKey, 1, idempotent.expire(), idempotent.timeUnit());
        if (!Boolean.TRUE.equals(success)) {
            // 已执行或处理中
            String message = idempotent.message();
            ErrorCode errorCode = CommonErrorCode.SBASEA001;
            if (message != null) {
                errorCode = ErrorCodeUtils.of(message);
            }
            throw new IdempotentException(errorCode);
        }

        try {
            // 正常执行目标方法
            return joinPoint.proceed();
        } catch (Throwable ex) {
            // 如果执行失败，释放幂等锁
            GlobalCache.remove(cacheKey);
            throw ex;
        }
    }

    /**
     * 构建幂等 key
     */
    private String buildKey(ProceedingJoinPoint joinPoint, String spelKey) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            EvaluationContext context = new StandardEvaluationContext();
            Method method = signature.getMethod();
            context.setVariable("_c", method.getClass().getName());
            context.setVariable("_m", method.getName());
            String[] paramNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }

            String value = parser.parseExpression(spelKey).getValue(context, String.class);
            if (value == null || "".equals(value.trim())) {
                throw new IllegalArgumentException("Idempotent key cannot be empty");
            }
            return value;
        } catch (Exception e) {
            throw new SystemException("Idempotent key parse failed", e);
        }
    }
}
