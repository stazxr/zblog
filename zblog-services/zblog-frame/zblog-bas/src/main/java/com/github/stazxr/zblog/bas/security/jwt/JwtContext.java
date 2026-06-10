package com.github.stazxr.zblog.bas.security.jwt;

/**
 * Jwt 上下文
 *
 * @author SunTao
 * @since 2026-02-11
 */
public class JwtContext {
    /** 用户ID */
    private String userId;
    /** 用户名 */
    private String username;
    /** 登录IP */
    private String loginIp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }
}
