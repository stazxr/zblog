package com.github.stazxr.zblog.base.component.webssh.handler;

import com.github.stazxr.zblog.base.component.webssh.ConstantPool;
import com.github.stazxr.zblog.base.component.webssh.WebSshService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

/**
 * WebSocket 处理器
 *
 * @author SunTao
 * @since 2022-11-02
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandlerImpl implements WebSocketHandler {
    private final WebSshService webSshService;

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        log.info("【WebSSH】用户 {} 连接 WebSSH", user(webSocketSession));
        webSshService.connection(webSocketSession);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) {
        if (webSocketMessage instanceof TextMessage) {
            String payload = ((TextMessage) webSocketMessage).getPayload();
            log.debug("【WebSSH】用户 {} 发送命令: {}", user(webSocketSession), payload);
            webSshService.recvHandle(payload, webSocketSession);
        } else if (webSocketMessage instanceof BinaryMessage) {
            log.warn("【WebSSH】用户 {} 发送 BinaryMessage 命令，暂不支持处理", user(webSocketSession));
        } else if (webSocketMessage instanceof PongMessage) {
            log.warn("【WebSSH】用户 {} 发送 PongMessage 命令，暂不支持处理", user(webSocketSession));
        } else {
            log.warn("【WebSSH】用户 {} 发送的消息类型不被支持: {}", user(webSocketSession), webSocketMessage);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) {
        log.error("【WebSSH】用户 {} 发生数据传输错误", user(webSocketSession), throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus status) {
        log.info("【WebSSH】用户 {} 断开 WebSSH 连接：{}【{}】", user(webSocketSession), status.getCode(), status.getReason());
        webSshService.close(webSocketSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        // WebSocketHandler 是否处理部分消息
        return false;
    }

    private String user(WebSocketSession socketSession) {
        return String.valueOf(socketSession.getAttributes().get(ConstantPool.USER_UUID_KEY));
    }
}
