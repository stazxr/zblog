package com.github.stazxr.zblog.bas.security.authn.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 *
 *
 * @author SunTao
 * @since 2024-11-10
 */
public class SmsAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 2587654456840870303L;

    /**
     * 手机号
     */
    private final String telephone;

    /**
     * 短信验证码
     */
    private final String smsCode;

    public SmsAuthenticationToken(String telephone, String smsCode) {
        super(null);
        this.telephone = telephone;
        this.smsCode = smsCode;
        setAuthenticated(false);
    }

    public SmsAuthenticationToken(String telephone, String smsCode, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.telephone = telephone;
        this.smsCode = smsCode;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
