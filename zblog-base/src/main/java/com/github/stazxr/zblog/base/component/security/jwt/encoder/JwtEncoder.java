package com.github.stazxr.zblog.base.component.security.jwt.encoder;

import com.github.stazxr.zblog.base.component.security.jwt.JwtException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * Encoding a JSON Web Token (JWT) TO JWS
 *
 * @author SunTao
 * @since 2022-08-17
 */
public interface JwtEncoder {
	/**
	 * Encode the JWT to JWS.
	 *
	 * @param algorithm the algorithm
	 * @param claims the JWT Claims Set
	 * @return jws
	 * @throws JwtException if an error occurs while attempting to encode the JWT
	 */
	String encode(JWSAlgorithm algorithm, JWTClaimsSet claims) throws JwtException;
}