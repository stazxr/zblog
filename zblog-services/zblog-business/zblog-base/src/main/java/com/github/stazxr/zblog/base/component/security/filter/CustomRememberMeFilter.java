package com.github.stazxr.zblog.base.component.security.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 记住我过滤器
 *
 * @author SunTao
 * @since 2022-06-16
 */
public class CustomRememberMeFilter extends RememberMeAuthenticationFilter {
    public static final String FROM_REMEMBER_ME = "from-rememberMe";

    public CustomRememberMeFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices) {
        super(authenticationManager, rememberMeServices);
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        request.setAttribute(FROM_REMEMBER_ME, true);
    }
}
