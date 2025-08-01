package com.github.stazxr.zblog.bas.security.core;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 表示用户令牌的实体类。
 *
 * @author SunTao
 * @since 2024-11-17
 */
public class SecurityToken implements Serializable {
    private static final long serialVersionUID = -8826952103326901594L;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 用户令牌
     */
    private String token;

    /**
     * 令牌版本
     */
    private int version;

    /**
     * 备注
     */
    private String remark;

    /**
     * 令牌创建时间
     */
    private String createTime;

    /**
     * 令牌修改时间
     */
    private String modifyTime;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 构造函数，用于初始化用户令牌。
     *
     * @param userId 用户 ID
     * @param token  令牌字符串
     */
    public SecurityToken(String userId, String token) {
        this.userId = userId;
        this.token = token;
        this.version = 1;
        this.remark = "新建";
        updateCreateTime();
    }

    /**
     * 获取用户 ID。
     *
     * @return 用户 ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户 ID。
     *
     * @param userId 用户 ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取令牌字符串。
     *
     * @return 令牌字符串
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置令牌字符串。
     *
     * @param token 令牌字符串
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取版本号。
     *
     * @return 当前版本号
     */
    public int getVersion() {
        return version;
    }

    /**
     * 设置版本号。
     *
     * @param version 版本号
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * 获取备注信息。
     *
     * @return 备注信息
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注信息。
     *
     * @param remark 备注信息
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取创建时间。
     *
     * @return 格式化后的创建时间字符串
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 更新创建时间为当前时间。
     */
    public void updateCreateTime() {
        this.createTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

    /**
     * 获取修改时间。
     *
     * @return 格式化后的修改时间字符串
     */
    public String getModifyTime() {
        return modifyTime;
    }

    /**
     * 更新修改时间为当前时间。
     */
    public void updateModifyTime() {
        this.modifyTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }
}

