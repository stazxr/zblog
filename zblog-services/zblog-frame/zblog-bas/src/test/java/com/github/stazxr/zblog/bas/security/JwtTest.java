package com.github.stazxr.zblog.bas.security;

import com.nimbusds.jose.JWSAlgorithm;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class JwtTest {
    @Test
    public void testJWSAlgorithm() {
        JWSAlgorithm jwsAlgorithm = new JWSAlgorithm("HS256");
        System.out.println(jwsAlgorithm);
    }
}
