package com.github.stazxr.zblog.base.component.webssh.pojo;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

/**
 * Ssh 链接信息
 *
 * @author SunTao
 * @since 2022-11-02
 */
@Data
public class SshConnectInfo {
    private WebSocketSession webSocketSession;

    private JSch jSch;

    private Channel channel;

    public SshConnectInfo(WebSocketSession webSocketSession) {
        this.jSch = new JSch();
        this.webSocketSession = webSocketSession;
    }
}
