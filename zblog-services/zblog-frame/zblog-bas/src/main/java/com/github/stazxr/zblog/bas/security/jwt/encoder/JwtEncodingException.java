package com.github.stazxr.zblog.bas.security.jwt.encoder;

import com.github.stazxr.zblog.bas.security.jwt.JwtException;

/**
 * 表示编码 JWT（JSON Web Token）过程中发生的异常。
 * <p>
 * 该异常通常用于表示在尝试编码 JWT 时出现的错误，例如无效的输入数据、无法生成有效的 JWT 或编码过程中出现的其他问题。
 * </p>
 *
 * @author SunTao
 * @since 2022-08-19
 */
public class JwtEncodingException extends JwtException {
	/**
	 * 使用指定的错误消息创建一个新的 {@code JwtEncodingException} 实例。
	 *
	 * @param message 错误消息，提供详细的异常信息，帮助开发人员理解发生了什么错误
	 */
	public JwtEncodingException(String message) {
		super(message);
	}

	/**
	 * 使用指定的错误消息和原因创建一个新的 {@code JwtEncodingException} 实例。
	 *
	 * @param message 错误消息，提供详细的异常信息
	 * @param cause 引发此异常的根本原因，通常是另一个异常
	 */
	public JwtEncodingException(String message, Throwable cause) {
		super(message, cause);
	}
}