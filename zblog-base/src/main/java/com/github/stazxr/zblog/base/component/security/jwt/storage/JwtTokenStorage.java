package com.github.stazxr.zblog.base.component.security.jwt.storage;

/**
 * JwtTokenStorage
 *
 * @author SunTao
 * @since 2022-01-19
 */
public interface JwtTokenStorage {
    /**
     * Put accessToken
     *
     * @param accessToken Token
     * @param uid  userId
     * @param duration 有效时间，单位秒
     * @return accessToken
     */
    String putAccessToken(String accessToken, Long uid, int duration);

    /**
     * Put refreshToken
     *
     * @param refreshToken Token
     * @param uid  userId
     * @param duration 有效时间，单位秒
     * @return refreshToken
     */
    String putRefreshToken(String refreshToken, Long uid, int duration);

    /**
     * Expire.
     *
     * @param uid userId
     */
    void expire(Long uid);

    /**
     * Get accessToken
     *
     * @param uid userId
     * @return accessToken
     */
    String getAccessToken(Long uid);

    /**
     * Get refreshToken
     *
     * @param uid userId
     * @return refreshToken
     */
    String getRefreshToken(Long uid);
}
