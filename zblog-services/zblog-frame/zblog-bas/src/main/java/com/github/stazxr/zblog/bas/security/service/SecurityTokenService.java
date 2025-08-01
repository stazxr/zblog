package com.github.stazxr.zblog.bas.security.service;

import com.github.stazxr.zblog.bas.security.core.SecurityToken;

/**
 * 用于管理系统中的安全令牌服务接口。
 *
 * @author SunTao
 * @since 2024-11-17
 */
public interface SecurityTokenService {
    /**
     * 保存安全令牌。
     *
     * @param securityToken 安全令牌
     */
    void saveToken(SecurityToken securityToken);

    /**
     * 获取安全令牌。
     *
     * @param userId 用户标识
     * @return 安全令牌，如果未找到返回 {@code null}
     */
    SecurityToken getToken(String userId);

    /**
     * 将指定用户的令牌标记为过期。
     *
     * @param userId 用户标识
     */
    void expireToken(String userId);
}

