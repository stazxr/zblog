package com.github.stazxr.zblog.base.component.security.jwt.decoder;

import com.github.stazxr.zblog.base.component.security.jwt.JwtException;

/**
 * This exception is thrown when an error occurs while attempting to decode a JSON Web Token.
 *
 * @author SunTao
 * @since 2022-08-17
 */
public class JwtDecodingException extends JwtException {
	public JwtDecodingException(String message) {
		super(message);
	}

	public JwtDecodingException(String message, Throwable cause) {
		super(message, cause);
	}
}