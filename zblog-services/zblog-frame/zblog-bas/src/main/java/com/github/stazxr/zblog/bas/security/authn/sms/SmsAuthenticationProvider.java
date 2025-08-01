package com.github.stazxr.zblog.bas.security.authn.sms;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 *
 *
 * @author SunTao
 * @since 2024-11-10
 */
public class SmsAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String telephone = (String) authentication.getPrincipal();
        String smsCode = (String) authentication.getCredentials();

        // TODO 短信验证码校验逻辑

        return new SmsAuthenticationToken(telephone, smsCode);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
