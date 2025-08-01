package com.github.stazxr.zblog.bas.security.jwt.decoder;

import com.github.stazxr.zblog.bas.security.jwt.JwtException;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * JWS（JSON Web Signature）解码器接口，用于将 JWS 解码为 JWT（JSON Web Token）。
 * <p>
 * 实现此接口的类应提供将 JWS 转换为 JWT 的具体解码实现。
 * </p>
 *
 * @author SunTao
 * @since 2022-08-17
 */
public interface JwtDecoder {
	/**
	 * 解码 JWS（JSON Web Signature）为 JWT（JSON Web Token）。
	 * <p>
	 * 该方法将 JWS 字符串解码为 JWT 声明集（JWTClaimsSet），该声明集包含了 JWT 的有效载荷信息。
	 * </p>
	 *
	 * @param jws 待解码的 JWS 字符串
	 * @return 解码后的 JWT 声明集（JWTClaimsSet）
	 * @throws JwtException 如果在解码过程中发生错误（例如，格式不正确或无效的签名）
	 */
	JWTClaimsSet decode(String jws) throws JwtException;
}