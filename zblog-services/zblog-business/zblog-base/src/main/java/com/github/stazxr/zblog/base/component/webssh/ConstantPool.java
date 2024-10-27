package com.github.stazxr.zblog.base.component.webssh;

/**
 * 常量池
 *
 * @author SunTao
 * @since 2022-11-02
 */
public final class ConstantPool {
    /**
     * ZBLOG 系统用户缓存主键
     */
    public static final String USER_UUID_KEY = "userUuidKey";

    /**
     * ZBLOG 系统用户令牌
     */
    public static final String USER_TOKEN_KEY = "userTokenKey";

    /**
     * ZBLOG 系统用户创建时间
     */
    public static final String USER_CREATE_TIME_KEY = "userCreateTimeKey";

    /**
     * 发送指令：连接
     */
    public static final String WEB_SSH_OPERATE_CONNECT = "connect";

    /**
     * 发送指令：命令
     */
    public static final String WEB_SSH_OPERATE_COMMAND = "command";
}
