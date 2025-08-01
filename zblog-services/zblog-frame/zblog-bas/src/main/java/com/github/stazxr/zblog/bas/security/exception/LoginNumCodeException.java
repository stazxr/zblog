package com.github.stazxr.zblog.bas.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义登录次数限制异常，用于限制用户登录失败次数过多的场景。
 *
 * <p>
 * 此异常继承自 {@link AuthenticationException}，是 Spring Security 认证异常体系的一部分。
 * 可用于在认证流程中抛出，触发自定义异常处理逻辑。
 * </p>
 *
 * @author SunTao
 * @since 2024-11-18
 */
public class LoginNumCodeException extends AuthenticationException {
    private static final long serialVersionUID = 2399675277250587214L;

    /**
     * 构造方法，接受自定义异常信息。
     *
     * @param msg 异常消息，用于描述登录次数限制的具体原因
     */
    public LoginNumCodeException(String msg) {
        super(msg);
    }

    /**
     * 构造方法，接受自定义异常信息和异常原因。
     *
     * @param msg   异常消息，用于描述登录次数限制的具体原因
     * @param cause 异常原因，可用于追踪根因
     */
    public LoginNumCodeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
