package com.github.stazxr.zblog.bas.security.service.impl;

import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.context.util.SpringContextUtil;
import com.github.stazxr.zblog.bas.security.cache.SecurityUserCache;
import com.github.stazxr.zblog.bas.security.jwt.JwtConstants;
import com.github.stazxr.zblog.bas.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.bas.security.service.SecurityTokenService;
import com.github.stazxr.zblog.bas.security.service.SecurityLogoutService;
import com.github.stazxr.zblog.bas.security.sso.SsoTokenCache;
import com.github.stazxr.zblog.util.net.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Objects;

/**
 * 用于管理系统中的安全注销服务接口的默认实现。
 *
 * 支持在清理操作前后插入自定义逻辑，通过继承该类并重写 `beforeClear` 和 `afterClean` 方法实现。
 *
 * @author SunTao
 * @since 2024-11-16
 */
@Slf4j
public class SecurityLogoutServiceImpl implements SecurityLogoutService {
    /**
     * 安全令牌服务
     */
    private volatile SecurityTokenService securityTokenService;

    /**
     * JWT Token 存储服务
     */
    private volatile JwtTokenStorage jwtTokenStorage;

    /**
     * 清理用户的登录信息。
     *
     * @param userId 用户标识
     */
    public void clearUserLoginInfo(String userId) {
        initBeans();

        // 在清理操作之前执行自定义逻辑
        beforeClear(userId);

        boolean success = false;
        try {
            // 更新 JWT Token 状态为已过期
            jwtTokenStorage.expire(userId);

            // 更新安全令牌状态为已过期
            securityTokenService.expireToken(userId);

            // 移除缓存中的 SSO 令牌
            removeSsoToken();

            // 移除缓存中相关信息
            String preTknCacheKey = String.format(Locale.ROOT, JwtConstants.PTK_TOKEN_CACHE_KEY, userId);
            GlobalCache.remove(preTknCacheKey);

            // 清理用户的缓存信息
            SecurityUserCache.remove(userId);
            success = true;
        } catch (Exception e) {
            throw new RuntimeException("清理用户登录信息失败: " + userId, e);
        } finally {
            // 在清理操作之后执行自定义逻辑
            afterClean(userId, success);
        }
    }

    private void removeSsoToken() {
        try {
            SsoTokenCache.remove(IpUtils.getIp(getRequest()));
        } catch (Exception e) {
            log.error("Remove Sso Token Failed: {}", e.getMessage());
        }
    }

    /**
     * 在清理用户登录信息之前执行的操作。
     *
     * @param userId 用户唯一标识符
     */
    protected void beforeClear(String userId) {
        // 默认实现为空，子类可重写
    }

    /**
     * 在清理用户登录信息之后执行的操作。
     *
     * @param userId 用户唯一标识符
     * @param success 执行结果
     */
    protected void afterClean(String userId, boolean success) {
        // 默认实现为空，子类可重写
    }

    /**
     * 初始化所需的 Bean，确保服务已加载。
     * <p>
     * 如果必须的 Bean 未能加载，将抛出 {@link RuntimeException}。
     * </p>
     */
    private void initBeans() {
        if (jwtTokenStorage == null || securityTokenService == null) {
            synchronized (SecurityLogoutServiceImpl.class) {
                if (jwtTokenStorage == null) {
                    jwtTokenStorage = SpringContextUtil.getBean(JwtTokenStorage.class);
                    if (jwtTokenStorage == null) {
                        throw new RuntimeException("未找到 JwtTokenStorage Bean");
                    }
                }
                if (securityTokenService == null) {
                    securityTokenService = SpringContextUtil.getBean(SecurityTokenService.class);
                    if (securityTokenService == null) {
                        throw new RuntimeException("未找到 SecurityTokenService Bean");
                    }
                }
            }
        }
    }


    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
