package com.github.stazxr.zblog.bas.security.jwt.storage;

/**
 * 该接口定义了存储和管理 JSON Web Token (JWT) 的操作
 *
 * @since 2022-01-19
 * @author SunTao
 */
public interface JwtTokenStorage {
    /**
     * 存储访问令牌（accessToken）。
     * <p>
     * 该方法将存储用户的访问令牌，并设置其有效时间。
     * </p>
     *
     * @param accessToken 需要存储的访问令牌
     * @param uid 用户的唯一标识符（通常是用户 ID）
     * @param duration 令牌的有效时间，单位为秒
     * @return 存储后的访问令牌
     */
    String putAccessToken(String accessToken, String uid, int duration);

    /**
     * 存储刷新令牌（refreshToken）。
     * <p>
     * 该方法将存储用户的刷新令牌，并设置其有效时间。
     * </p>
     *
     * @param refreshToken 需要存储的刷新令牌
     * @param uid 用户的唯一标识符（通常是用户 ID）
     * @param duration 令牌的有效时间，单位为秒
     * @return 存储后的刷新令牌
     */
    String putRefreshToken(String refreshToken, String uid, int duration);

    /**
     * 获取访问令牌（accessToken）。
     * <p>
     * 该方法根据用户 ID 获取对应的访问令牌。如果令牌不存在或已过期，则返回 null。
     * </p>
     *
     * @param uid 用户的唯一标识符（通常是用户 ID）
     * @return 用户对应的访问令牌，若不存在则返回 null
     */
    String getAccessToken(String uid);

    /**
     * 获取刷新令牌（refreshToken）。
     * <p>
     * 该方法根据用户 ID 获取对应的刷新令牌。如果令牌不存在或已过期，则返回 null。
     * </p>
     *
     * @param uid 用户的唯一标识符（通常是用户 ID）
     * @return 用户对应的刷新令牌，若不存在则返回 null
     */
    String getRefreshToken(String uid);

    /**
     * 使指定用户的令牌过期。
     * <p>
     * 该方法根据用户 ID 使用户的访问令牌和刷新令牌立即过期。
     * </p>
     *
     * @param uid 用户的唯一标识符（通常是用户 ID）
     */
    void expire(String uid);
}
