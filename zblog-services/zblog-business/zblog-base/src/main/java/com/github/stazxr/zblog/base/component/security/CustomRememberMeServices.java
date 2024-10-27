package com.github.stazxr.zblog.base.component.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * 记住我
 *
 * @author SunTao
 * @since 2022-06-14
 */
public class CustomRememberMeServices extends PersistentTokenBasedRememberMeServices {
    /**
     * 过期时间，默认两周
     */
    public static final int TWO_WEEKS_S = 1209600;

    /**
     * remember-me 的值必须是 true | on | yes | 1
     */
    private static final String[] ALLOWED_VALUE = {"true", "on", "yes", "1"};

    public CustomRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
        setTokenValiditySeconds(TWO_WEEKS_S);
    }

    /**
     * Allows customization of whether a remember-me login has been requested. The default
     * is to return true if <tt>alwaysRemember</tt> is set or the configured parameter
     * name has been included in the request and is set to the value "true".
     *
     * @param request   the request submitted from an interactive login, which may include
     *                  additional information indicating that a persistent login is desired.
     * @param parameter the configured remember-me parameter name.
     * @return true if the request includes information indicating that a persistent login
     * has been requested.
     */
    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        String paramValue = request.getAttribute(parameter).toString();
        if (paramValue != null) {
            return paramValue.equalsIgnoreCase(ALLOWED_VALUE[0]) || paramValue.equalsIgnoreCase(ALLOWED_VALUE[1])
                    || paramValue.equalsIgnoreCase(ALLOWED_VALUE[2]) || paramValue.equals(ALLOWED_VALUE[3]);
        }
        return false;
    }
}
