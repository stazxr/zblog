package com.github.stazxr.zblog.base.security.handler;

import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.security.jwt.cache.impl.JwtTokenCacheStorage;
import com.github.stazxr.zblog.base.service.ZblogService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.util.CacheUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义登出处理器
 *
 * @author SunTao
 * @since 2022-01-20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final ZblogService zblogService;

    private final JwtTokenCacheStorage jwtTokenStorage;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        log.info("username: {}  is offline now", username);

        // 注销token
        jwtTokenStorage.expire(username);

        // 清除RememberMe
        zblogService.removeRememberMe(username);
    }
}
