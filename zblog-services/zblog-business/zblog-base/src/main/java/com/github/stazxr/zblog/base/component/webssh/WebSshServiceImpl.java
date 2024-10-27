package com.github.stazxr.zblog.base.component.webssh;

import com.alibaba.fastjson.JSONObject;
import com.github.stazxr.zblog.base.component.webssh.pojo.SshConnectInfo;
import com.github.stazxr.zblog.base.component.webssh.pojo.SshInfo;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.jsch.JschUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSsh
 *
 * @author SunTao
 * @since 2022-11-02
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSshServiceImpl implements WebSshService {
    private static final Map<String, SshConnectInfo> SSH_MAP = new ConcurrentHashMap<>();

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 连接 WebSSH
     *
     * @param session WebSocketSession
     */
    @Override
    public void connection(WebSocketSession session) {
        SshConnectInfo connectInfo = new SshConnectInfo(session);
        String username = getUsernameFromSession(session);
        SSH_MAP.put(username, connectInfo);
    }

    /**
     * 处理 TextMessage 类型的消息
     *
     * @param message 消息内容
     * @param session session
     */
    @Override
    public void recvHandle(String message, WebSocketSession session) {
        SshInfo sshInfo;
        try {
            sshInfo = JSONObject.parseObject(message, SshInfo.class);
        } catch (Exception e) {
            log.error("Json转换异常, 异常信息:{}", e.getMessage());
            return;
        }

        String username = getUsernameFromSession(session);
        if (ConstantPool.WEB_SSH_OPERATE_CONNECT.equals(sshInfo.getOperate())) {
            SshConnectInfo sshConnectInfo = SSH_MAP.get(username);
            threadPoolTaskExecutor.execute(() -> {
                try {
                    connectToSsh(sshConnectInfo, sshInfo, session);
                    log.info("用户 {} 连接 WebSSH 成功", username);
                } catch (JSchException | IOException e) {
                    log.error("webSsh 连接异常, 异常信息:{}", e.getMessage());
                    close(session);
                }
            });
        } else if (ConstantPool.WEB_SSH_OPERATE_COMMAND.equals(sshInfo.getOperate())) {
            String command = sshInfo.getCommand();
            SshConnectInfo sshConnectInfo = SSH_MAP.get(username);
            if (sshConnectInfo != null) {
                try {
                    transToSsh(sshConnectInfo.getChannel(), command);
                } catch (IOException e) {
                    log.error("webSsh 连接异常, 异常信息:{}", e.getMessage());
                    close(session);
                }
            }
        } else {
            log.error("不支持的操作");
            close(session);
        }
    }

    /**
     * 发送消息
     *
     * @param session WebSocketSession
     * @param buffer  消息字节数组
     * @throws IOException 消息发送失败
     */
    @Override
    public void sendMessage(WebSocketSession session, byte[] buffer) throws IOException {
        session.sendMessage(new TextMessage(buffer));
    }

    /**
     * 关闭链接
     *
     * @param session WebSocketSession
     */
    @Override
    public void close(WebSocketSession session) {
        String username = getUsernameFromSession(session);
        SshConnectInfo connectInfo = SSH_MAP.get(username);
        if (connectInfo != null) {
            Channel channel = connectInfo.getChannel();
            if (channel != null && channel.isConnected()) {
                JschUtils.closeChannel(channel);
            }

            SSH_MAP.remove(username);
        }
    }

    private String getUsernameFromSession(WebSocketSession session) {
        String username = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
        if (StringUtils.isBlank(username)) {
            throw new IllegalStateException("用户不存在");
        }
        return username;
    }

    private void connectToSsh(SshConnectInfo sshConnectInfo, SshInfo sshInfo, WebSocketSession webSocketSession) throws JSchException, IOException {
        Session session = JschUtils.getSession(sshInfo.getHost(), sshInfo.getPort(), sshInfo.getUsername(), sshInfo.getPassword());
        Channel channel = JschUtils.openChannelShell(session);

        // 通道连接 超时时间3s
        channel.connect(3000);
        sshConnectInfo.setChannel(channel);
        transToSsh(channel, "\r");

        // 读取终端返回的信息流
        try (InputStream inputStream = channel.getInputStream()) {
            int i;
            byte[] buffer = new byte[1024];
            while ((i = inputStream.read(buffer)) != -1) {
                sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
            }
        } finally {
            session.disconnect();
            channel.disconnect();
        }
    }

    private void transToSsh(Channel channel, String command) throws IOException {
        if (channel != null) {
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        }
    }
}
