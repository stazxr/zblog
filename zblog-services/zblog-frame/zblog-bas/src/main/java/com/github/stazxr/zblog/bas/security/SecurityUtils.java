package com.github.stazxr.zblog.bas.security;

import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

/**
 * 安全工具类，用于获取当前登录的用户信息
 * <p>
 * 该类提供了一些方法来获取当前认证用户的详情，包括用户名、ID等。如果用户未认证或未登录，
 * 则可以提供默认值或者返回Optional进行处理。
 * </p>
 *
 * @author SunTao
 * @since 2024-11-26
 */
public class SecurityUtils {
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
    @SuppressWarnings("all")
    public static <T extends SecurityUser> T getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof SecurityUser) {
                return (T) principal;
            }
        }

        throw new IllegalStateException("请登录");
    }

    /**
     * 获取当前登录的用户信息，返回 {@link Optional} 类型
     * <p>
     * 如果用户未登录或认证信息无效，返回空的 Optional。
     * </p>
     *
     * @param <T> 用户类型，必须是实现了 {@link SecurityUser} 的类型
     * @return 当前登录用户的 {@link Optional} 对象
     */
    @SuppressWarnings("all")
    public static <T extends SecurityUser> Optional<T> getLoginUserOptional() {
        try {
            return Optional.of((T) getLoginUser());
        } catch (IllegalStateException e) {
            return Optional.empty();
        }
    }

    /**
     * 获取当前登录用户的用户名
     * <p>
     * 如果用户未登录，则返回 null。
     * </p>
     *
     * @return 当前登录用户的用户名，或 null 如果未登录
     */
    public static String getLoginUsername() {
        return getLoginUserOptional().map(SecurityUser::getUsername).orElse(null);
    }

    /**
     * 获取当前登录用户的用户名
     * <p>
     * 如果用户未登录，则返回系统用户。
     * </p>
     *
     * @return 当前登录用户的用户名，或系统用户，如果未登录
     */
    public static String getLoginUsernameWithoutNull() {
        return getLoginUserOptional().map(SecurityUser::getUsername).orElse("anonymous");
    }

    /**
     * 获取当前登录用户的用户ID
     * <p>
     * 如果用户未登录，则返回 null。
     * </p>
     *
     * @return 当前登录用户的ID，或 null 如果未登录
     */
    public static Long getLoginId() {
        return getLoginUserOptional().map(SecurityUser::getId).orElse(null);
    }

    /**
     * 获取当前登录用户的用户ID
     * <p>
     * 如果用户未登录，则返回系统用户ID。
     * </p>
     *
     * @return 当前登录用户的ID，或系统用户ID，如果未登录
     */
    public static Long getLoginIdWithoutNull() {
        return getLoginUserOptional().map(SecurityUser::getId).orElse(1L);
    }
}
