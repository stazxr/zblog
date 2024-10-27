package com.github.stazxr.zblog.base.component.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 密钥校验异常
 *
 * @author SunTao
 * @since 2022-02-04
 */
public class PreJwtCheckAuthenticationException extends AuthenticationException {
    /**
     * serialId
     */
    private static final long serialVersionUID = 2399675277250587214L;

    public PreJwtCheckAuthenticationException(String msg) {
        super(msg);
    }

    public PreJwtCheckAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PreJwtCheckAuthenticationException(AuthenticationException e) {
        super(e.getMessage());
    }
}
