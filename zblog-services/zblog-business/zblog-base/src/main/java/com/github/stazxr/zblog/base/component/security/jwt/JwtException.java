package com.github.stazxr.zblog.base.component.security.jwt;

/**
 * Jwt exception
 *
 * @author SunTao
 * @since 2022-08-18
 */
public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
