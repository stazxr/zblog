package com.github.stazxr.zblog.core.util;

import cn.hutool.json.JSONObject;
import com.github.stazxr.zblog.core.base.BaseConst;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }

        throw new IllegalStateException("请登录");
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
        UserDetails userDetails = getLoginUser();
        return userDetails.getUsername();
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
