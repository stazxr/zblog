package com.github.stazxr.zblog.bas.security.authn.sms;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public SmsAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String telephone = request.getParameter("telephone");
        String smsCode = request.getParameter("smsCode");
        SmsAuthenticationToken authenticationToken = new SmsAuthenticationToken(telephone, smsCode);
        return getAuthenticationManager().authenticate(authenticationToken);
    }
}
