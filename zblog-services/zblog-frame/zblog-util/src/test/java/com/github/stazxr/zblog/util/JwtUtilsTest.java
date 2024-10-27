package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.util.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 测试JWT工具类
 *
 * @author Thomas Sun
 * @since 2023-04-07
 */
public class JwtUtilsTest {
    @Test
    @Ignore
    public void test1() throws Exception {
        JwtBuilder jwtBuilder = JwtUtils.buildJwtBuilder("login", 600, "jwt-rule-c0647f136cfc5ce09cd3c2f3b025da69").claim("loginId", "1").claim("forceResetPwd", "1").claim("roleId", "").claim("deptId", "");
        String token = JwtUtils.buildJwt(jwtBuilder);
        System.out.println(token);

        Claims claims = JwtUtils.getClaims(token, "jwt-rule-c0647f136cfc5ce09cd3c2f3b025da69");
        System.out.println(claims);
    }
}
