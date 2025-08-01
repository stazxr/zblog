package com.github.stazxr.zblog.bas.security.jwt.decoder;

import com.github.stazxr.zblog.bas.security.jwt.JwtException;

/**
 * 表示解码 JWT（JSON Web Token）过程中发生的异常。
 * <p>
 * 该异常通常用于表示在尝试解码 JWT 时出现的错误，例如无效的 JWT 格式或无法解析的 JWT。
 * </p>
 *
 * @author SunTao
 * @since 2022-08-17
 */
public class JwtDecodingException extends JwtException {
	/**
	 * 使用指定的错误消息创建一个新的 {@code JwtDecodingException} 实例。
	 *
	 * @param message 错误消息，提供详细的异常信息，帮助开发人员理解发生了什么错误
	 */
	public JwtDecodingException(String message) {
		super(message);
	}

	/**
	 * 使用指定的错误消息和原因创建一个新的 {@code JwtDecodingException} 实例。
	 *
	 * @param message 错误消息，提供详细的异常信息
	 * @param cause 引发此异常的根本原因，通常是另一个异常
	 */
	public JwtDecodingException(String message, Throwable cause) {
		super(message, cause);
	}
}