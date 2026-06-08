package com.github.stazxr.zblog.bas.security.authn.exception.resolver;

import com.github.stazxr.zblog.bas.exception.code.CommonErrorCode;
import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import com.github.stazxr.zblog.bas.security.authn.exception.AuthenticationErrorCode;
import com.github.stazxr.zblog.bas.security.authn.userpass.numcode.LoginNumCodeException;
import com.github.stazxr.zblog.bas.security.authn.exception.SystemUserDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 认证失败错误码解析器。
 *
 * @author SunTao
 * @since 2026-03-16
 */
@Component
public class AuthenticationErrorResolver {
    public ErrorCode resolve(AuthenticationException exception) {
        Throwable realException = exception;
        if (exception instanceof InternalAuthenticationServiceException) {
            realException = exception.getCause();
        }

        if (realException instanceof LoginNumCodeException) {
            return ((LoginNumCodeException) realException).getErrorCode();
        }

        if (realException instanceof SystemUserDeniedException) {
            return ((SystemUserDeniedException) realException).getErrorCode();
        }

        if (realException instanceof LockedException) {
            return AuthenticationErrorCode.EAUTHN001;
        }

        if (realException instanceof DisabledException) {
            return AuthenticationErrorCode.EAUTHN002;
        }

        if (realException instanceof AccountExpiredException) {
            return AuthenticationErrorCode.EAUTHN003;
        }

        if (realException instanceof CredentialsExpiredException) {
            return AuthenticationErrorCode.EAUTHN004;
        }

        if (realException instanceof UsernameNotFoundException || realException instanceof BadCredentialsException) {
            return AuthenticationErrorCode.EAUTHN000;
        }

        return CommonErrorCode.SBASEA000;
    }
}
