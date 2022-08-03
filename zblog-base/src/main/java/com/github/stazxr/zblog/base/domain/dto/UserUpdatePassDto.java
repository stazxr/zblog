package com.github.stazxr.zblog.base.domain.dto;

import lombok.Data;

/**
 * 用户更新密码字段信息
 *
 * @author SunTao
 * @since 2022-08-02
 */
@Data
public class UserUpdatePassDto {
    /**
     * 用户名
     */
    private String username;

    /**
     * 旧密码
     */
    private String oldPass;

    /**
     * 新密码
     */
    private String newPass;

    /**
     * 密码确认
     */
    private String confirmPass;
}
