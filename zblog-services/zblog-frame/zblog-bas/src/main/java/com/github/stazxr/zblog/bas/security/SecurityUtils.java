package com.github.stazxr.zblog.bas.security;

import com.github.stazxr.zblog.bas.encryption.util.Md5Utils;
import com.github.stazxr.zblog.bas.security.core.SecurityRole;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Security 工具类
 *
 * 用于获取当前登录用户信息
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

    /**
     * 反射获取字段
     *
     * 兼容业务模块扩展字段
     */
    public static Object getField(String fieldName) {
        SecurityUser user = getLoginUser();
        Class<?> clazz = user.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(user);
            } catch (Exception ignored) {
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * 反射获取字段（类型安全）
     */
    public static <T> T getField(String fieldName, Class<T> clazz) {
        Object value = getField(fieldName);
        if (value == null) {
            return null;
        }
        return clazz.cast(value);
    }

    /**
     * 判断是否拥有角色
     */
    public static boolean hasRole(String roleCode) {
        return getLoginUser().getAuthorities()
                .stream()
                .anyMatch(role -> roleCode.equals(role.getRoleCode()));
    }

    /**
     * 判断是否拥有任一角色
     */
    public static boolean hasAnyRole(String... roleCodes) {
        if (roleCodes == null || roleCodes.length == 0) {
            return false;
        }
        List<? extends SecurityRole> authorities = getLoginUser().getAuthorities();
        return authorities.stream()
                .anyMatch(role -> Arrays.asList(roleCodes).contains(role.getRoleCode()));
    }

    /**
     * 判断是否拥有权限
     */
    public static boolean hasPermission(String permission) {
        String md5Perm;
        try {
            md5Perm = Md5Utils.md5(permission);
        } catch (Exception e) {
            return false;
        }
        return getLoginUser().getPerms().stream().anyMatch(md5Perm::equals);
    }
}
