package com.github.stazxr.zblog.base.domain.dto;

import lombok.Data;

/**
 * 用户更新邮箱字段信息
 *
 * @author SunTao
 * @since 2022-08-04
 */
@Data
public class UserUpdateEmailDto {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String pass;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮箱验证码
     */
    private String code;

    /**
     * 缓存标识
     */
    private String uuid;
}
