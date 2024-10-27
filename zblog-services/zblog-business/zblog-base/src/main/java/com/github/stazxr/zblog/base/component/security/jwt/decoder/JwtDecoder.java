package com.github.stazxr.zblog.base.component.security.jwt.decoder;

import com.github.stazxr.zblog.base.component.security.jwt.JwtException;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * Decoding a JWS TO JSON Web Token (JWT)
 *
 * @author SunTao
 * @since 2022-08-17
 */
public interface JwtDecoder {
	/**
	 * Decode the JWS to JWT.
	 *
	 * @param jws JWS
	 * @return JWT
	 * @throws JwtException if an error occurs while attempting to decode the JWS
	 */
	JWTClaimsSet decode(String jws) throws JwtException;
}