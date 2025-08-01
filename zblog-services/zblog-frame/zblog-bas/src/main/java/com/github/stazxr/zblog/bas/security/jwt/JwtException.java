package com.github.stazxr.zblog.bas.security.jwt;

/**
 * 自定义的 JWT 异常类，用于表示与 JWT 相关的错误。
 *
 * @author SunTao
 * @since 2022-08-18
 */
public class JwtException extends RuntimeException {
    /**
     * 默认构造函数
     */
    public JwtException() {
        super();
    }

    /**
     * 创建一个包含指定错误消息的 JwtException 对象。
     *
     * @param message 异常的详细错误消息，用于描述发生的错误
     */
    public JwtException(String message) {
        super(message);
    }

    /**
     * 创建一个包含指定错误消息和原因的 JwtException 对象。
     *
     * @param message 错误消息，提供关于异常的描述
     * @param cause   引发此异常的原因
     */
    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
