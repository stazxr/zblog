package com.github.stazxr.zblog.bas.security.jwt.storage;

import java.io.Serializable;
import java.util.Date;

/**
 * Token信息载体
 *
 * @author SunTao
 * @since 2026-02-22
 */
public class TokenPayload implements Serializable {
    private static final long serialVersionUID = 1106300371468064794L;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 访问令牌ID
     */
    private String accessTokenId;

    /**
     * 访问时间
     */
    private Date lastAccessTime;

    /**
     * 续签时间
     */
    private Date renewTime;

    /**
     * 是否被踢出
     */
    private boolean isKickOut = false;

    public TokenPayload() {
    }

    public TokenPayload(String userId, String refreshToken, Date loginTime) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.loginTime = loginTime;
    }

    public TokenPayload(String userId, String refreshToken, Date loginTime, String accessTokenId) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.loginTime = loginTime;
        this.accessTokenId = accessTokenId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public String getAccessTokenId() {
        return accessTokenId;
    }

    public void setAccessTokenId(String accessTokenId) {
        this.accessTokenId = accessTokenId;
    }

    public Date getRenewTime() {
        return renewTime;
    }

    public void setRenewTime(Date renewTime) {
        this.renewTime = renewTime;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public boolean isKickOut() {
        return isKickOut;
    }

    public void kickOut() {
        isKickOut = true;
    }
}
