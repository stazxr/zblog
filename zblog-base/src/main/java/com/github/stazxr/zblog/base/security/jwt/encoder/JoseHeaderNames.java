/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.stazxr.zblog.base.security.jwt.encoder;

/**
 * The Registered Header Parameter Names defined by the JSON Web Token (JWT),
 * JSON Web Signature (JWS) and JSON Web Encryption (JWE) specifications
 * that may be contained in the JOSE Header of a JWT.
 *
 * @author Anoop Garlapati
 * @author Joe Grandja
 */
public final class JoseHeaderNames {
	/**
	 * 算法标头标识用于保护 JWS 或 JWE 的加密算法
	 */
	public static final String ALG = "alg";

	/**
	 * JWK Set URL 标头是一个 URI，它引用一组 JSON 编码的公钥的资源，其中一个对应于用于对 JWS 进行数字签名或加密 JWE 的密钥
	 */
	public static final String JKU = "jku";

	/**
	 * JSON Web Key 标头是与用于对 JWS 进行数字签名或加密 JWE 的密钥对应的公钥
	 */
	public static final String JWK = "jwk";

	/**
	 * 密钥 ID 标头是指示用于保护 JWS 或 JWE 的密钥的提示
	 */
	public static final String KID = "kid";

	/**
	 * X.509 URL 标头是一个 URI，它引用 X.509 公钥证书或证书链的资源，对应于用于对 JWS 进行数字签名或加密 JWE 的密钥
	 */
	public static final String X5U = "x5u";

	/**
	 * X.509 证书链头包含与用于对 JWS 进行数字签名或加密 JWE 的密钥对应的 X.509 公钥证书或证书链
	 */
	public static final String X5C = "x5c";

	/**
	 * X.509 证书 SHA-1 指纹标头是 X.509 证书的 DER 编码的 base64url 编码的 SHA-1 指纹（又名摘要），对应于用于对 JWS 进行数字签名或加密 JWE 的密钥
	 */
	public static final String X5T = "x5t";

	/**
	 * X.509 证书 SHA-256 指纹标头是 X.509 证书的 DER 编码的 base64url 编码的 SHA-256 指纹（又名摘要），对应于用于对 JWS 进行数字签名或加密 JWE 的密钥
	 */
	public static final String X5T_S256 = "x5t#S256";

	/**
	 * JWS/JWE 应用程序使用类型标头来声明 JWS/JWE 的媒体类型
	 */
	public static final String TYP = "typ";

	/**
	 * JWS/JWE 应用程序使用内容类型标头来声明受保护内容（有效负载）的媒体类型
	 */
	public static final String CTY = "cty";

	/**
	 * 关键标头指示正在使用对 JWS/JWE/JWA 规范的扩展，必须理解和处理这些规范
	 */
	public static final String CRIT = "crit";

	private JoseHeaderNames() {
	}
}