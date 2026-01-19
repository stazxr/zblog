package com.github.stazxr.zblog.bas.security.service;

import com.github.stazxr.zblog.bas.security.core.SecurityUser;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于管理系统中的安全用户服务接口。
 *
 * @see org.springframework.security.core.userdetails.UserDetails
 * @see com.github.stazxr.zblog.bas.security.core.SecurityUser
 *
 * @author SunTao
 * @since 2024-11-10
 */
public interface SecurityUserService {
    /**
     * 根据 {@code userId} 查询用户信息。
     *
     * @param userId 用户 ID
     * @return 用户的 {@link SecurityUser} 实例
     */
    SecurityUser findUserById(String userId);

    /**
     * 根据用户名 {@code username} 登录并返回登录用户信息。
     *
     * @param username 用户名
     * @return 用户的 {@link SecurityUser} 实例
     */
    SecurityUser loginWithUsername(String username);

    /**
     * 更新用户的登录信息。
     *
     * @param username 用户名
     * @param userIp   用户的登录ip
     * @param type     用户登录类型
     * @param request  用户的请求信息
     */
    void updateUserLoginInfo(String username, String userIp, int type, HttpServletRequest request);
}

