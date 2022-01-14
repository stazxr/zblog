package com.github.stazxr.zblog.base.domain.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {
    /**
     * 用户名
     */
    @NotBlank
    private String username;

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识，验证码的ID
     */
    private String uuid = "";
}
