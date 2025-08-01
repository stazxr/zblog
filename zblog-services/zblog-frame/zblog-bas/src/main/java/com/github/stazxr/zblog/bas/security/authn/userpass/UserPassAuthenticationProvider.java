package com.github.stazxr.zblog.bas.security.authn.userpass;

import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author SunTao
 * @since 2024-11-18
 */
@Component
public class UserPassAuthenticationProvider extends DaoAuthenticationProvider implements InitializingBean {
    private boolean enableAdditionalChecks;

    private SecurityExtProperties securityExtProperties;

    private UserDetailsChecker customPostAuthenticationChecks;

    public UserPassAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        setPasswordEncoder(passwordEncoder);
        setUserDetailsService(userDetailsService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return super.authenticate(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // 自定义附加校验逻辑
        if (enableAdditionalChecks && customPostAuthenticationChecks != null) {
            customPostAuthenticationChecks.check(userDetails);
        }

        // 调用父类逻辑
        super.additionalAuthenticationChecks(userDetails, authentication);
    }

    public boolean isEnableAdditionalChecks() {
        return enableAdditionalChecks;
    }

    public void setEnableAdditionalChecks(boolean enableAdditionalChecks) {
        this.enableAdditionalChecks = enableAdditionalChecks;
    }

    @Autowired
    public void setSecurityExtProperties(SecurityExtProperties securityExtProperties) {
        this.securityExtProperties = securityExtProperties;
    }

    @Autowired(required = false)
    public void setCustomPostAuthenticationChecks(UserDetailsChecker customPostAuthenticationChecks) {
        this.customPostAuthenticationChecks = customPostAuthenticationChecks;
    }

    @Override
    protected void doAfterPropertiesSet() {
        setEnableAdditionalChecks(securityExtProperties.isEnableAdditionalChecks());
        setHideUserNotFoundExceptions(securityExtProperties.isHideUserNotFoundExceptions());
        super.doAfterPropertiesSet();
    }
}
