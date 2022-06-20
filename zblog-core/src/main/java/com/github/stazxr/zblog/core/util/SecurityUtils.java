package com.github.stazxr.zblog.core.util;

import cn.hutool.json.JSONObject;
import com.github.stazxr.zblog.core.base.BaseConst;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * spring security utils
 *
 * @author SunTao
 * @since 2021-01-29
 */
public class SecurityUtils {
    /**
     * 获取当前登录的用户信息
     *
     * @return UserDetails
     */
    public static UserDetails getLoginUser() {
        UserDetailsService userDetailsService = SpringContextUtils.getBean(UserDetailsService.class);
        return userDetailsService.loadUserByUsername(getLoginUsername());
    }

    /**
     * 获取当前登录用户编码
     *
     * @return 当前登录用户编码
     */
    public static Long getLoginId() {
        UserDetails userDetails = getLoginUser();
        return new JSONObject(userDetails).get("id", Long.class);
    }

    /**
     * 获取当前登录用户名称
     *
     * @return 当前登录用户名称
     */
    public static String getLoginUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // return username
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }

        throw new IllegalStateException("未登录");
    }

    /**
     * 获取当前登录用户名称，如果未登录，返回系统用户
     *
     * @return 当前登录用户名称
     */
    public static String getLoginUsernameNoEor() {
        try {
            return SecurityUtils.getLoginUsername();
        } catch (IllegalStateException e) {
            return BaseConst.USER_SYSTEM;
        }
    }
}
