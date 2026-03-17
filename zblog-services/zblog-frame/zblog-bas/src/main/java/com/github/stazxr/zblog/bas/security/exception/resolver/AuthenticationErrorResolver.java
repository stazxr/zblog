package com.github.stazxr.zblog.bas.security.exception.resolver;

import com.github.stazxr.zblog.bas.exception.code.CommonErrorCode;
import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import com.github.stazxr.zblog.bas.security.authn.userpass.numcode.LoginNumCodeException;
import com.github.stazxr.zblog.bas.security.exception.AuthenticationErrorCode;
import com.github.stazxr.zblog.bas.security.exception.SystemUserDeniedException;
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
        if (exception instanceof LoginNumCodeException) {
            return ((LoginNumCodeException) exception).getErrorCode();
        }

        if (exception instanceof SystemUserDeniedException) {
            return ((SystemUserDeniedException) exception).getErrorCode();
        }

        if (exception instanceof LockedException) {
            return AuthenticationErrorCode.EAUTHN001;
        }

        if (exception instanceof DisabledException) {
            return AuthenticationErrorCode.EAUTHN002;
        }

        if (exception instanceof AccountExpiredException) {
            return AuthenticationErrorCode.EAUTHN003;
        }

        if (exception instanceof CredentialsExpiredException) {
            return AuthenticationErrorCode.EAUTHN004;
        }

        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {
            return AuthenticationErrorCode.EAUTHN000;
        }

        return CommonErrorCode.SBASEA000;
    }
}
