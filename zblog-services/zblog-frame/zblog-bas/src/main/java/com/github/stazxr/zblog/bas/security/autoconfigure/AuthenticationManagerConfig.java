package com.github.stazxr.zblog.bas.security.autoconfigure;

import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.authn.userpass.UserPassAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * AuthenticationManager 配置
 *
 * @author SunTao
 * @since 2026-03-10
 */
@Configuration
@RequiredArgsConstructor
public class AuthenticationManagerConfig {
    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final SecurityExtProperties securityExtProperties;

    @Bean
    public UserPassAuthenticationProvider userPassAuthenticationProvider(@Autowired(required = false) UserDetailsChecker customPostAuthenticationChecks) {
        UserPassAuthenticationProvider provider = new UserPassAuthenticationProvider(passwordEncoder, userDetailsService);
        provider.setEnableAdditionalChecks(securityExtProperties.isEnableAdditionalChecks());
        provider.setEnableAdditionalChecks(securityExtProperties.isEnableAdditionalChecks());
        provider.setHideUserNotFoundExceptions(false);
        provider.setCustomPostAuthenticationChecks(customPostAuthenticationChecks);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> providers) {
        return new ProviderManager(providers);
    }
}
