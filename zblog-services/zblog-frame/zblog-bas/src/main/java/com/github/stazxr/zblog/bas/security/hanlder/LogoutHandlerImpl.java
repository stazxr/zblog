package com.github.stazxr.zblog.bas.security.hanlder;

import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.security.service.SecurityLogoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Component
public class LogoutHandlerImpl implements LogoutHandler {
    private static final Logger log = LoggerFactory.getLogger(LogoutHandlerImpl.class);

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
        if (authentication == null || authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof SecurityUser)) {
            return;
        }

        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        String username = user.getUsername();
        Long userId = user.getId();

        // 记录用户登出日志
        log.info("用户 [{}] (ID: {}) 正在登出...", username, userId);

        try {
            // 清除用户登录信息
            securityLogoutService.clearUserLoginInfo(String.valueOf(userId));
            log.info("用户 [{}] (ID: {}) 登出成功", username, userId);
        } catch (Exception e) {
            log.error("用户 [{}] (ID: {}) 登出失败", username, userId, e);
        }
    }

    @Autowired
    public void setClearUserLoginManager(SecurityLogoutService clearUserLoginManager) {
        this.securityLogoutService = clearUserLoginManager;
    }
}
