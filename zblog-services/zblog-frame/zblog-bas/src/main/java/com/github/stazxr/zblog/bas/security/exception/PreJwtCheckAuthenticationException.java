package com.github.stazxr.zblog.bas.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义异常：JWT 认证异常。
 *
 * <p>
 * 此异常主要用于在 JWT 检查之前的认证阶段出现问题时抛出，例如请求中缺少有效凭据或凭据格式错误。
 * 继承自 {@link AuthenticationException}，可与 Spring Security 认证流程无缝集成。
 * </p>
 *
 * @author SunTao
 * @since 2024-11-18
 */
public class PreJwtCheckAuthenticationException extends AuthenticationException {
    private static final long serialVersionUID = 2399675277250587214L;

    /**
     * 构造方法，接受自定义异常消息。
     *
     * @param msg 异常消息，用于描述认证失败的原因。
     */
    public PreJwtCheckAuthenticationException(String msg) {
        super(msg);
    }

    /**
     * 构造方法，接受自定义异常消息和异常原因。
     *
     * @param msg   异常消息，用于描述认证失败的原因。
     * @param cause 异常原因，用于追踪问题的根本原因。
     */
    public PreJwtCheckAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * 构造方法，从已有的 {@link AuthenticationException} 中提取异常信息。
     *
     * @param e 原始的 {@link AuthenticationException}。
     */
    public PreJwtCheckAuthenticationException(AuthenticationException e) {
        super(e.getMessage(), e.getCause());
    }
}

