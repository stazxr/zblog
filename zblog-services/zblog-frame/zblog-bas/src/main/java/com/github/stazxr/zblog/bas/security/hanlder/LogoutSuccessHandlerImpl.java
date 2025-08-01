package com.github.stazxr.zblog.bas.security.hanlder;

import com.github.stazxr.zblog.bas.msg.Result;
import com.github.stazxr.zblog.bas.msg.util.ResponseUtils;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登出成功处理器，实现 Spring Security 提供的 {@link LogoutSuccessHandler} 接口。
 * 负责处理用户登出成功后的操作，如记录日志和返回 JSON 响应。
 *
 * @author SunTao
 * @since 2024-11-18
 */
@Slf4j
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    /**
     * 处理用户登出成功事件。
     * 包括记录用户登出日志，并向客户端返回成功响应。
     *
     * @param request       当前 HTTP 请求
     * @param response      当前 HTTP 响应
     * @param authentication 当前用户的认证信息，可能为空
     * @throws IOException 可能的 IO 异常
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication != null && authentication.getPrincipal() != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof SecurityUser) {
                SecurityUser securityUser = (SecurityUser) principal;
                log.info("用户 [{}] (ID: {}) 登出成功", securityUser.getUsername(), securityUser.getId());
            } else {
                log.warn("无法识别的用户信息，可能是匿名用户登出。");
            }
        } else {
            log.info("匿名用户或未认证用户登出成功。");
        }

        // 构造返回结果并输出响应
        ResponseUtils.responseJsonWriter(response, Result.success("登出成功"));
    }
}

