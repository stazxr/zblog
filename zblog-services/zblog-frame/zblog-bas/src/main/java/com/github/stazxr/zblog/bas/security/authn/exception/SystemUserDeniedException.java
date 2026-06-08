package com.github.stazxr.zblog.bas.security.authn.exception;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import org.springframework.security.core.AuthenticationException;

/**
 * 系统用户禁止登录异常。
 *
 * @author SunTao
 * @since 2026-03-16
 */
public class SystemUserDeniedException extends AuthenticationException {
	private static final long serialVersionUID = -1223684075095843103L;

	private static final ErrorCode ERROR_CODE = AuthenticationErrorCode.EAUTHN005;

	public SystemUserDeniedException() {
		super("Pre check user status failed: Denied");
	}

	public ErrorCode getErrorCode() {
		return ERROR_CODE;
	}
}
