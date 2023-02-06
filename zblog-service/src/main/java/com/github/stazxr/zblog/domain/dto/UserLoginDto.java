package com.github.stazxr.zblog.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户登录信息
 *
 * @author SunTao
 * @since 2023-02-05
 */
@Getter
@Setter
@ToString
public class UserLoginDto {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
