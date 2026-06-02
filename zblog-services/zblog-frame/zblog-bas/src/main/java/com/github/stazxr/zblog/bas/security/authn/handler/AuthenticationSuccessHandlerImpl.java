package com.github.stazxr.zblog.bas.security.authn.handler;

import com.github.stazxr.zblog.bas.exception.code.CommonErrorCode;
import com.github.stazxr.zblog.bas.rest.Result;
import com.github.stazxr.zblog.bas.rest.ResultType;
import com.github.stazxr.zblog.bas.rest.util.ResponseUtils;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.security.jwt.JwtConstants;
import com.github.stazxr.zblog.bas.security.jwt.JwtContext;
import com.github.stazxr.zblog.bas.security.jwt.JwtTokenGenerator;
import com.github.stazxr.zblog.bas.security.jwt.JwtTokenPair;
import com.github.stazxr.zblog.bas.security.jwt.autoconfigure.properties.JwtCookieProperties;
import com.github.stazxr.zblog.bas.security.jwt.autoconfigure.properties.JwtProperties;
import com.github.stazxr.zblog.bas.security.service.SecurityUserService;
import com.github.stazxr.zblog.util.net.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

/**
 * 自定义认证成功处理器，实现 Spring Security 提供的 {@link AuthenticationSuccessHandler} 接口。
 * 处理用户认证成功后的逻辑，包括生成 JWT Token、存储 Token、记录登录日志以及向客户端返回响应。
 *
 * @author SunTao
 * @since 2024-11-11
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationSuccessHandlerImpl.class);

    private JwtProperties jwtProperties;

    private JwtCookieProperties jwtCookieProperties;

    private JwtTokenGenerator jwtTokenGenerator;

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
            JwtContext jwtContext = new JwtContext();
            jwtContext.setUserId(userId);
            jwtContext.setLoginIp(userIp);
            JwtTokenPair tokenPair = jwtTokenGenerator.generateToken(jwtContext);

            // 记录用户登录日志
            int loginType = 1; // 登录成功
            securityUserService.updateUserLoginInfo(securityUser.getUsername(), loginType, request);

            // 访问令牌
            ResponseCookie accessCookie = ResponseCookie.from(JwtConstants.ACCESS_TOKEN, tokenPair.getAccessToken())
                .httpOnly(jwtCookieProperties.isHttpOnly())
                .secure(jwtCookieProperties.getSecure())
                .domain(jwtCookieProperties.getDomain())
                .path(jwtCookieProperties.getPath())
                .sameSite(jwtCookieProperties.getSameSite())
                .maxAge(Duration.ofSeconds(jwtProperties.getAccessTokenTtl()))
                .build();
            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

            // 刷新令牌
            ResponseCookie refreshTokenCookie = ResponseCookie.from(JwtConstants.REFRESH_TOKEN, tokenPair.getRefreshToken())
                    .httpOnly(jwtCookieProperties.isHttpOnly())
                    .secure(jwtCookieProperties.getSecure())
                    .domain(jwtCookieProperties.getDomain())
                    .path(jwtCookieProperties.getPath())
                    .sameSite(jwtCookieProperties.getSameSite())
                    .maxAge(Duration.ofSeconds(jwtProperties.getRefreshTokenTtl()))
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            ResponseUtils.responseJsonWriter(response, Result.success("登录成功"));
        } catch (Exception e) {
            log.error("处理认证成功逻辑时发生异常：{}", e.getMessage(), e);
            CommonErrorCode errorCode = CommonErrorCode.SBASEA000;
            Result<Object> result = Result.failure(errorCode.getCode(), errorCode.getMessage()).type(ResultType.LOGIN_FAILED);
            ResponseUtils.responseJsonWriter(response, result, HttpStatus.UNAUTHORIZED);
        }
    }

    @Autowired
    public void setJwtProperties(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Autowired
    public void setJwtCookieProperties(JwtCookieProperties jwtCookieProperties) {
        this.jwtCookieProperties = jwtCookieProperties;
    }

    @Autowired
    public void setJwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @Autowired
    public void setSecurityUserService(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }
}
