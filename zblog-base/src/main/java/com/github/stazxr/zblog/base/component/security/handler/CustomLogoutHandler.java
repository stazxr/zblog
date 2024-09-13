package com.github.stazxr.zblog.base.component.security.handler;

import com.github.stazxr.zblog.base.component.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.service.UserService;
import com.github.stazxr.zblog.base.service.ZblogService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.cache.util.GlobalCache;
import com.github.stazxr.zblog.util.net.IpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

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

    private final UserService userService;

    private final JwtTokenStorage jwtTokenStorage;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() != null) {
            User user = (User) authentication.getPrincipal();
            String username = user.getUsername();
            log.info("用户 {} 正在注销...", username);

            // 注销 token
            userService.clearUserStorageToken(user.getId());

            // 清除缓存
            jwtTokenStorage.expire(user.getId());
            String preTknCacheKey = String.format(Constants.SysCacheKey.preTkn.cacheKey(), user.getId(), Locale.ROOT);
            String ssoTknCacheKey = String.format(Constants.SysCacheKey.ssoTkn.cacheKey(), IpUtils.getIp(request), Locale.ROOT);
            GlobalCache.remove(preTknCacheKey, ssoTknCacheKey);

            // 清除 RememberMe
            zblogService.removeRememberMe(username);
        }
    }
}
