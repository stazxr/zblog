package com.github.stazxr.zblog.bas.security.authn.userpass.numcode;

import com.github.stazxr.zblog.bas.i18n.I18nUtils;
import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常类。
 *
 * <p>
 * 此异常继承自 {@link AuthenticationException}，是 Spring Security 认证异常体系的一部分。
 * 可用于在认证流程中抛出，触发自定义异常处理逻辑。
 * </p>
 *
 * @author SunTao
 * @since 2024-11-18
 */
public class LoginNumCodeException extends AuthenticationException {
    private static final long serialVersionUID = 2399675277250587214L;

    private final LoginNumCodeErrorCode errorCode;

    /**
     * 构造方法，接受自定义异常信息。
     *
     * @param errorCode 异常消息
     */
    public LoginNumCodeException(LoginNumCodeErrorCode errorCode) {
        super(I18nUtils.getMessage(errorCode.getI18nKey()));
        this.errorCode = errorCode;
    }

    /**
     * 构造方法，接受自定义异常信息和异常原因。
     *
     * @param errorCode 异常消息
     * @param cause     异常原因，可用于追踪根因
     */
    public LoginNumCodeException(LoginNumCodeErrorCode errorCode, Throwable cause) {
        super(I18nUtils.getMessage(errorCode.getI18nKey()), cause);
        this.errorCode = errorCode;
    }

    public LoginNumCodeErrorCode getErrorCode() {
        return errorCode;
    }
}
