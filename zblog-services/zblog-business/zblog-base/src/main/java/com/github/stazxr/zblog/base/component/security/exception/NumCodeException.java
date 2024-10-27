package com.github.stazxr.zblog.base.component.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码校验异常
 *
 * @author SunTao
 * @since 2020-11-15
 */
public class NumCodeException extends AuthenticationException {
    /**
     * serialId
     */
    private static final long serialVersionUID = 2399675277250587214L;

    public NumCodeException(String msg) {
        super(msg);
    }
}
