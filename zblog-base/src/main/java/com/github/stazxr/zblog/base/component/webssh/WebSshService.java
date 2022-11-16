package com.github.stazxr.zblog.base.component.webssh;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * WebSocket 业务逻辑
 *
 * @author SunTao
 * @since 2022-11-02
 */
public interface WebSshService {
    /**
     * 连接 WebSSH
     *
     * @param session WebSocketSession
     */
    void connection(WebSocketSession session);

    /**
     * 处理 TextMessage 类型的消息
     *
     * @param message 消息内容
     * @param session session
     */
    void recvHandle(String message, WebSocketSession session);

    /**
     * 发送消息
     *
     * @param session WebSocketSession
     * @param buffer  消息字节数组
     * @throws IOException 消息发送失败
     */
    void sendMessage(WebSocketSession session, byte[] buffer) throws IOException;

    /**
     * 关闭链接
     *
     * @param session WebSocketSession
     */
    void close(WebSocketSession session);
}
