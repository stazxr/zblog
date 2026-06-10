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
     * 用户名
     */
    private String username;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 访问令牌ID
     */
    private String accessTokenId;

    /**
     * 刷新令牌ID
     */
    private String refreshTokenId;

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
    private boolean kickOut = false;

    public TokenPayload() {
    }

    public TokenPayload(String userId, String username, Date loginTime, String accessTokenId, String refreshTokenId) {
        this.userId = userId;
        this.username = username;
        this.loginTime = loginTime;
        this.accessTokenId = accessTokenId;
        this.refreshTokenId = refreshTokenId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public String getAccessTokenId() {
        return accessTokenId;
    }

    public String getRefreshTokenId() {
        return refreshTokenId;
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
        return kickOut;
    }

    public void setKickOut(boolean kickOut) {
        this.kickOut = kickOut;
    }
}
