package com.github.stazxr.zblog.bas.security.exception.handler;

import com.github.stazxr.zblog.bas.rest.Result;
import com.github.stazxr.zblog.bas.rest.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
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
        // 获取当前请求路径和用户信息
        String requestUri = request.getRequestURI();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "匿名用户";

        // 记录访问拒绝日志
        log.warn("用户 [{}] 尝试访问 [{}] 被拒绝：{}", username, requestUri, accessDeniedException.getMessage());

        // 返回结果
        Result result = Result.failure("您没有权限访问该资源").code(HttpStatus.FORBIDDEN.value()).data(requestUri);
        ResponseUtils.responseJsonWriter(response, result);
    }
}
