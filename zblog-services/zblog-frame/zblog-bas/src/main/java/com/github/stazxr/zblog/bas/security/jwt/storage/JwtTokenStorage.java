package com.github.stazxr.zblog.bas.security.jwt.storage;

/**
 * 该接口定义了存储和管理 JSON Web Token (JWT) 的操作
 *
 * @author SunTao
 * @since 2022-01-19
 */
public interface JwtTokenStorage {
    /**
     * 存储令牌。
     *
     * @param uid      用户的唯一标识符（通常是用户 ID）
     * @param token    令牌信息
     * @param duration 令牌的有效时间，单位为秒
     */
    void put(String uid, TokenPayload token, int duration);

    /**
     * 获取令牌。
     *
     * @param uid 用户的唯一标识符（通常是用户 ID）
     * @return 令牌信息，若不存在则返回 null
     */
    TokenPayload get(String uid);

    /**
     * 移除令牌。
     *
     * @param uid 用户的唯一标识符（通常是用户 ID）
     */
    void remove(String uid);
}
