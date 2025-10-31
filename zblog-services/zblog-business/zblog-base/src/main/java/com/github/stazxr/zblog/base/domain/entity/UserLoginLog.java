package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户登录日志
 *
 * @author SunTao
 * @since 2025-10-17
 */
@Getter
@Setter
@TableName("user_login_log")
public class UserLoginLog {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 用户名
     */
    private Long userId;

    /**
     * 登录时间
     */
    private String loginTime;

    /**
     * 登录渠道, 01:移动端; 02:PC端
     */
    private String loginChan;

    /**
     * 登录类型, 00:访客登录; 01:密码登录; 02:QQ登录; 99:未知
     */
    private String loginType;

    /**
     * 登录IP
     */
    private String loginIp;

    /**
     * 登录地址
     */
    private String loginAddress;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 登录是否成功
     */
    private Boolean isSuccess;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建日期
     */
    private String createDate;

    /**
     * 创建时间
     */
    private String createTime;
}
