package com.github.stazxr.zblog.bas.security.exception;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import com.github.stazxr.zblog.bas.i18n.I18nUtils;
import org.springframework.security.authentication.AccountStatusException;

/**
 * 系统用户禁止登录异常。
 *
 * @author SunTao
 * @since 2026-03-16
 */
public class SystemUserDeniedException extends AccountStatusException {
	private static final long serialVersionUID = -1223684075095843103L;

	private static final ErrorCode ERROR_CODE = AuthenticationErrorCode.EAUTHN005;

	public SystemUserDeniedException() {
		super(I18nUtils.getMessage(ERROR_CODE.getI18nKey()));
	}

	public ErrorCode getErrorCode() {
		return ERROR_CODE;
	}
}
