package com.github.stazxr.zblog.bas.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Spring Security 扩展配置
 *
 * <p>该类用于配置系统的安全相关设置，例如登录和注销的 URL等。</p>
 *
 * @author SunTao
 * @since 2024-11-08
 */
@ConfigurationProperties(prefix = SecurityExtProperties.SECURITY_PREFIX)
public class SecurityExtProperties {
    static final String SECURITY_PREFIX = "zblog.base.security";

    /**
     * 登录 URL
     */
    private String loginUrl = "/api/login";

    /**
     * 登出 URL
     */
    private String logoutUrl = "/api/logout";

    /**
     * 用户密码有效天数，默认 90 天
     */
    private int passwordLimitedDay = 90;

    /**
     * 资源与角色关联关系的缓存时长, 单位毫秒, 默认 30 分钟
     */
    private long resourceRolesCacheMills = 30 * 60 * 1000L;

    /**
     * SSO 令牌缓存时长, 单位毫秒, 默认 3 天
     */
    private long ssoTokenCacheMills = 24 * 3600 * 1000L;

    private boolean enableAdditionalChecks = false;

    protected boolean hideUserNotFoundExceptions = true;

    /**
     * 获取登录 URL。
     *
     * @return 登录 URL 字符串
     */
    public String getLoginUrl() {
        return loginUrl;
    }

    /**
     * 设置登录 URL。
     *
     * @param loginUrl 登录 URL 字符串
     */
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    /**
     * 获取注销 URL。
     *
     * @return 注销 URL 字符串
     */
    public String getLogoutUrl() {
        return logoutUrl;
    }

    /**
     * 设置注销 URL。
     *
     * @param logoutUrl 注销 URL 字符串
     */
    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    /**
     * 获取密码有效天数。
     *
     * @return 密码有效天数
     */
    public int getPasswordLimitedDay() {
        return passwordLimitedDay;
    }

    /**
     * 设置密码有效天数。
     *
     * @param passwordLimitedDay 密码有效天数
     */
    public void setPasswordLimitedDay(int passwordLimitedDay) {
        if (passwordLimitedDay < 0) {
            throw new IllegalArgumentException(SECURITY_PREFIX + ".password-limited-day must not be negative.");
        }
        this.passwordLimitedDay = passwordLimitedDay;
    }

    public long getResourceRolesCacheMills() {
        return resourceRolesCacheMills;
    }

    public void setResourceRolesCacheMills(long resourceRolesCacheMills) {
        this.resourceRolesCacheMills = resourceRolesCacheMills;
    }

    public long getSsoTokenCacheMills() {
        return ssoTokenCacheMills;
    }

    public void setSsoTokenCacheMills(long ssoTokenCacheMills) {
        this.ssoTokenCacheMills = ssoTokenCacheMills;
    }

    public boolean isEnableAdditionalChecks() {
        return enableAdditionalChecks;
    }

    public void setEnableAdditionalChecks(boolean enableAdditionalChecks) {
        this.enableAdditionalChecks = enableAdditionalChecks;
    }

    public boolean isHideUserNotFoundExceptions() {
        return hideUserNotFoundExceptions;
    }

    public void setHideUserNotFoundExceptions(boolean hideUserNotFoundExceptions) {
        this.hideUserNotFoundExceptions = hideUserNotFoundExceptions;
    }
}
