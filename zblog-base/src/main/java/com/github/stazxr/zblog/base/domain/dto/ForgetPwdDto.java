package com.github.stazxr.zblog.base.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 修改密码信息
 *
 * @author SunTao
 * @since 2023-02-06
 */
@Getter
@Setter
@ToString
public class ForgetPwdDto {
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
