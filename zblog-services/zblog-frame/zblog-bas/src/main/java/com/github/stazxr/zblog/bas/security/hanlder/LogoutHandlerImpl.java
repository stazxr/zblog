package com.github.stazxr.zblog.bas.security.hanlder;

import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.security.service.SecurityLogoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义注销处理器，实现 Spring Security 提供的 {@link LogoutHandler} 接口。
 * 主要职责是处理用户登出事件，清除用户登录相关信息。
 *
 * @author SunTao
 * @since 2024-11-18
 */
@Slf4j
@Component
public class LogoutHandlerImpl implements LogoutHandler {
    private SecurityLogoutService securityLogoutService;

    /**
     * 处理用户注销逻辑，包括记录登出日志和清除用户的登录信息。
     *
     * @param request       当前 HTTP 请求
     * @param response      当前 HTTP 响应
     * @param authentication 当前用户的认证信息，可能为空
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            log.warn("用户未登录，无法执行登出操作。");
            return;
        }

        // 获取当前用户信息
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof SecurityUser)) {
            log.warn("无法识别的用户信息，登出操作被跳过。");
            return;
        }

        SecurityUser user = (SecurityUser) principal;
        String username = user.getUsername();
        Long userId = user.getId();

        // 记录用户登出日志
        log.info("用户 [{}] (ID: {}) 正在登出...", username, userId);

        try {
            // 清除用户登录信息
            securityLogoutService.clearUserLoginInfo(String.valueOf(userId));
            log.info("用户 [{}] (ID: {}) 的登录信息已清除。", username, userId);
        } catch (Exception e) {
            log.error("用户 [{}] (ID: {}) 的登录信息清除失败：{}", username, userId, e.getMessage(), e);
        }
    }

    @Autowired
    public void setClearUserLoginManager(SecurityLogoutService clearUserLoginManager) {
        this.securityLogoutService = clearUserLoginManager;
    }
}
