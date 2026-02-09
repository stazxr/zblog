package com.github.stazxr.zblog.bas.ratelimit.aspect;

import com.github.stazxr.zblog.bas.context.Context;
import com.github.stazxr.zblog.bas.exception.code.CommonErrorCode;
import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import com.github.stazxr.zblog.bas.exception.code.ErrorCodeUtils;
import com.github.stazxr.zblog.bas.ratelimit.RateLimitException;
import com.github.stazxr.zblog.bas.ratelimit.annotation.RateLimit;
import com.github.stazxr.zblog.bas.ratelimit.core.RateLimitKeyBuilder;
import com.github.stazxr.zblog.bas.ratelimit.core.RateLimitRule;
import com.github.stazxr.zblog.bas.ratelimit.core.RateLimitService;
import com.github.stazxr.zblog.util.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 限流切面
 *
 * @author SunTao
 * @since 2025-02-09
 */
@Aspect
@Component
public class RateLimitAspect {
    private static final Logger log = LoggerFactory.getLogger(RateLimitAspect.class);

    private final RateLimitService rateLimitService;

    public RateLimitAspect(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint pjp, RateLimit rateLimit) throws Throwable {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attr != null) {
                // 获取 Request
                HttpServletRequest request = attr.getRequest();
                // 获取规则（可从 Redis 动态获取）
                RateLimitRule rule = buildRule(rateLimit);
                // 获取用户ID
                String userId = getUserId();
                // 构建多维 key
                List<String> keys = RateLimitKeyBuilder.buildKeys(request, rule, userId);
                // 任一维度超限即拒绝
                for (String key : keys) {
                    boolean pass = rateLimitService.tryAcquire(key, rule.getTime(), rule.getCount());
                    if (!pass) {
                        if (StringUtils.isNotBlank(rateLimit.message())) {
                            ErrorCode customCode = ErrorCodeUtils.of(CommonErrorCode.SBASEA001.getCode(), rateLimit.message());
                            throw new RateLimitException(customCode);
                        } else {
                            throw new RateLimitException(CommonErrorCode.SBASEA001);
                        }
                    }
                }
            }
        } catch (RateLimitException e) {
            throw e;
        } catch (Exception e) {
            log.error("RateLimitAspect error", e);
        }

        // call api
        return pjp.proceed();
    }

    /**
     * 根据注解生成规则
     */
    private RateLimitRule buildRule(RateLimit rateLimit) {
        RateLimitRule rule = new RateLimitRule();
        rule.setTime(rateLimit.time());
        rule.setCount(rateLimit.count());
        rule.setEnableIp(rateLimit.enableIp());
        rule.setEnableUser(rateLimit.enableUser());
        rule.setEnableApi(rateLimit.enableApi());
        return rule;
    }

    private String getUserId() {
        return Context.getLoginId();
    }
}
