package com.github.stazxr.zblog.base.component.webssh.config;

import com.github.stazxr.zblog.base.component.webssh.handler.WebSocketHandlerImpl;
import com.github.stazxr.zblog.base.component.webssh.interceptor.SetUserSocketInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket 配置
 *
 * @author SunTao
 * @since 2022-11-02
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSshWebSocketConfig implements WebSocketConfigurer {
    private static final String WEB_SSH_HANDLER = "/webssh";

    private final WebSocketHandlerImpl webSocketHandler;

    private final SetUserSocketInterceptor setUserSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(webSocketHandler, WEB_SSH_HANDLER)
                .addInterceptors(setUserSocketInterceptor)
                .setAllowedOrigins("*");
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
