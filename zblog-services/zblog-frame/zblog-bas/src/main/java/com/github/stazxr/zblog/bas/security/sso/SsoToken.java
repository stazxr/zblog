package com.github.stazxr.zblog.bas.security.sso;

import lombok.Getter;
import lombok.Setter;

/**
 * 表示单点登录 (SSO) 相关的用户令牌信息。
 *
 * @author SunTao
 * @since 2024-11-16
 */
@Getter
@Setter
public class SsoToken {
    /**
     * 用户标识
     */
    private String userId;

    /**
     * 单点登录令牌
     *
     * <p>用于在整个单点登录过程中，维持用户的登录状态。</p>
     */
    private String ssoToken;

    /**
     * 用户的 IP 地址
     */
    private String userIp;

    public SsoToken() {
    }

    /**
     * 构造函数，初始化单点登录用户令牌信息。
     *
     * @param userId 用户标识
     * @param ssoToken 单点登录令牌
     * @param userIp 用户 IP 地址
     * @throws IllegalArgumentException 如果任何字段为空或无效，将抛出异常。
     */
    public SsoToken(String userId, String ssoToken, String userIp) {
        this.userId = userId;
        this.ssoToken = ssoToken;
        this.userIp = userIp;
    }
}
