package com.github.stazxr.zblog.bas.security;

import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Security 工具类
 *
 * @author SunTao
 * @since 2024-11-26
 */
public final class SecurityUtils {
    private SecurityUtils() {
    }

    /**
     * 获取 Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 判断是否已登录
     */
    public static boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }

    /**
     * 获取当前登录的用户信息
     * <p>
     * 该方法会从Spring Security的上下文中获取当前认证的用户信息（UserDetails）。如果用户未登录，
     * 会抛出IllegalStateException异常。
     * </p>
     *
     * @param <T> 用户类型，必须是实现了 {@link SecurityUser} 的类型
     * @return 当前登录用户的UserDetails对象
     * @throws IllegalStateException 如果用户未登录
     */
    @SuppressWarnings("unchecked")
    public static <T extends SecurityUser> T getLoginUser() {
        // TODO 待优化，获取失败直接抛出401
        if (!isAuthenticated()) {
            throw new IllegalStateException("用户未登录");
        }
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof SecurityUser)) {
            throw new IllegalStateException("用户信息异常");
        }
        return (T) principal;
    }

    /**
     * 获取当前登录用户的用户ID
     *
     * @return 当前登录用户的ID
     */
    public static Long getLoginId() {
        return getLoginUser().getId();
    }

    /**
     * 获取当前登录用户的用户名
     *
     * @return 当前登录用户的用户名
     */
    public static String getUserName() {
        return getLoginUser().getUsername();
    }
}
