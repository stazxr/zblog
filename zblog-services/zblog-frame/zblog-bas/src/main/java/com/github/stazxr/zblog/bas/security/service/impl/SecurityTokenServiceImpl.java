package com.github.stazxr.zblog.bas.security.service.impl;

import com.github.stazxr.zblog.bas.security.core.SecurityToken;
import com.github.stazxr.zblog.bas.security.service.SecurityTokenService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存存储的安全令牌服务实现类。
 *
 * @author SunTao
 * @since 2024-11-17
 */
public class SecurityTokenServiceImpl implements SecurityTokenService {
    /**
     * 内存中存储的用户令牌，线程安全。
     * Key 为用户 ID，Value 为用户令牌 {@link SecurityToken}。
     */
    private final Map<String, SecurityToken> tokenStore = new ConcurrentHashMap<>();

    /**
     * 保存安全令牌。
     *
     * @param securityToken 安全令牌
     */
    @Override
    public void saveToken(SecurityToken securityToken) {
        if (securityToken != null) {
            tokenStore.put(securityToken.getUserId(), securityToken);
        }
    }

    /**
     * 获取安全令牌。
     *
     * @param userId 用户标识
     * @return 安全令牌，如果未找到返回 {@code null}
     */
    @Override
    public SecurityToken getToken(String userId) {
        return tokenStore.get(userId);
    }

    /**
     * 将指定用户的令牌标记为过期。
     *
     * @param userId 用户标识
     */
    @Override
    public void expireToken(String userId) {
        tokenStore.remove(userId);
    }
}
