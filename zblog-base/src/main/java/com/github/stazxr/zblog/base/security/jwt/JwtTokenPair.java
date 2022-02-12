package com.github.stazxr.zblog.base.security.jwt;

import lombok.Data;

import java.io.Serializable;

/**
 * token info
 *
 * @author SunTao
 * @since 2022-01-19
 */
@Data
@Deprecated
public class JwtTokenPair implements Serializable {
    /**
     * serial Id
     */
    private static final long serialVersionUID = -8518897818107784049L;

    /**
     * token
     */
    private String accessToken;

    /**
     * refresh token
     */
    private String refreshToken;
}
