package com.github.stazxr.zblog.util.jsch;

import cn.hutool.core.lang.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.jcraft.jsch.*;

import java.util.Properties;

/**
 * Jsch 工具类
 *
 * @author SunTao
 * @since 2022-11-02
 */
public class JschUtils {
    /**
     * 30s
     */
    private static final int SSH_TIMEOUT = 30000;

    private static final String EXEC_CHANNEL = "exec";

    private static final String SHELL_CHANNEL = "shell";

    private static final Properties PROPERTIES = new Properties();

    static {
        PROPERTIES.put("StrictHostKeyChecking", "no");
    }

    /**
     * 获取会话
     *
     * @param host     主机地址
     * @param port     端口
     * @param username 登录用户
     * @param password 登录密码
     * @return Session
     */
    public static Session getSession(String host, int port, String username, String password) throws JSchException {
        Assert.notEmpty(host, "SSH Host must be not empty!");
        Assert.isTrue(port > 0, "SSH port must be > 0");
        if (StringUtils.isBlank(username)) {
            username = "root";
        }

        JSch jSch = new JSch();
        Session session = jSch.getSession(username, host, port);
        session.setPassword(password);
        session.setTimeout(SSH_TIMEOUT);
        session.setConfig(PROPERTIES);
        session.connect();
        return session;
    }

    /**
     * 开启 Exec 通道
     *
     * @param session 会话信息
     * @return ChannelExec
     * @throws JSchException 发生异常
     */
    public static ChannelExec openChannelExec(Session session) throws JSchException {
        return (ChannelExec) session.openChannel(EXEC_CHANNEL);
    }

    /**
     * 开启 Shell 通道
     *
     * @param session 会话信息
     * @return ChannelShell
     * @throws JSchException 发生异常
     */
    public static ChannelShell openChannelShell(Session session) throws JSchException {
        return (ChannelShell) session.openChannel(SHELL_CHANNEL);
    }

    /**
     * 关闭会话
     *
     * @param session 会话
     */
    public static void closeSession(Session session) {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    /**
     * 关闭通道
     *
     * @param channel Exec Or Shell 通道
     */
    public static void closeChannel(Channel channel) {
        if (channel != null) {
            channel.disconnect();
        }
    }
}
