package com.github.stazxr.zblog.base.component.security.jwt.encoder;

import com.github.stazxr.zblog.base.component.security.jwt.JwtException;

/**
 * This exception is thrown when an error occurs while attempting to encode a JSON Web Token.
 *
 * @author SunTao
 * @since 2022-08-17
 */
public class JwtEncodingException extends JwtException {
	public JwtEncodingException(String message) {
		super(message);
	}

	public JwtEncodingException(String message, Throwable cause) {
		super(message, cause);
	}
}