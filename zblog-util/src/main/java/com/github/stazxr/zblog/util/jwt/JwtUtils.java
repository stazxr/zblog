package com.github.stazxr.zblog.util.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

/**
 * JJWT工具类
 *
 * @author SunTao
 * @since 2023-04-07
 */
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private static final String JWT_SEPARATOR = "Bearer ";

    /**
     * 采用的加密算法
     */
    private static final SignatureAlgorithm JWT_ALG;

    /**
     * 默认有效期
     */
    private static final int JWT_DURATION = 600;

    static {
        JWT_ALG = SignatureAlgorithm.HS256;
    }

    private JwtUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static SecretKey generateKey(SignatureAlgorithm alg) {
        return MacProvider.generateKey(alg);
    }

    public static SecretKey generateKey(SignatureAlgorithm alg, String rule) {
        byte[] bytes = Base64.decodeBase64(rule);
        return new SecretKeySpec(bytes, alg.getJcaName());
    }

    public static JwtBuilder buildJwtBuilder(String sub, String jwtRule) {
        return buildJwtBuilder(sub, null, UUID.randomUUID().toString(), null, null, JWT_DURATION, jwtRule);
    }

    public static JwtBuilder buildJwtBuilder(String sub, String aud, String jti, String iss, Date nbf, Integer duration, String jwtRule) {
        return buildJwtBuilder(JWT_ALG, generateKey(JWT_ALG, jwtRule), sub, aud, jti, iss, nbf, duration);
    }

    public static JwtBuilder buildJwtBuilder(String sub, Integer duration, String jwtRule) {
        return buildJwtBuilder(sub, null, UUID.randomUUID().toString(), null, null, duration, jwtRule);
    }

    public static JwtBuilder buildJwtBuilder(String sub, String jti, Integer duration, String jwtRule) {
        return buildJwtBuilder(sub, null, jti, null, null, duration, jwtRule);
    }

    public static JwtBuilder buildJwtBuilder(SignatureAlgorithm alg, Key key, String sub, String aud, String jti, String iss, Date nbf, Integer duration) {
        Date iat = new Date();
        Date exp = null;
        if (duration != null) {
            exp = nbf == null ? DateUtils.addSeconds(iat, duration) : DateUtils.addSeconds(nbf, duration);
        }

        return Jwts.builder().signWith(alg, key).setSubject(sub).setAudience(aud).setId(jti).setIssuer(iss).setNotBefore(nbf).setIssuedAt(iat).setExpiration(exp);
    }

    public static String buildJwt(JwtBuilder jwtBuilder) {
        return JWT_SEPARATOR + jwtBuilder.compact();
    }

    public static String buildJwt(String sub, String jwtRule) {
        return buildJwt(sub, null, UUID.randomUUID().toString(), null, null, 600, jwtRule);
    }

    public static String buildJwt(String sub, String jti, Integer duration, String jwtRule) {
        return buildJwt(sub, null, jti, null, null, duration, jwtRule);
    }

    public static String buildJwt(String sub, String aud, String jti, String iss, Date nbf, Integer duration, String jwtRule) {
        return buildJwt(JWT_ALG, generateKey(JWT_ALG, jwtRule), sub, aud, jti, iss, nbf, duration);
    }

    public static String buildJwt(SignatureAlgorithm alg, Key key, String sub, String aud, String jti, String iss, Date nbf, Integer duration) {
        String compact = buildJwtBuilder(alg, key, sub, aud, jti, iss, nbf, duration).compact();
        return JWT_SEPARATOR + compact;
    }

    public static Jws<Claims> parseJwt(Key key, String claimsJws) {
        claimsJws = StringUtils.substringAfter(claimsJws, JWT_SEPARATOR);
        return Jwts.parser().setSigningKey(key).parseClaimsJws(claimsJws);
    }

    public static Jws<Claims> parseJwt(String claimsJws, String jwtRule) {
        SecretKey key = generateKey(JWT_ALG, jwtRule);
        claimsJws = StringUtils.substringAfter(claimsJws, JWT_SEPARATOR);
        return Jwts.parser().setSigningKey(key).parseClaimsJws(claimsJws);
    }

    public static Claims getClaims(String claimsJws, String jwtRule) {
        return parseJwt(claimsJws, jwtRule).getBody();
    }

    public static Boolean checkJwt(String claimsJws, String jwtRule) {
        boolean flag = false;

        try {
            SecretKey key = generateKey(JWT_ALG, jwtRule);
            flag = parseJwt(key, claimsJws).getBody() != null;
        } catch (Exception var4) {
            logger.warn("JWT验证出错，错误原因：{}", var4.getMessage());
        }

        return flag;
    }

    public static Boolean checkJwt(String claimsJws, String sub, String jwtRule) {
        return checkJwt(generateKey(JWT_ALG, jwtRule), claimsJws, sub);
    }

    private static Boolean checkJwt(Key key, String claimsJws, String sub) {
        boolean flag = false;

        try {
            Claims claims = parseJwt(key, claimsJws).getBody();
            flag = claims.getSubject().equals(sub);
        } catch (Exception var5) {
            logger.warn("JWT验证出错，错误原因：{}", var5.getMessage());
        }

        return flag;
    }
}
