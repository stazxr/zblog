package com.github.stazxr.zblog.bas.security.jwt.encoder;

import com.github.stazxr.zblog.bas.security.jwt.JwtException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * 接口用于将 JSON Web Token (JWT) 编码为 JSON Web Signature (JWS)。
 * <p>
 * 该接口定义了编码 JWT 为 JWS 的方法，通常用于生成经过签名的 JWT，以便安全地传输。
 * 通过指定签名算法和 JWT 声明集，此接口可以将 JWT 转换为 JWS 格式。
 * </p>
 *
 * @author SunTao
 * @since 2022-08-17
 */
public interface JwtEncoder {
	/**
	 * 将给定的 JWT 声明集编码为 JWS（签名的 JWT）。
	 * <p>
	 * 该方法通过指定的算法对 JWT 进行编码，并生成一个有效的 JWS 字符串。JWS 字符串可以用于安全地传输信息。
	 * </p>
	 *
	 * @param algorithm 用于签名 JWT 的算法（例如：RS256、HS256 等）
	 * @param claims JWT 声明集，包含有关用户或客户端的有意义的信息（如身份、权限等）
	 * @return 生成的 JWS 字符串，代表经过签名的 JWT
	 * @throws JwtException 如果在编码过程中发生错误，例如签名失败或算法不支持等
	 */
	String encode(JWSAlgorithm algorithm, JWTClaimsSet claims) throws JwtException;
}