package com.github.stazxr.zblog.bas.security.jwt.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Jwt Cookie 配置信息
 *
 * @author SunTao
 * @since 2026-05-22
 */
@ConfigurationProperties(prefix= JwtCookieProperties.JWT_COOKIE_PREFIX)
public class JwtCookieProperties {
    static final String JWT_COOKIE_PREFIX = "zblog.base.security.jwt.cookie";

    /**
     * Cookie域名
     */
    private String domain = "localhost";

    /**
     * HTTPS专用
     */
    private boolean secure = false;

    /**
     * 禁止JS读取
     */
    private boolean httpOnly = true;

    /**
     * SameSite策略
     */
    private String sameSite = "Lax";

    /**
     * Cookie路径
     */
    private String path = "/";

    /**
     * 刷新令牌的Cookie路径
     */
    private String refreshPath = "/";

    /**
     * 有效时间(秒)
     */
    private long maxAge = 1800L;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public String getSameSite() {
        return sameSite;
    }

    public void setSameSite(String sameSite) {
        this.sameSite = sameSite;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRefreshPath() {
        return refreshPath;
    }

    public void setRefreshPath(String refreshPath) {
        this.refreshPath = refreshPath;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }
}
