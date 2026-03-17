package com.github.stazxr.zblog.bas.security.hanlder;

import com.github.stazxr.zblog.bas.exception.code.CommonErrorCode;
import com.github.stazxr.zblog.bas.i18n.I18nUtils;
import com.github.stazxr.zblog.bas.rest.Result;
import com.github.stazxr.zblog.bas.rest.util.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义访问拒绝处理器，实现 Spring Security 提供的 {@link AccessDeniedHandler} 接口。
 * 用于处理已认证用户访问无权限资源时的逻辑。
 *
 * @author SunTao
 * @since 2024-11-18
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    // 资源被禁止访问
    public static final String FORBIDDEN = "error.service.resource.forbidden";
    // 测试用户被禁止访问
    public static final String TEST = "error.service.resource.test";

    /**
     * 处理访问拒绝逻辑。
     *
     * @param request               当前 HTTP 请求
     * @param response              当前 HTTP 响应
     * @param accessDeniedException 访问拒绝异常
     * @throws IOException 可能的 IO 异常
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        if (!response.isCommitted()) {
            try {
                CommonErrorCode errorCode = CommonErrorCode.EBASEA000;
                String errorMessage = accessDeniedException.getMessage();
                if (FORBIDDEN.equals(errorMessage)) {
                    errorMessage = I18nUtils.getMessage(FORBIDDEN);
                } else if (TEST.equals(errorMessage)) {
                    errorMessage = I18nUtils.getMessage(TEST);
                } else {
                    errorMessage = I18nUtils.getMessage(errorCode.getI18nKey());
                }
                Result<Void> result = Result.failure(errorCode.getCode(), errorMessage);
                ResponseUtils.responseJsonWriter(response, result, HttpStatus.FORBIDDEN);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        }
    }
}
