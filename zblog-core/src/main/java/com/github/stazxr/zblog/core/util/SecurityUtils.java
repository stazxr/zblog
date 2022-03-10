package com.github.stazxr.zblog.core.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * spring security utils
 *
 * @author SunTao
 * @since 2021-01-29
 */
public class SecurityUtils {
    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No Login");
        }

        return authentication.getName();
    }
}
