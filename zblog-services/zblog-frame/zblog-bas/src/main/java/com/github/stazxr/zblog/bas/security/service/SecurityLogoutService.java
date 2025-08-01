package com.github.stazxr.zblog.bas.security.service;

/**
 * 用于管理系统中的安全注销服务接口。
 *
 * @author SunTao
 * @since 2024-11-16
 */
public interface SecurityLogoutService {
    /**
     * 清理用户的登录信息。
     *
     * @param userId 用户标识
     */
    void clearUserLoginInfo(String userId);
}
