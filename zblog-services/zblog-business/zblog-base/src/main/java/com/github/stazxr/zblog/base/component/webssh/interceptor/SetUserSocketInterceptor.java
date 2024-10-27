package com.github.stazxr.zblog.base.component.webssh.interceptor;

import com.github.stazxr.zblog.base.component.security.jwt.decoder.JwtDecoder;
import com.github.stazxr.zblog.base.component.webssh.ConstantPool;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.service.UserService;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * WebSocket 拦截器，设置当前操作用户
 *
 * @author SunTao
 * @since 2022-11-02
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SetUserSocketInterceptor implements HandshakeInterceptor {
    private static final String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";

    private final JwtDecoder jwtDecoder;

    private final UserService userService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
            String token = request.getHeader(SEC_WEBSOCKET_PROTOCOL);
            try {
                JWTClaimsSet claimsSet = jwtDecoder.decode(token);
                List<String> audiences = claimsSet.getAudience();
                if (audiences != null && !audiences.isEmpty()) {
                    String userId = audiences.get(0);
                    User tokenUser = userService.getById(userId);
                    attributes.put(ConstantPool.USER_UUID_KEY, tokenUser.getUsername());
                    attributes.put(ConstantPool.USER_TOKEN_KEY, token);
                    attributes.put(ConstantPool.USER_CREATE_TIME_KEY, DateUtils.formatNow());
                    attributes.put(HttpSessionHandshakeInterceptor.HTTP_SESSION_ID_ATTR_NAME, request.getSession().getId());
                    log.info("【beforeHandshake】 userId = {}, cache = {}", userId, attributes);
                    return true;
                }
            } catch (Exception e) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                log.error("【beforeHandshake】 authorization Parse failure", e);
            }
        } else {
            log.error("【beforeHandshake】 request is not instanceof ServletServerHttpRequest");
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {
        HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
        HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
        if (StringUtils.isNotEmpty(httpRequest.getHeader(SEC_WEBSOCKET_PROTOCOL))) {
            log.info("【afterHandshake】 set header - [{}]", SEC_WEBSOCKET_PROTOCOL, exception);
            httpResponse.addHeader(SEC_WEBSOCKET_PROTOCOL, httpRequest.getHeader(SEC_WEBSOCKET_PROTOCOL));
        }

        if (exception != null) {
            log.error("【afterHandshake】 catch an exception", exception);
        }
    }
}
