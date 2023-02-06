package com.github.stazxr.zblog.base.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户注册信息
 *
 * @author SunTao
 * @since 2023-02-05
 */
@Getter
@Setter
@ToString
public class UserRegisterDto {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String code;

    /**
     * UUID
     */
    private String uuid;
}
