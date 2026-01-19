package com.github.stazxr.zblog.bas.security.hanlder;

import com.github.stazxr.zblog.bas.msg.Result;
import com.github.stazxr.zblog.bas.msg.util.ResponseUtils;
import com.github.stazxr.zblog.bas.security.SecurityConstant;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.security.core.SecurityToken;
import com.github.stazxr.zblog.bas.security.jwt.JwtTokenGenerator;
import com.github.stazxr.zblog.bas.security.service.SecurityUserService;
import com.github.stazxr.zblog.bas.security.service.SecurityTokenService;
import com.github.stazxr.zblog.bas.security.sso.SsoToken;
import com.github.stazxr.zblog.bas.security.sso.SsoTokenCache;
import com.github.stazxr.zblog.util.net.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 自定义认证成功处理器，实现 Spring Security 提供的 {@link AuthenticationSuccessHandler} 接口。
 * 处理用户认证成功后的逻辑，包括生成 JWT Token、存储 Token、记录登录日志以及向客户端返回响应。
 *
 * @author SunTao
 * @since 2024-11-11
 */
@Slf4j
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    private JwtTokenGenerator jwtTokenGenerator;

    private SecurityTokenService userTokenService;

    private SecurityUserService securityUserService;

    /**
     * 用户认证成功后的处理逻辑。
     *
     * @param request        当前 HTTP 请求
     * @param response       当前 HTTP 响应
     * @param authentication 当前认证信息，包含用户的身份信息
     * @throws IOException 可能的 IO 异常
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            // 获取用户信息
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            String userId = String.valueOf(securityUser.getId());
            String userIp = IpUtils.getIp(request);
            log.info("用户 {} 登录成功，IP: {}", securityUser.getUsername(), userIp);

            // 生成新的 Token
            String token = jwtTokenGenerator.generateToken(request, userId, 1, null);
            log.debug("为用户 {} 生成了新的 Token: {}", userId, token);

            // 存储 Token
            SecurityToken userToken = new SecurityToken(userId, token);
            userTokenService.saveToken(userToken);

            // 缓存 SSO Token
            SsoToken ssoToken = new SsoToken(userId, token, userIp);
            SsoTokenCache.set(ssoToken);

            // 记录用户登录日志
            securityUserService.updateUserLoginInfo(securityUser.getUsername(), userIp, 1, request);

            // 构造登录结果并返回
            Map<String, Object> loginResult = new HashMap<>(1);
            loginResult.put("access_token", SecurityConstant.AUTHENTICATION_PREFIX.concat(token));
            ResponseUtils.responseJsonWriter(response, Result.success("登录成功").data(loginResult));
        } catch (Exception e) {
            log.error("处理认证成功逻辑时发生异常：{}", e.getMessage(), e);
            ResponseUtils.responseJsonWriter(response, Result.failure("登录失败，请稍后重试"));
        }
    }

    @Autowired
    public void setJwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @Autowired
    public void setUserTokenService(SecurityTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    @Autowired
    public void setSecurityUserService(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }
}
